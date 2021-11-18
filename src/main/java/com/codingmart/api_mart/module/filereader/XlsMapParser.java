package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileParseException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsMapParser extends XlParser implements FileMapParser {
    @Override
    public List<Map<String, Object>> parseMap(File file) {
        try {
            return parseFromXlsx(new HSSFWorkbook(new FileInputStream(file)));
        } catch (IOException e) {
            throw new FileParseException(e.getMessage(), e.getCause());
        }finally {
//            file.delete();
        }
    }

    @Override
    public File parseFile(List<Map<String, String>> records, String fileName) {

        File file = new File(Paths.get("").toAbsolutePath() + "/src/main/resources/downloads/" + fileName);
        try {
            file.createNewFile();
            parseMapToFile(records, file, new HSSFWorkbook());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
