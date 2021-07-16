package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.service.CollectionService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @PostMapping("/upload")
    public ResponseBody uploadCollection(@RequestParam("File") MultipartFile file, HttpServletRequest request) {
        return collectionService.uploadCollection(file, request);
    }

    @GetMapping("/get")
    public ResponseBody getAllCollectionByUser(HttpServletRequest request) {
        return collectionService.getAllCollectionByUser(request);
    }

    @GetMapping("/{user}/{fileName}")
    public ResponseBody getCollectionByUser(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String,String> queryParams ) {
        System.out.println("queryParams = " + queryParams);
        return collectionService.getCollectionByUser(user, fileName, queryParams);
    }


}
