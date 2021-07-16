package com.codingmart.api_mart.model;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String created_at = LocalDateTime.now().toString();

    public User() {
        super();
    }

    public User(String id, String name, String email, String password) {
        System.out.println("created_at = " + created_at);
        this.id = id;
        this.name = name.toLowerCase();
        this.email = email;
        this.password = password;
    }

    public User(String id, String name, String email, String password, String created_at) {
        this.id = id;
        this.name = name.toLowerCase();
        this.email = email;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.replace(" ", "").toLowerCase();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
