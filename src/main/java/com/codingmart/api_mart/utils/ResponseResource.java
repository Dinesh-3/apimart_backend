package com.codingmart.api_mart.utils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Component
public class ResponseResource {
    public ResponseEntity<Resource> getResourceResponseEntity(File file, String fileName) {

        InputStreamResource resource = null;
        try {
            resource = new InputStreamResource(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
//            file.delete();
        }
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.MULTIPART_RELATED)
                .body(resource);
    }

    public void writeStream(File file, HttpServletResponse response) {
        response.setHeader("Content-disposition", "attachment; filename=" + file.getName());

        try(
            OutputStream out = response.getOutputStream();
            FileInputStream in = new FileInputStream(file);
        ) {
            IOUtils.copy(in,out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            file.delete();
        }
    }
}
