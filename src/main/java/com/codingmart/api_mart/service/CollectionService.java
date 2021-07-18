package com.codingmart.api_mart.service;

import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.repository.CollectionRepository;
import com.codingmart.api_mart.repository.UserRepository;
import com.codingmart.api_mart.repository.UserTableRepository;
import com.codingmart.api_mart.utils.GetTokenPayload;
import com.codingmart.api_mart.utils.ResponseBody;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class CollectionService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserTableRepository userTableRepository;

    public ResponseBody uploadCollection(MultipartFile file, HttpServletRequest request) {
        String fullName = file.getOriginalFilename().replace(" ", "");
        String[] splitName = fullName.split("[.]");
        String fileName = splitName[0];
        String fileType = splitName[1];
        if(fileType.equals("csv") || fileType.equals("xlsx") || fileType.equals("xls")) System.out.println("fileType = " + fileType);
        else return getResponseBody(false, 400, "File Type Not supported", null);
        String token = request.getHeader("Authorization");
        String username = GetTokenPayload.getPayload(token, "sub");


        String filePath = "src/main/resources/uploads/" + fullName;
        File myFile= new File(filePath);

        String collectionName = username + fileName;

        boolean isTableExist = userTableRepository.isUserAndCollectionExists(username, collectionName);

        if(isTableExist) return getResponseBody(false, 400, "table already exist please change file name", null);

        try (
                FileOutputStream fos=new FileOutputStream(myFile);
                ) {
            myFile.createNewFile();
            fos.write(file.getBytes());
            System.out.println("File Stored Successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (
                Reader reader = new FileReader(filePath);
                BufferedReader br = new BufferedReader(reader);
                FileInputStream fileInputStream = new FileInputStream(myFile);
        ) {
            List<Map<String, Object>> records = new ArrayList<>();
            if (fileType.equals("csv")) records = getHashMapFromApacheCSV(reader);
            if(fileType.equals("xlsx")) records = getHashMapFromXL(new XSSFWorkbook(fileInputStream));
            if(fileType.equals("xls")) records = getHashMapFromXL(new HSSFWorkbook(fileInputStream));
            boolean isSaved = collectionRepository.saveCollection(collectionName, records);
            if(isSaved) {
                Table table = userTableRepository.save(new Table(username, fileName, collectionName));
                return getResponseBody("Success Data Saved", table);
            }
            return getResponseBody(false, 400, "Unable to parse " + fileType + " File", null);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseBody(false, 400, "Error in " + fileType + " file " + e.getMessage(), null);
        }finally {
            String userDirectory = Paths.get("").toAbsolutePath().toString();
            try {
                Files.deleteIfExists(Paths.get(userDirectory + "/" + filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public ResponseBody getAllCollectionByUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = GetTokenPayload.getPayload(token, "sub");

        List<Table> tables = userTableRepository.findByUser(username);

        ResponseBody responseBody = getResponseBody("Success", tables);
        return responseBody;
    }

    public ResponseBody getCollectionByUser(String user, String fileName, Map<String,String> queryParams) {
        String collectionName = (user.toLowerCase() + fileName);
        List<HashMap<String, String>> records = null;
        try {
            records = collectionRepository.getRecordsByCollection(collectionName, queryParams);
        } catch (IOException e) {
            e.printStackTrace();
            return getResponseBody(false, 500, e.getMessage(), null);
        }

        return getResponseBody("Success", records);
    }

    public ResponseBody deleteCollectionByUser(String fileName, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = GetTokenPayload.getPayload(token, "sub");

        userTableRepository.deleteTableByUser(username.toLowerCase(), fileName);

        return getResponseBody("Collection Deleted Successfully", true);

    }

    private ResponseBody getResponseBody(String message, Object data){
        return new ResponseBody(message, data);
    }

    private List<Map<String, Object>> getHashMapFromCSV(BufferedReader br) throws IOException {
        String[] headers = br.readLine().split(",");
        List<Map<String, Object>> records =
                br.lines().map(s -> s.split(","))
                        .map(t -> IntStream.range(0, t.length)
                                .boxed()
                                .collect(toMap(i -> headers[i], i -> (Object) t[i])))
                        .collect(toList());
        return records;
    }

    private List<Map<String, Object>> getHashMapFromApacheCSV(Reader reader) throws IOException {
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);

        Iterator<String> csvHeader = records.iterator().next().iterator();
        List<String> headers = new ArrayList<>();
        while (csvHeader.hasNext()) {
            String value = csvHeader.next();
            headers.add(value);
        }
        System.out.println("headers = " + headers);

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


    private List<Map<String, Object>> getHashMapFromXL(Workbook workbook) throws IOException {
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
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    private ResponseBody getResponseBody(boolean status, int status_code, String message, Object data){
        return new ResponseBody(status, status_code, message, data);
    }


}
