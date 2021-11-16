package com.codingmart.api_mart.service;

import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface CollectionService {
    Table upload(MultipartFile multipartFile, User user);

    List<Table> getAllCollectionByUser(String username);

    List<Map<String, String>> getCollectionByUser(String user, String fileName, Map<String, String> queryParams);

    Map<String, String> insertRecord(String user, String fileName, Map<String, String> requestBody);

    Map<String, String> updateRecord(String user, String fileName, Map<String, String> queryParams, Map<String, String> requestBody);

    boolean deleteRecord(String user, String fileName, Map<String, String> queryParams);

    String deleteCollectionByUser(String fileName, String username);

    File createFile(String fileName, String type, User user);
}
