package com.codingmart.api_mart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
public class VideoService {
    private static final String FORMAT="classpath:videos/%s.mp4";

    @Autowired
    private ResourceLoader resourceLoader;


    public Resource getVideo(String title){
        return resourceLoader.getResource(String.format(FORMAT,title)) ;
    }
}
