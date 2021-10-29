package com.codingmart.api_mart.utils;

import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ControllerResponse {
    public static  <T> ResponseEntity<ResponseBody> getResponseEntity(T getCollections) {
        return new ResponseEntity<>(new ResponseBody(getCollections), HttpStatus.OK);
    }
}
