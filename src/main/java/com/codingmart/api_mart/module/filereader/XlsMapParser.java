package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileParseException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsMapParser extends XlParser implements FileMapParser {
    @Override
    public List<Map<String, Object>> parseMap(File file) throws IOException {
        return parseFromXlsx(new HSSFWorkbook(new FileInputStream(file)));
    }

    @Override
    public File parseFile(List<Map<String, String>> records, File file) throws IOException {
        return parseMapToFile(records, file, new HSSFWorkbook());
    }
}
