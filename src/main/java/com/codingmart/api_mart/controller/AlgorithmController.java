package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.service.AlgorithmService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/algorithm")
public class AlgorithmController {
    private final AlgorithmService service;

     public AlgorithmController(AlgorithmService service) {
        this.service = service;
    }

    @GetMapping("/possiblePathInGrid/{grid}")
    ResponseEntity<ResponseBody> possiblePathsInGrid(@PathVariable("grid") int count){
        int result = service.possiblePathsInGrid(count);
        return new ResponseEntity<>(new ResponseBody("Success", result), HttpStatus.OK);
    }

}
