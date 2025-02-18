package com.gabriel.blog.presentation.config;

import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Mock
@ApplicationScoped
@IfBuildProfile(anyOf = {"dev", "test"})
public class GoogleCredentialsMockProducer {

  @Produces
  @Singleton
  @Default
  public Credentials googleCredential() {
    return NoCredentials.getInstance();
  }
}
