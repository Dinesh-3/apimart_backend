package com.codingmart.api_mart.service;

import com.codingmart.api_mart.ExceptionHandler.ClientErrorException;
import com.codingmart.api_mart.ExceptionHandler.StatusCodeException;
import com.codingmart.api_mart.configuration.SystemConfig;
import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.CollectionRepository;
import com.codingmart.api_mart.repository.UserTableRepository;
import com.codingmart.api_mart.utils.FileName;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.format;

@Service
public class CollectionService {
    private static final String[] fileTypes = {"csv", "xlsx", "xls"};
    private static final Set<String> supportedFileTypes = new HashSet<>(Arrays.asList(fileTypes));

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserTableRepository userTableRepository;

    public Table upload(MultipartFile multipartFile, User user) {
        String fullName = multipartFile.getOriginalFilename().replace(" ", "");
        FileName fileName = new FileName(fullName);
        String fileType = fileName.getType();

        if(!supportedFileTypes.contains(fileType)) throw new ClientErrorException(HttpStatus.NOT_ACCEPTABLE, format("File Type: %s Not supported", fileType));

        String collectionName = getCollectionName(user.getName(), fileName);
        System.out.println("collectionName = " + collectionName);
        File saveFile = saveFile(multipartFile, fullName);
        List<Map<String, Object>> records = getRecords(fileType, saveFile);

        collectionRepository.saveCollection(collectionName, records);
        Table table = userTableRepository.save(new Table(user.getName(), fileName.getFullName(), collectionName));
        return table;
    }

    private String getCollectionName(String username, FileName fileName) {
        String collectionName = username + fileName;
        handleReservedCollection(collectionName);
        boolean isTableExist = userTableRepository.isExist(username, collectionName);
        if(isTableExist) throw new ClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Table already exist. Change file name");
        return collectionName;
    }

    private List<Map<String, Object>> getRecords(String fileType, File myFile) {
        try (
                Reader reader = new FileReader(myFile.getAbsolutePath());
                FileInputStream fileInputStream = new FileInputStream(myFile)
        ) {
            if(fileType.equals("csv")) return parseFromCsv(reader);
            if(fileType.equals("xlsx")) return parseFromXlsx(new XSSFWorkbook(fileInputStream));
            if(fileType.equals("xls")) return parseFromXlsx(new HSSFWorkbook(fileInputStream));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientErrorException(400, format("Error in %s file: %s" , fileType, e.getMessage()));
        }finally {
            deleteFile(myFile.getAbsolutePath());
        }
        throw new ClientErrorException(400, String.format("Invalid File Type: %s", fileType));
    }

    private void deleteFile(String filePath) {
        String userDirectory = Paths.get("").toAbsolutePath().toString();
        try {
            Files.deleteIfExists(Paths.get(userDirectory + "/" + filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File saveFile(MultipartFile multipartFile, String fileName) {
        //        try (
//                FileOutputStream fos=new FileOutputStream(myFile);
//                ) {
//            myFile.createNewFile();
//            fos.write(file.getBytes());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            System.out.println("fileName = " + fileName);
            String saveFilePath = Paths.get("").toAbsolutePath() + "/src/main/resources/uploads/" + fileName;
            File saveFile = new File(saveFilePath);
            saveFile.createNewFile();
            multipartFile.transferTo(saveFile);
            return saveFile;
        } catch (IOException e) {
            e.printStackTrace();
            throw new StatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to parse file Try again");
        }
    }

    public List<Table> getAllCollectionByUser(String username) {
        return userTableRepository.findByUser(username);
    }

    public List<HashMap<String, String>> getCollectionByUser(String user, String fileName, Map<String,String> queryParams) {
        String collectionName = (user.toLowerCase() + fileName);
        handleReservedCollection(collectionName);
        List<HashMap<String, String>> records;
        try {
            records = collectionRepository.getRecordsByCollection(collectionName, queryParams);
        } catch (IOException e) {
            e.printStackTrace();
            throw new StatusCodeException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return records;
    }

    private void handleReservedCollection(String collectionName) {
        if(SystemConfig.getReservedCollections().contains(collectionName)) throw new ClientErrorException(HttpStatus.NOT_ACCEPTABLE, "File Name Not available change file name to continue");
    }

    public Map<String, String> insertRecord(String user, String fileName, Map<String, String> requestBody) {
        String collectionName = (user.toLowerCase() + fileName);
        handleReservedCollection(collectionName);
        return collectionRepository.insertRecord(collectionName, requestBody);
    }

    public Map<String, String> updateRecord(String user, String fileName, Map<String, String> queryParams, Map<String, String> requestBody) {
        String collectionName = (user.toLowerCase() + fileName);
        handleReservedCollection(collectionName);
        return collectionRepository.updateRecord(collectionName, queryParams, requestBody);
    }

    public boolean deleteRecord(String user, String fileName, Map<String, String> queryParams) {
        String collectionName = (user.toLowerCase() + fileName);
        handleReservedCollection(collectionName);
        return collectionRepository.deleteRecord(collectionName, queryParams);
    }

    public String deleteCollectionByUser(String fileName, String username) {
        userTableRepository.deleteTableByUser(username.toLowerCase(), fileName);

        return "Collection Deleted Successfully";
    }

    private List<Map<String, Object>> parseFromCsv(Reader reader) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

        Iterator<String> csvHeader = records.iterator().next().iterator();
        List<String> headers = new ArrayList<>();
        while (csvHeader.hasNext()) {
            String value = csvHeader.next();
            headers.add(value);
        }

        List<Map<String, Object>> listOfRecords = new ArrayList<>();

        for (CSVRecord record : records) {
            Iterator<String> recordIterable = record.iterator();
            Map<String, Object> rowMap = new HashMap<>();
            int index = 0;
            while (recordIterable.hasNext()) {
                String value = recordIterable.next();
                rowMap.put(headers.get(index), value);
                ++ index;
            }
            listOfRecords.add(rowMap);
        }
        return listOfRecords;
    }


    private List<Map<String, Object>> parseFromXlsx(Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Row header = sheet.getRow(0);
        List<Map<String, Object>> records = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            Iterator<Cell> cellIterator = currentRow.cellIterator();
            Map<String, Object> rowMap = new HashMap<>();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                int cellIndex = cell.getColumnIndex();

                String key = getCellValue(header.getCell(cellIndex)).toString();
                Object value = getCellValue(cell);

                rowMap.put(key,value);
            }
            records.add(rowMap);
        }
        workbook.close();
        return records;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

}
