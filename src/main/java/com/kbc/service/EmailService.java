package com.kbc.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
    }

    public void sendUserCreationEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Welcome to Kingdom Believers Church");
        message.setText("Your account has been created. Your temporary password is: " + password + 
                       "\nPlease change it after your first login.\nFollow this link to login: " +
                       "\n\nhttps://kingdom-believers-church.vercel.app/login"
                       + "\n\n\nThis is authomatically generated Email don't reply to it please.");
        mailSender.send(message);
    }

    public void sendPasswordResetEmail(String email, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Password Reset Request");
        message.setText("To reset your password, click the link below:\n" + resetLink + 
                       "\nThis link will expire in 10 hours.");
        mailSender.send(message);
    }
}