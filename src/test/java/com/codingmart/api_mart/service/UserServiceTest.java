package com.codingmart.api_mart.service;

import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceTest {


    @Test
    void getAllUsers() {
        String developer = "Dinesh I";

        boolean equal = developer.equals("Dinesh I");

        assertThat(equal).isTrue();

    }

    @Test
    void getUserById() {
    }
}