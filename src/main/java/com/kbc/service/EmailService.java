package com.kbc.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendUserCreationEmail(String email, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your Account Has Been Created");
        message.setText("Your account has been created. Your temporary password is: " + password + 
                       "\nPlease change it after your first login.\n Follow this link to login: " +
                       "\nhttps://kingdom-believers-church.vercel.app/login");
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