package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.service.UserService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/test")
    public ResponseBody test() {
        return userService.test();
    }

    @GetMapping("/all")
    public ResponseBody getAllUser(){
        return userService.getAllUser();
    }

    @GetMapping("/get")
    public ResponseBody getUser(HttpServletRequest request){
        return userService.getUserById(request);
    }

    @PostMapping("/signup")
    public ResponseBody createUser(@RequestBody User user){
        return userService.signup(user);
    }

    @PostMapping("/login")
    public ResponseBody loginUser(@RequestBody User user){
        return userService.login(user);
    }
//
//    @PutMapping("/{id}")
//    public ResponseBody updateUser(@PathVariable long id, @RequestBody User user){
//        return userService.updateUser(user);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseBody deleteUser(@PathVariable long id){
//        return userService.deleteUser(id);
//    }
}
