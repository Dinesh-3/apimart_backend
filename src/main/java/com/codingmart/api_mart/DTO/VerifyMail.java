package com.codingmart.api_mart.DTO;

public class VerifyMail {
    public String link;
    public String userName;

    public VerifyMail(String link, String userName) {
        this.link = link;
        this.userName = userName;
    }
}
