package com.codingmart.api_mart.module.filereader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface FileMapParser {
    List<Map<String, Object>> parseMap(File file);
    File parseFile(List<Map<String, String>> records, String fileName);
}
