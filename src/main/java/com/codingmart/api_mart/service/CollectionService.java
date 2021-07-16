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

        String token = request.getHeader("Authorization");
        String username = GetTokenPayload.getPayload(token, "sub");

        System.out.println("username = " + username);
//        String username = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();

        String fullName = file.getOriginalFilename().replace(" ", "");
        String filePath = "src/main/resources/uploads/" + fullName;
        File myFile= new File(filePath);
        String fileName = fullName.split("[.]")[0];
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
        ) {
            String[] headers = br.readLine().split(",");
            List<Map<String, String>> records =
                    br.lines().map(s -> s.split(","))
                            .map(t -> IntStream.range(0, t.length)
                                    .boxed()
                                    .collect(toMap(i -> headers[i], i -> t[i])))
                            .collect(toList());

            boolean isSaved = collectionRepository.saveCollection(collectionName, records);
            if(isSaved) {
                userTableRepository.save(new Table(username, fileName, collectionName));
                return getResponseBody("Success Data Saved", null);
            }
            System.out.println(headers);
            System.out.println(records);
            return getResponseBody(false, 400, "Unable to parse csv", null);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseBody(false, 400, "Error in csv file", null);
        }

    }

    public ResponseBody getAllCollectionByUser(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String username = GetTokenPayload.getPayload(token, "sub");

        List<Table> tables = userTableRepository.findByUser(username);

        ResponseBody responseBody = getResponseBody("Success", tables);
        return responseBody;
    }

    public ResponseBody getCollectionByUser(String user, String fileName) {
        String collectionName = (user + fileName).toLowerCase();
        List<HashMap<String, String>> records = null;
        try {
            records = collectionRepository.getRecordsByCollection(collectionName);
        } catch (IOException e) {
            e.printStackTrace();
            return getResponseBody(false, 500, e.getMessage(), null);
        }

        return getResponseBody("Success", records);
    }

    private ResponseBody getResponseBody(String message, Object data){
        return new ResponseBody(message, data);
    }

    private ResponseBody getResponseBody(boolean status, int status_code, String message, Object data){
        return new ResponseBody(status, status_code, message, data);
    }

}
