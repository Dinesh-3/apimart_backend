package com.codingmart.api_mart.controller;

import com.codingmart.api_mart.model.Table;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.service.CollectionService;
import com.codingmart.api_mart.utils.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codingmart.api_mart.utils.ControllerResponse.getResponseEntity;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/collection")
public class CollectionController {

    private final CollectionService service;

    public CollectionController(CollectionService service) {
        this.service = service;
    }

    @ModelAttribute("user")
    private User getUser(HttpServletRequest request)
    {
        return (User) request.getAttribute("user");
    }

    @GetMapping()
    public ResponseEntity<ResponseBody> collectionsByUser(@ModelAttribute("user") User user) {
        List<Table> getCollections = service.getAllCollectionByUser(user.getName());
        return getResponseEntity(getCollections);
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<ResponseBody> deleteCollectionByUser(@PathVariable String fileName, @ModelAttribute("user") User user) {
        String message = service.deleteCollectionByUser(fileName, user.getName());
        return getResponseEntity(message);
    }

    @GetMapping("/get/{user}/{fileName}")
    public ResponseEntity<ResponseBody> collectionByUser(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String,String> queryParams ) {
        List<HashMap<String, String>> collection = service.getCollectionByUser(user, fileName, queryParams);
        return getResponseEntity(collection);
    }

    @PostMapping("/{user}/{fileName}")
    public ResponseEntity<ResponseBody> insert(@PathVariable String user, @PathVariable String fileName, @RequestBody Map<String, String> requestBody) {
        Map<String, String> record = service.insertRecord(user, fileName, requestBody);
        return getResponseEntity(record);
    }

    @PutMapping("/{user}/{fileName}")
    public ResponseEntity<ResponseBody> update(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String, String> queryParams, @RequestBody Map<String, String> requestBody) {
        Map<String, String> record = service.updateRecord(user, fileName, queryParams, requestBody);
        return getResponseEntity(record);
    }

    @DeleteMapping("/{user}/{fileName}")
    public ResponseEntity<ResponseBody> delete(@PathVariable String user, @PathVariable String fileName, @RequestParam Map<String, String> queryParams) {
        boolean deleted = service.deleteRecord(user, fileName, queryParams);
        return getResponseEntity(deleted);
    }

    @PostMapping("/upload")
    public ResponseEntity<ResponseBody> upload(@RequestParam("File") MultipartFile file, @ModelAttribute("user") User user) {
        Table message = service.upload(file, user);
        return getResponseEntity(message);
    }

}
