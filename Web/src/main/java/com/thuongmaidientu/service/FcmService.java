package com.thuongmaidientu.service;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;

@Service
public class FcmService {

    private static final String FCM_ENDPOINT = "https://fcm.googleapis.com/v1/projects/my-application-b6114/messages:send";
    private static final String SCOPE = "https://www.googleapis.com/auth/firebase.messaging";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String sendNotification(String targetToken, String title, String body, Map<String, Object> data) {
        try {
            // Load credentials
            GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new FileInputStream("D:/Code_java/Quan-li-dien-thoai/my-application-b6114-firebase-adminsdk-fbsvc-5716ea1c8b.json"))
                .createScoped(Collections.singletonList(SCOPE));
            googleCredentials.refreshIfExpired();
            String accessToken = googleCredentials.getAccessToken().getTokenValue();

            // Build payload
         // Convert data to Map<String, String>
            Map<String, String> stringData = new HashMap<>();
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                stringData.put(entry.getKey(), String.valueOf(entry.getValue()));
            }

            // Build payload
            Map<String, Object> message = Map.of(
                "message", Map.of(
                    "token", targetToken,
                    "notification", Map.of(
                        "title", title,
                        "body", body
                    ),
                    "data", stringData
                )
            );


            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(
                objectMapper.writeValueAsString(message), headers
            );

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(FCM_ENDPOINT, request, String.class);
            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "Lỗi gửi FCM V1: " + e.getMessage();
        }
    }
}

