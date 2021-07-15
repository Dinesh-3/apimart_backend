package com.codingmart.api_mart.service;

import com.codingmart.api_mart.config.jwt_configure.JwtTokenProvider;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.codingmart.api_mart.utils.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public ResponseBody getAllUser() {
        List<User> users = userRepository.getAll();
        ResponseBody responseBody = getResponseBody("Success", users);
        return responseBody;
    }

    public ResponseBody getUserById(long id) {

        return new ResponseBody("","");
    }

    public ResponseBody signup(User user) {
        boolean isEmailOrNameExists = userRepository.isEmailOrNameExists(user.getName(), user.getEmail());
//        boolean isEmailExist = userRepository.findByEmailExists(user.getEmail());
//        boolean isNameExist = userRepository.findByNameExists(user.getName());
//
        if( isEmailOrNameExists ) return new ResponseBody(false, (short) 400,  "Email or Name Already Exist", null);

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        boolean savedUser = userRepository.save(user);
        return new ResponseBody("Success", savedUser);
    }


    public ResponseBody login(User user) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        if(userRepository.findByEmail(user.getEmail()) == null) return getResponseBody(false, 400,"User Doesn't Exist", null);
        User savedUser = userRepository.findByEmail(user.getEmail());
        HashMap body = getHashMap();
        body.put("token", tokenProvider.createToken(user.getName(), user.getEmail()));
        body.put("user", savedUser);
        ResponseBody responseBody = getResponseBody("Login Success", body);
        return responseBody;
    }

    public ResponseBody updateUser(User user) {
        return new ResponseBody("","");
    }

    public ResponseBody deleteUser(long id) {
        return new ResponseBody("","");
    }

    public ResponseBody test() {
        boolean result = userRepository.isEmailOrNameExists("Test", "asasdfk@gmail.com");
        return getResponseBody("Success", result);
    }

    private ResponseBody getResponseBody(String message, Object data){
        return new ResponseBody(message, data);
    }

    private ResponseBody getResponseBody(boolean status, int status_code, String message, Object data){
        return new ResponseBody(status, status_code, message, data);
    }

    @Bean
    private HashMap<String, Object> getHashMap() {
        return new HashMap<>();
    }

}
