package com.codingmart.api_mart.ExceptionHandler;

import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseBody> handleException(Exception ex) {
        ResponseBody responseBody = new ResponseBody(false,500, ex.getMessage(), null);
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
