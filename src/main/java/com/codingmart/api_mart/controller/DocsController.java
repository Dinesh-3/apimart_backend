package com.codingmart.api_mart.controller;


import com.codingmart.api_mart.service.DocsService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/docs")
public class DocsController {

    private final DocsService docsService;

    public DocsController(DocsService docsService) {
        this.docsService = docsService;
    }

    @GetMapping("/open-api")
    public ResponseEntity<Resource> getOpenApiDocument(HttpServletRequest request) {
        return docsService.getOpenApiFile(request);
    }

    @GetMapping("/postman-collection")
    public ResponseEntity<Resource> getPostmanCollection() {
        return docsService.getPostmanCollection();
    }

}
