package com.codingmart.api_mart.module.filereader;

import com.codingmart.api_mart.module.filereader.exception.FileParseException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public abstract class XlParser {
    List<Map<String, Object>> parseFromXlsx(Workbook workbook) throws IOException {
        Sheet sheet = workbook.getSheetAt(0);
        int lastRowNum = sheet.getLastRowNum();
        Row header = sheet.getRow(0);
        List<Map<String, Object>> records = new ArrayList<>();
        for (int rowIndex = 1; rowIndex <= lastRowNum; rowIndex++) {
            Row currentRow = sheet.getRow(rowIndex);
            Iterator<Cell> cellIterator = currentRow.cellIterator();
            Map<String, Object> rowMap = new HashMap<>();
            while (cellIterator.hasNext()){
                Cell cell = cellIterator.next();
                int cellIndex = cell.getColumnIndex();

                String key = getCellValue(header.getCell(cellIndex)).toString();
                Object value = getCellValue(cell);

                rowMap.put(key,value);
            }
            records.add(rowMap);
        }
        workbook.close();
        return records;
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                } else {
                    return cell.getNumericCellValue();
                }
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    File parseMapToFile(List<Map<String, String>> records, File file, Workbook workbook) {
        Sheet sheet = workbook.createSheet(file.getName().split("[.]")[0]);
        sheet.setSelected(true);

        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 12);
        font.setColor((short) 0);

        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);

        for (int i = 0; i < 1; i++) {

            Row row = sheet.createRow(i);
            int column = 0;
            for (String key: records.get(i).keySet()) {
                Cell cell = row.createCell(column);
                cell.setCellValue(key);
                cell.setCellStyle(cellStyle);
                column++;
            }
//            row.setRowStyle(cellStyle);
        }

        for (int i = 0; i < records.size(); i++) {
            Row row = sheet.createRow(i+1);
            int column = 0;
            for (String value: records.get(i).values()) {
                Cell cell = row.createCell(column);
                cell.setCellValue(value);
                column++;
            }
        }
        try {
            OutputStream fileOut = new FileOutputStream(file);
            workbook.write(fileOut);
        } catch (IOException e) {
            throw new FileParseException(e.getMessage(), e);
        }
        return file;
    }
}
