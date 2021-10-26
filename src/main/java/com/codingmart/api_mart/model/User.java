package com.codingmart.api_mart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;


public class User {
    private String id;
    private String user_id = UUID.randomUUID().toString();
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String created_at = LocalDateTime.now().toString();

    public User() {
        super();
    }

    public User(String id, String name, String email, String password) {
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

    public User(String id, String name, String email, String password, String created_at, String user_id) {
        this.id = id;
        this.name = name.toLowerCase();
        this.email = email;
        this.password = password;
        this.user_id = user_id;
    }

    //    @JsonProperty("user_id")
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

    @JsonIgnore
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
