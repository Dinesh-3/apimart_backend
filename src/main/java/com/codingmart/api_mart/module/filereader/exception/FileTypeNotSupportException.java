package com.codingmart.api_mart.module.filereader.exception;

public class FileTypeNotSupportException extends RuntimeException{

    private static String message = "File Type Not Supported";

    public FileTypeNotSupportException() {
        super(message);
    }

    public FileTypeNotSupportException(String message) {
        super(message);
    }

    public FileTypeNotSupportException(String message, Throwable cause) {
        super(message, cause);
    }
}
