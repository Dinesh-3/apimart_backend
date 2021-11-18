package com.codingmart.api_mart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor()
public class User {
    private String id;
    private String user_id = UUID.randomUUID().toString();
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @JsonIgnore
    private String password;
    private String created_at = LocalDateTime.now().toString();

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
        this.created_at = created_at;
    }

    public User(String id, String name, String email, String password, String created_at, String user_id) {
        this.id = id;
        this.name = name.toLowerCase();
        this.email = email;
        this.password = password;
        this.created_at = created_at;
        this.user_id = user_id;
    }

}
