package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileParseException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class CsvMapParser implements FileMapParser {
    @Override
    public List<Map<String, Object>> parseMap(File file) {
        try {
            Reader reader = new java.io.FileReader(file.getAbsolutePath());
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(reader);
            List<String> headers = getHeaders(records);

            List<Map<String, Object>> listOfRecords = new ArrayList<>();

            for (CSVRecord record : records) {
                Iterator<String> recordIterable = record.iterator();
                Map<String, Object> rowMap = new HashMap<>();
                int index = 0;
                while (recordIterable.hasNext()) {
                    String value = recordIterable.next();
                    rowMap.put(headers.get(index), value);
                    ++ index;
                }
                listOfRecords.add(rowMap);
            }
            return listOfRecords;
        } catch (IOException e) {
            throw new FileParseException(e.getMessage(), e.getCause());
        } finally {
            file.delete();
        }

    }

    @Override
    public File parseFile(List<Map<String, String>> records, String fileName) {
        File file = new File(Paths.get("").toAbsolutePath() + "/src/main/resources/downloads/" + fileName);
        try(
                FileWriter write = new FileWriter(file);
        ) {
            file.createNewFile();

            String header = records.get(0).keySet().stream().collect(Collectors.joining(",","","\n"));
            write.write(header);

            for(Map<String, String> record: records){
                String row = record.values().stream().collect(Collectors.joining(",", "", "\n"));
                write.write(row);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private List<String> getHeaders(Iterable<CSVRecord> records) {
        Iterator<String> csvHeader = records.iterator().next().iterator();
        List<String> headers = new ArrayList<>();
        while (csvHeader.hasNext()) {
            String value = csvHeader.next();
            headers.add(value);
        }
        return headers;
    }
}
