package com.codingmart.api_mart.utils;

import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;

import java.util.Base64;
import java.util.Map;

public class GetTokenPayload {
    public static String getPayload(String token, String key) {
        String[] chunks = token.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[1]));
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, ?> tokenData = parser.parseMap(payload);
        String value = tokenData.get(key).toString();
        return value;
    }
}
