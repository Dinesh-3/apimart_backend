package com.codingmart.api_mart.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Verification {
    private String id;
    private String user_id;
    private boolean is_email_verified;
    private String created_at = LocalDateTime.now().toString();
    private String updated_at = LocalDateTime.now().toString();
    private String otp_created_at;
    private String otp;

    public Verification(String user_id) {
        this.user_id = user_id;
    }

    public Verification(String user_id, String otp_created_at, String otp) {
        this.user_id = user_id;
        this.otp_created_at = otp_created_at;
        this.otp = otp;
    }
}
