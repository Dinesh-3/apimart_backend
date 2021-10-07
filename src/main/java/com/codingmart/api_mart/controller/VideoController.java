package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/video")
public class VideoController {
    @Autowired
    private VideoService service;


    @GetMapping(value = "{title}", produces = "video/mp4")
    public Resource getVideos(@PathVariable String title, @RequestHeader("Range") String range) {
        System.out.println("range in bytes() : " + range);
        return service.getVideo(title);
    }
}
