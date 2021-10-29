package com.codingmart.api_mart.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.StandardCharsets;

public class ClientErrorException extends HttpClientErrorException {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    public ClientErrorException() {
        super(status, status.getReasonPhrase());
    }

    public ClientErrorException(HttpStatus statusCode, String message) {
        super(statusCode, statusCode.getReasonPhrase(), message.getBytes(), StandardCharsets.UTF_8);
    }
    public ClientErrorException(int statusCode, String message) {
        super(getStatus(statusCode), getStatus(statusCode).getReasonPhrase(), message.getBytes(), StandardCharsets.UTF_8);
    }

    private static HttpStatus getStatus(int statusCode) {
        return HttpStatus.valueOf(statusCode);
    }
}
