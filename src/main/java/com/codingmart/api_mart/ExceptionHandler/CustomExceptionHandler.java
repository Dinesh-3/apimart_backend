package com.codingmart.api_mart.ExceptionHandler;

import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

@ControllerAdvice
public class CustomExceptionHandler {

//    @ExceptionHandler(HttpClientErrorException.class)
//    public ResponseEntity<ResponseBody> clientErrorException(HttpClientErrorException ex) {
//        ResponseBody responseBody = new ResponseBody(false,ex.getStatusCode().value(), ex.getResponseBodyAsString());
//        return new ResponseEntity<>(responseBody, ex.getStatusCode());
//    }
//
//    @ExceptionHandler(HttpStatusCodeException.class)
//    public ResponseEntity<ResponseBody> httpStatusCodeException(HttpStatusCodeException ex) {
//        ResponseBody responseBody = new ResponseBody(false,ex.getStatusCode().value(), ex.getResponseBodyAsString());
//        return new ResponseEntity<>(responseBody, ex.getStatusCode());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResponseBody> handleException(Exception ex) {
//        ex.printStackTrace();
//        ResponseBody responseBody = new ResponseBody(false,500, ex.getMessage(), null);
//        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
}
