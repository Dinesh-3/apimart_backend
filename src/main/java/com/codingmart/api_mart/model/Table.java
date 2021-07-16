package com.codingmart.api_mart.model;

import java.time.LocalDateTime;

public class Table {

    private String id;
    private String user;
    private String fileName;
    private String collection;
    private String created_at = LocalDateTime.now().toString();

    public Table() {}

    public Table(String id, String user, String fileName, String collection, String created_at) {
        this.id = id;
        this.user = user;
        this.user = user;
        this.fileName = fileName;
        this.collection = collection;
        this.created_at = created_at;
    }

    public Table(String user, String fileName, String collection) {
        this.user = user;
        this.fileName = fileName;
        this.collection = collection;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
