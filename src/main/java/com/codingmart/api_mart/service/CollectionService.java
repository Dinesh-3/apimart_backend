package com.codingmart.api_mart.service;

import com.codingmart.api_mart.ExceptionHandler.ClientErrorException;
import com.codingmart.api_mart.ExceptionHandler.StatusCodeException;
import com.codingmart.api_mart.configuration.SystemConfig;
import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.CollectionRepository;
import com.codingmart.api_mart.repository.UserTableRepository;
import com.codingmart.api_mart.utils.FileName;
import com.dinesh.apimart.filereader.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.format;

@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private UserTableRepository userTableRepository;


    public Table upload(MultipartFile multipartFile, User user) {
        String fullName = multipartFile.getOriginalFilename().replace(" ", "");
        FileName fileName = new FileName(fullName);
        String fileType = fileName.getType();

        if(!FileParser.supports(fileType)) throw new ClientErrorException(HttpStatus.NOT_ACCEPTABLE, format("File Type: %s Not supported", fileType));

        String collectionName = getCollectionName(user.getName(), fileName.getName());
        File saveFile = saveFile(multipartFile, fullName);

        try {
            List<Map<String, Object>> records = FileParser.parseMap(saveFile);
            collectionRepository.saveCollection(collectionName, records);
            Table table = new Table(user.getName(), fileName.getName(), collectionName);
            userTableRepository.save(table);
            return table;
        }catch (Exception e) {
            throw new StatusCodeException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    private String getCollectionName(String username, String fileName) {
        String collectionName = username + fileName;
        handleReservedCollection(collectionName);
        boolean isTableExist = userTableRepository.isExist(username, collectionName);
        if(isTableExist) throw new ClientErrorException(HttpStatus.NOT_ACCEPTABLE, "Table already exist. Change file name");
        return collectionName;
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

    public List<Map<String, String>> getCollectionByUser(String user, String fileName, Map<String,String> queryParams) {
        String collectionName = (user.toLowerCase() + fileName);
        handleReservedCollection(collectionName);
        List<Map<String, String>> records;
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

    public File createFile(String fileName, String type, User user) {
        List<Map<String, String>> records = getCollectionByUser(user.getName(), fileName, new HashMap<>());
        File file = FileParser.parseFile(records, String.format("%s--%s.%s", user.getName(), fileName, type));
        return file;
    }
}
