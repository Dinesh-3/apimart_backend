package com.codingmart.api_mart.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.util.SortedMap;

public class UserNotFoundException extends HttpClientErrorException {
    private static HttpStatus status = HttpStatus.NOT_FOUND;

    public UserNotFoundException() {
        super(status, status.getReasonPhrase());
    }

    public UserNotFoundException(HttpStatus statusCode, String body) {
        super(statusCode, statusCode.getReasonPhrase(), body.getBytes(), Charset.forName("UTF-8"));
//        SortedMap<String, Charset> map = Charset.availableCharsets();
//        System.out.println("map = " + map);
    }
}
