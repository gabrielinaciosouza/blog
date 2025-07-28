package com.gabriel.blog.infrastructure.config;

import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
public class GoogleCredentialsConfig {

  @Mock
  @Produces
  @Singleton
  @IfBuildProfile(anyOf = { "test", "local" })
  public Credentials localCredendials() {
    return NoCredentials.getInstance();
  }
}
