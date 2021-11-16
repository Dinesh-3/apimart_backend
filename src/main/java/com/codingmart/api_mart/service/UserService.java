package com.codingmart.api_mart.service;

import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.utils.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(HttpServletRequest request);

    ResponseBody signup(User user);

    ResponseBody login(User user);

    boolean verifyEmail(String otp, String id);

    void sendVerifyEmail(String id);
}
