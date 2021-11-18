package com.codingmart.api_mart.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Table {

    private String id;
    private String user;
    private String fileName;
    private String collection;
    private String created_at = LocalDateTime.now().toString();

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
}
