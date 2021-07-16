package com.codingmart.api_mart.service;

import com.codingmart.api_mart.config.jwt_configure.JwtTokenProvider;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.repository.UserRepository;
import com.codingmart.api_mart.utils.GetTokenPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.codingmart.api_mart.utils.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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

    public ResponseBody getUserById(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String name = GetTokenPayload.getPayload(token, "sub");
        User user = userRepository.findByName(name);
        return getResponseBody("Success", user);
    }

    public ResponseBody signup(User user) {
//        boolean isEmailOrNameExists = userRepository.isEmailOrNameExists(user.getName(), user.getEmail());
//
//        if( isEmailOrNameExists ) return new ResponseBody(false, 400,  "Email or Name Already Exist", null);
//
//        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
//
//        boolean savedUser = userRepository.save(user);
        return new ResponseBody("Success", null);
    }


    public ResponseBody login(User user) {
        User savedUser = userRepository.findByEmail(user.getEmail());
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(savedUser.getName(), user.getPassword()));
        if(savedUser == null) return getResponseBody(false, 400,"User Doesn't Exist", null);

        HashMap body = getHashMap();
        body.put("token", tokenProvider.createToken(savedUser.getName(), savedUser.getEmail(), savedUser.getId()));
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
