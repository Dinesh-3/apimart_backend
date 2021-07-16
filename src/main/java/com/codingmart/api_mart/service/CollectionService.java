package com.codingmart.api_mart.service;

import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.repository.CollectionRepository;
import com.codingmart.api_mart.repository.UserRepository;
import com.codingmart.api_mart.repository.UserTableRepository;
import com.codingmart.api_mart.utils.GetTokenPayload;
import com.codingmart.api_mart.utils.ResponseBody;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.mongodb.client.FindIterable;
import io.jsonwebtoken.Jwts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${jwt.secret-key}")
    private String secretKey;

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
        if(fileType.equals("csv") || fileType.equals("xlsx")) System.out.println("fileType = " + fileType);
        else return getResponseBody(false, 400, "File Type Not supported", null);
        String token = request.getHeader("Authorization");
        String username = GetTokenPayload.getPayload(token, "sub");

        System.out.println("username = " + username);
//        String username = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

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
            List<Map<String, String>> records = new ArrayList<>();
            if (fileType.equals("csv")) records = getHashMapFromCSV(br);
            if(fileType.equals("xlsx")) records = getHashMapFromXLSX(fileInputStream);
            boolean isSaved = collectionRepository.saveCollection(collectionName, records);
            if(isSaved) {
                userTableRepository.save(new Table(username, fileName, collectionName));
                String userDirectory = Paths.get("").toAbsolutePath().toString();
                System.out.println("userDirectory = " + userDirectory+filePath);
                Files.deleteIfExists(Paths.get(userDirectory + "/" + filePath));
                return getResponseBody("Success Data Saved", null);
            }
            System.out.println(records);
            return getResponseBody(false, 400, "Unable to parse " + fileType + " File", null);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseBody(false, 400, "Error in " + fileType + " file", null);
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
        String collectionName = (user + fileName).toLowerCase();
        List<HashMap<String, String>> records = null;
        try {
            records = collectionRepository.getRecordsByCollection(collectionName, queryParams);
        } catch (IOException e) {
            e.printStackTrace();
            return getResponseBody(false, 500, e.getMessage(), null);
        }

        return getResponseBody("Success", records);
    }

    private ResponseBody getResponseBody(String message, Object data){
        return new ResponseBody(message, data);
    }

    private List<Map<String, String>> getHashMapFromCSV(BufferedReader br) throws IOException {
        String[] headers = br.readLine().split(",");
        List<Map<String, String>> records =
                br.lines().map(s -> s.split(","))
                        .map(t -> IntStream.range(0, t.length)
                                .boxed()
                                .collect(toMap(i -> headers[i], i -> t[i])))
                        .collect(toList());
        return records;
    }

    private List<Map<String, String>> getHashMapFromXLSX(FileInputStream fileInputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Row header = sheet.getRow(0);
        List<Map<String, String>> records = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            Iterator<Cell> cellIterator = currentRow.cellIterator();
            Map<String, String> rowMap = new HashMap<>();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                int cellIndex = cell.getColumnIndex();
                cell.setCellType(CellType.STRING);
                String key = header.getCell(cellIndex).getStringCellValue();
                String value = cell.getStringCellValue();
                rowMap.put(key,value);
            }
//            System.out.println("rowMap = " + rowMap);
            records.add(rowMap);
        }

        return records;
    }

    private ResponseBody getResponseBody(boolean status, int status_code, String message, Object data){
        return new ResponseBody(status, status_code, message, data);
    }

}
