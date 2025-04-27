package com.gabriel.blog.infrastructure.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import java.io.IOException;
import org.jboss.logging.Logger;

@Singleton
public class FirebaseConfig {

  private static final Logger LOG = Logger.getLogger(FirebaseConfig.class);

  @PostConstruct
  public void initializeFirebase() {
    try {
      final var options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.getApplicationDefault())
          .build();

      FirebaseApp.initializeApp(options);
      LOG.info("Firebase initialized successfully");
    } catch (IOException e) {
      LOG.error("Failed to initialize Firebase", e);
      throw new IllegalStateException("Failed to initialize Firebase", e);
    }
  }
}