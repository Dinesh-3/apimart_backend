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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    public ResponseBody getUserById(long id) {

        return new ResponseBody("","");
    }

    public ResponseBody signup(User user) {
//        User existData = userRepository.findBy(user.getEmail());
//        if (existData != null) {
//            return new ResponseEntity<>(returnJsonString(false, "Email already exist please try with new mail"),
//                    HttpStatus.FORBIDDEN);
//        } else {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
        return new ResponseBody("Signup Success", null);
//    }
    }

    public ResponseBody login(User user) {
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
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

    private ResponseBody getResponseBody(String message, Object data){
        return new ResponseBody(message, data);
    }

    @Bean
    private HashMap<String, Object> getHashMap() {
        return new HashMap<>();
    }
}
