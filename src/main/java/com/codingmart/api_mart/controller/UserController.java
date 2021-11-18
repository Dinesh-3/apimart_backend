package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.ExceptionHandler.ClientErrorException;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.service.UserService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.codingmart.api_mart.utils.ControllerResponse.getResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;
    @Autowired
    ApplicationEventPublisher publisher;

    public UserController(@Qualifier("userServiceImpl") UserService userService) { // Default is className in camel case
        this.userService = userService;
    }

    @GetMapping("/all")
    public ResponseEntity<ResponseBody> getAll(){
        List<User> users = userService.getAllUsers();
        AvailabilityChangeEvent.publish(publisher, this, LivenessState.BROKEN);
        return getResponseEntity(users);
    }

    @GetMapping("/get")
    public ResponseEntity<ResponseBody> getUser(HttpServletRequest request){
        User user = userService.getUserById(request);
        return getResponseEntity(user);
    }

    @PostMapping("/signup")
    public ResponseBody create(@RequestBody User user){
        return userService.signup(user);
    }

    @PostMapping("/login")
    public ResponseBody login(@RequestBody User user){
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
}
