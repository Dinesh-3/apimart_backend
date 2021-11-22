package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileParseException;
import com.codingmart.api_mart.module.filereader.exception.FileTypeNotSupportException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

public class FileParser {

    private static final Map<String, FileMapParser> mapper = new HashMap<>();

    static {
        mapper.put("csv", new CsvMapParser());
        mapper.put("xls", new XlsMapParser());
        mapper.put("xlsx", new XlsxMapParser());
        mapper.put("json", new JsonMapParser());
    }

    public static List<Map<String, Object>> parseMap(File file) {
        String type = file.getName().split("[.]")[1];
        if(!mapper.containsKey(type)) throw new FileTypeNotSupportException();
        try {
            return mapper.get(type).parseMap(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileParseException("Unable to parse " + file.getName(), e);
        }
    }

    public static File parseFile(List<Map<String, String>> records, String fileName) {
        String type = fileName.split("[.]")[1];
        File file = new File(Paths.get("").toAbsolutePath() + "/src/main/resources/downloads/" + fileName);
        if(!mapper.containsKey(type)) throw new FileTypeNotSupportException();
        try {
            file.createNewFile();
            return mapper.get(type).parseFile(records, file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileParseException("Unable to parse " + file.getName(), e);
        }
    }

    public static boolean supports(String fileType){
        return mapper.containsKey(fileType.toLowerCase());
    }

    public static String[] getSupportedFiles(){
        return mapper.keySet().toArray(new String[0]);
    }
}
