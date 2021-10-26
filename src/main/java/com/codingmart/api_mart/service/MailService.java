package com.codingmart.api_mart.service;

import com.codingmart.api_mart.DTO.VerifyMail;
import com.codingmart.api_mart.model.User;
import com.codingmart.api_mart.model.Verification;
import org.apache.xpath.operations.Bool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Service
public class MailService {
    private final Logger log = LoggerFactory.getLogger(MailService.class);

    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String SENDER_EMAIL;
    private final String SENDER_NAME = "OTT ALERT";

    public void sendForVerify(User user, Verification verify) {
        CompletableFuture
                .supplyAsync(() -> {
                    String html = parseHTML(user, verify);
                    Map<String, String> info = new HashMap<>();
                    info.put("toAddress", user.getEmail());
                    info.put("subject", "Verify Email");
                    info.put("content", html);
                    return info;
                })
                .thenApplyAsync(sendMail())
                .thenAcceptAsync(result -> log.info(String.format("Mail Sent %s Status: %b", user.getEmail(), result)))
                .exceptionally(error -> {
                    error.printStackTrace();
                    return null;
                })
        ;
    }

    private String parseHTML(User user, Verification verify) {
        final Context ctx = new Context();
        String link = String.format("http://localhost:5000/api/v1/user/verify/email?otp=%s&id=%s",verify.getOtp(), verify.getUser_id());
        ctx.setVariable("verify", new VerifyMail(link, user.getName()));
        return templateEngine.process("verification", ctx);
    }

    private Function<Map<String,String>, Boolean> sendMail()  {
        return (info) -> {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            try {
                helper.setFrom(SENDER_EMAIL, SENDER_NAME);
                helper.setTo(info.get("toAddress"));
                helper.setSubject(info.get("subject"));
                helper.setText(info.get("content"), true);
                mailSender.send(message);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        };
    }
}
