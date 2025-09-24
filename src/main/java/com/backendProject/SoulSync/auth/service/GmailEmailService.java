package com.backendProject.SoulSync.auth.service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// Jakarta imports
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

@Service
public class GmailEmailService {

    private static final Logger logger = LoggerFactory.getLogger(GmailEmailService.class);

    @Autowired(required = false)
    private Gmail gmailService;

    @Value("${gmail.user-email:ayushksb11@gmail.com}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp) {
        if (gmailService == null) {
            logger.error("Gmail service is not initialized. Check your Gmail API configuration.");
            throw new RuntimeException("Gmail service not available");
        }

        try {
            logger.info("Sending OTP email to: {}", toEmail);
            
            // USE YOUR EXACT ORIGINAL CONTENT
            String subject = "OTP Verification";
            String body = "Your OTP for verification in SoulSync app : " + otp;
            
            MimeMessage email = createEmail(toEmail, fromEmail, subject, body);
            Message message = createMessageWithEmail(email);
            
            gmailService.users().messages().send("me", message).execute();
            
            logger.info("OTP email sent successfully to: {}", toEmail);
            
        } catch (Exception e) {
            logger.error("Failed to send OTP email to: {}, Error: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage(), e);
        }
    }

    private MimeMessage createEmail(String to, String from, String subject, String bodyText) 
            throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText, "utf-8");
        return email;
    }

    private Message createMessageWithEmail(MimeMessage emailContent) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        emailContent.writeTo(buffer);
        byte[] bytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(bytes);
        
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}