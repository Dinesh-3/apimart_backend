package com.codingmart.api_mart.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/")
public class SampleController {
    @GetMapping
    public String home(){
        return "home";
    }
}