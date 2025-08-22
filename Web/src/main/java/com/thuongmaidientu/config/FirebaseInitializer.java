package com.thuongmaidientu.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Configuration
public class FirebaseInitializer {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                // Load file từ classpath để không bị lỗi khi chạy JAR
                InputStream serviceAccount = new ClassPathResource("firebase/serviceAccountKey.json").getInputStream();

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized");
            }
        } catch (IOException e) {
            System.err.println("❌ Failed to initialize Firebase: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    @PreDestroy
    public void shutdown() {
        List<FirebaseApp> apps = FirebaseApp.getApps();
        if (apps != null && !apps.isEmpty()) {
            for (FirebaseApp app : apps) {
            	app.delete();
            }
            System.out.println("FirebaseApp shut down cleanly.");
        }
    }
}
