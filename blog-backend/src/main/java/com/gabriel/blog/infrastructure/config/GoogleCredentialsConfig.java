package com.gabriel.blog.infrastructure.config;

import com.gabriel.blog.infrastructure.models.PostModel;
import com.gabriel.blog.presentation.exceptions.ErrorResponse;
import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import com.google.cloud.logging.Severity;
import com.google.common.util.concurrent.AbstractFuture;
import com.google.firebase.auth.internal.GetAccountInfoResponse;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.annotations.RegisterForReflection;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@ApplicationScoped
@RegisterForReflection(targets = { AbstractFuture.class, Severity.class,
    GetAccountInfoResponse.class, PostModel.class, ErrorResponse.class })
public class GoogleCredentialsConfig {

  @Mock
  @Produces
  @Singleton
  @IfBuildProfile(anyOf = { "test", "local" })
  public Credentials localCredendials() {
    return NoCredentials.getInstance();
  }
}
