package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileTypeNotSupportException;

import java.io.File;
import java.util.*;

public class FileParser {

//    private static final String[] FILE_TYPES = {"csv", "xlsx", "xls"}; // No need
//    private static final Set<String> SUPPORTED_FILE_TYPES = new HashSet<>(Arrays.asList(FILE_TYPES));

    private static final Map<String, FileMapParser> mapper = new HashMap<>();
//    {
//        {
//            put("csv", new CsvMapParser());
//            put("xls", new XlsMapParser());
//            put("xlsx", new XlsxMapParser());
//        }
//    };

    static {
        mapper.put("csv", new CsvMapParser());
        mapper.put("xls", new XlsMapParser());
        mapper.put("xlsx", new XlsxMapParser());
    }

    public static List<Map<String, Object>> parseMap(File file) {
        String type = file.getName().split("[.]")[1];
        if(!mapper.containsKey(type)) throw new FileTypeNotSupportException();
        return mapper.get(type).parseMap(file);
    }

    public static File parseFile(List<Map<String, String>> records, String fileName) {
        String type = fileName.split("[.]")[1];
        if(!mapper.containsKey(type)) throw new FileTypeNotSupportException();
        return mapper.get(type).parseFile(records, fileName);
    }

    public static boolean supports(String fileType){
        return mapper.containsKey(fileType.toLowerCase());
    }

    public static String[] getSupportedFiles(){
        return mapper.keySet().toArray(new String[0]);
    }
}
