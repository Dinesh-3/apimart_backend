package com.codingmart.api_mart.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;

public class UserNotFoundException extends HttpClientErrorException {
    private static final HttpStatus status = HttpStatus.NOT_FOUND;

    public UserNotFoundException() {
        super(status, status.getReasonPhrase());
    }

    public UserNotFoundException(HttpStatus statusCode, String body) {
        super(statusCode, statusCode.getReasonPhrase(), body.getBytes(), StandardCharsets.UTF_8);
    }
}
