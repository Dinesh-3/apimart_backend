package com.codingmart.api_mart.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import java.nio.charset.StandardCharsets;

public class StatusCodeException extends HttpStatusCodeException {
    public StatusCodeException(HttpStatus statusCode, String message) {
        super(statusCode, statusCode.getReasonPhrase(), message.getBytes(), StandardCharsets.UTF_8);
    }
}
