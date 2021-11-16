package com.codingmart.api_mart.service.impl;

import com.codingmart.api_mart.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

@Service
@Primary
public class VideoServiceImpl implements VideoService {
    private static final String FORMAT="classpath:videos/%s.mp4";

    @Autowired
    private ResourceLoader resourceLoader;


    @Override public Resource getVideo(String title){
        return resourceLoader.getResource(String.format(FORMAT,title)) ;
    }
}
