package com.codingmart.api_mart.utils;

public class ResponseBody {
    private boolean status = true;
    private int status_code = 200;
    private String message = "Success";
    private Object data;

    public ResponseBody() {
    }

    public ResponseBody(Object data) {
        this.data = data;
    }

    public ResponseBody(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public ResponseBody(boolean status, int status_code, String message) {
        this.status = status;
        this.status_code = status_code;
        this.message = message;
    }

    public ResponseBody(boolean status, int status_code, String message, Object data) {
        this.status = status;
        this.status_code = status_code;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public int getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
