package com.backendProject.SoulSync.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmailConfig {

    private static final Logger logger = LoggerFactory.getLogger(GmailConfig.class);

    @Value("${gmail.client-id:}")
    private String clientId;

    @Value("${gmail.client-secret:}")
    private String clientSecret;

    @Value("${gmail.refresh-token:}")
    private String refreshToken;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    @Bean
    public Gmail gmailService() {
        try {
            // Check if all required properties are available
            if (clientId.isEmpty() || clientSecret.isEmpty() || refreshToken.isEmpty()) {
                logger.warn("Gmail API credentials not configured. Email sending will not work.");
                return null;
            }

            NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            GoogleCredential credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(JSON_FACTORY)
                    .setClientSecrets(clientId, clientSecret)
                    .build()
                    .setRefreshToken(refreshToken);

            Gmail gmailService = new Gmail.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName("SoulSync")
                    .build();

            logger.info("Gmail service initialized successfully");
            return gmailService;

        } catch (Exception e) {
            logger.error("Failed to initialize Gmail service: {}", e.getMessage());
            return null;
        }
    }
}