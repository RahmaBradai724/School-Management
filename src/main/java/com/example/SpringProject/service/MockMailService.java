package com.example.SpringProject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MockMailService implements EmailService {

    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("--------------------------------------------------");
        log.info("MOCK EMAIL SENT TO: {}", to);
        log.info("SUBJECT: {}", subject);
        log.info("BODY:");
        log.info(body);
        log.info("--------------------------------------------------");
    }
}
