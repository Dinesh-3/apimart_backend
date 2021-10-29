package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.service.VideoService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/video")
public class VideoController {
    private final VideoService service;

    public VideoController(VideoService service) {
        this.service = service;
    }


    @GetMapping(value = "{title}", produces = "video/mp4")
    public Resource getVideos(@PathVariable String title) {
        return service.getVideo(title);
    }
}
