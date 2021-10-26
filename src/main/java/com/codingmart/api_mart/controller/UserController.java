package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.ExceptionHandler.ClientErrorException;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.service.UserService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

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

    @GetMapping(value = "/verify/email", params = {"otp", "id"})
    public RedirectView verifyEmail(@RequestParam("otp") String otp, @RequestParam("id") String id){
        boolean status = userService.verifyEmail(otp, id);
        if(status) return new RedirectView("http://localhost:3000/login");
        throw new ClientErrorException(HttpStatus.FORBIDDEN, "Invalid Link Try Again");
    }

    @GetMapping("/verify/send_mail/{id}")
    public ResponseEntity<ResponseBody> sendVerifyMail(@PathVariable String id){
        userService.sendVerifyEmail(id);
        return new ResponseEntity<>(new ResponseBody("Verification mail Sent"), HttpStatus.OK);
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
