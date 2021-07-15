package com.codingmart.api_mart.service;

import com.codingmart.api_mart.repository.CollectionRepository;
import com.codingmart.api_mart.repository.UserRepository;
import com.codingmart.api_mart.utils.ResponseBody;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
public class CollectionService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    public ResponseBody uploadCollection(String fileName, Object data) {
        System.out.println("fileName = " + fileName);
        System.out.println("data = " + data);
        try (
                Reader reader = new FileReader("src/main/resources/uploads/sample.csv");
                BufferedReader br = new BufferedReader(reader);
        ) {
            String[] headers = br.readLine().split(",");
            List<Map<String, String>> records =
                    br.lines().map(s -> s.split(","))
                            .map(t -> IntStream.range(0, t.length)
                                    .boxed()
                                    .collect(toMap(i -> headers[i], i -> t[i])))
                            .collect(toList());
            String collectionName = "employee";
            boolean isSaved = collectionRepository.saveCollection(collectionName, records);
            if(isSaved) return getResponseBody("Success Data Saved", null);
            System.out.println(headers);
            System.out.println(records);
            return getResponseBody(false, 400, "Unable to parse csv", null);
        } catch (Exception e) {
            e.printStackTrace();
            return getResponseBody(false, 400, "Error in csv file", null);
        }

    }

    private ResponseBody getResponseBody(String message, Object data){
        return new ResponseBody(message, data);
    }

    private ResponseBody getResponseBody(boolean status, int status_code, String message, Object data){
        return new ResponseBody(status, status_code, message, data);
    }
}
