package com.codingmart.api_mart.model;

import java.time.LocalDateTime;

public class Verification {
    private String id;
    private String user_id;
    private boolean is_email_verified;
    private String created_at = LocalDateTime.now().toString();
    private String updated_at = LocalDateTime.now().toString();
    private String otp_created_at;
    private String otp;

    public Verification() {
    }

    public Verification(String user_id) {
        this.user_id = user_id;
    }

    public Verification(String user_id, String otp_created_at, String otp) {
        this.user_id = user_id;
        this.otp_created_at = otp_created_at;
        this.otp = otp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isIs_email_verified() {
        return is_email_verified;
    }

    public void setIs_email_verified(boolean is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getOtp_created_at() {
        return otp_created_at;
    }

    public void setOtp_created_at(String otp_created_at) {
        this.otp_created_at = otp_created_at;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
