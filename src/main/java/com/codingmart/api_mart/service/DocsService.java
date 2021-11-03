package com.codingmart.api_mart.service;

import com.codingmart.api_mart.utils.ResponseResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Service
public class DocsService {

    @Autowired
    ResponseResource resource;

    public ResponseEntity<Resource> getOpenApiFile(HttpServletRequest request) {
        String filePath = "src/main/resources/docs/API_MART_OPEN_API.yaml";
        File file = new File(filePath);

        return resource.getResourceResponseEntity(file, "open_api.yml");
    }

    public ResponseEntity<Resource> getPostmanCollection() {
        String filePath = "src/main/resources/docs/API_MART_Postman_Collection.json";
        File file = new File(filePath);

        return resource.getResourceResponseEntity(file, "attachment; filename=postman_collection.json");
    }

}
