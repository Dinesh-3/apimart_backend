package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonMapParser implements FileMapParser{
    private final Gson gson = new Gson();
    @Override
    public List<Map<String, Object>> parseMap(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> parsed = mapper.readValue(file, new TypeReference<List<Map<String, Object>>>(){});
        return parsed;
    }

    @Override
    public File parseFile(List<Map<String, String>> records, File file) throws IOException {
        String json = gson.toJson(records);
        try (
            FileWriter fileWriter = new FileWriter(file);
        ) {
            fileWriter.write(json);
            return file;
        }
    }
}
