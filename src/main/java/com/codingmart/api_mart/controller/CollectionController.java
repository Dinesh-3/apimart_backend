package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.service.CollectionService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @PostMapping("/upload/{fileName}")
    public ResponseBody uploadCollection(@PathVariable String fileName, @RequestBody Object data){
        return collectionService.uploadCollection(fileName, data);
    }



}
