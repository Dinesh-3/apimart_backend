package com.codingmart.api_mart.service;

import org.springframework.core.io.Resource;

public interface VideoService {
    Resource getVideo(String title);
}
