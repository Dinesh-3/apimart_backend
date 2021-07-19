package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.service.CollectionService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @GetMapping()
    public ResponseBody getAllCollectionByUser(HttpServletRequest request) {
        return collectionService.getAllCollectionByUser(request);
    }

    @DeleteMapping("/{fileName}")
    public ResponseBody deleteCollectionByUser(@PathVariable String fileName, HttpServletRequest request) {
        return collectionService.deleteCollectionByUser(fileName, request);
    }

    @GetMapping("/get/{user}/{fileName}")
    public ResponseBody getCollectionByUser(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String,String> queryParams ) {
        return collectionService.getCollectionByUser(user, fileName, queryParams);
    }

    @PostMapping("/{user}/{fileName}")
    public ResponseBody insertRecord(@PathVariable String user, @PathVariable String fileName, @RequestBody Map<String, String> requestBody) {
        return collectionService.insertRecord(user, fileName, requestBody);
    }

    @PutMapping("/{user}/{fileName}")
    public ResponseBody updateRecord(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String, String> queryParams, @RequestBody Map<String, String> requestBody) {
        return collectionService.updateRecord(user, fileName, queryParams, requestBody);
    }

    @DeleteMapping("/{user}/{fileName}")
    public ResponseBody deleteRecord(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String, String> queryParams) {
        return collectionService.deleteRecord(user, fileName, queryParams);
    }

    @PostMapping("/upload")
    public ResponseBody uploadCollection(@RequestParam("File") MultipartFile file, HttpServletRequest request) {
        return collectionService.uploadCollection(file, request);
    }

}
