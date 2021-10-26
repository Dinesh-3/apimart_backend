package com.codingmart.api_mart.ExceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.charset.Charset;

public class ClientErrorException extends HttpClientErrorException {
    private static HttpStatus status = HttpStatus.BAD_REQUEST;

    public ClientErrorException() {
        super(status, status.getReasonPhrase());
    }

    public ClientErrorException(HttpStatus statusCode, String body) {
        super(statusCode, statusCode.getReasonPhrase(), body.getBytes(), Charset.forName("UTF-8"));
    }
}
