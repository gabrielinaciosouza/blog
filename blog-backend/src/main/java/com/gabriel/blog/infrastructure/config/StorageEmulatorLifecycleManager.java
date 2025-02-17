package com.gabriel.blog.infrastructure.config;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import io.quarkus.arc.profile.IfBuildProfile;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * Lifecycle manager for the Fake GCS Server.
 */
@ApplicationScoped
@IfBuildProfile(anyOf = {"dev", "test"})
public class StorageEmulatorLifecycleManager {

  private static final int SERVICE_PORT = 4443;
  private GenericContainer<?> container;

  /**
   * Produces a {@link Credentials} instance.
   *
   * @return a {@link Credentials} instance
   */
  @Produces
  @Singleton
  @Default
  public Credentials googleCredential() {
    return NoCredentials.getInstance();
  }

  /**
   * Produces a {@link CredentialsProvider} instance.
   *
   * @return a {@link CredentialsProvider} instance
   */
  @Produces
  @Singleton
  @Default
  public CredentialsProvider credentialsProvider() {
    return NoCredentialsProvider.create();
  }

  void onStart(final @Observes StartupEvent ev) {
    container = new GenericContainer<>(DockerImageName.parse("fsouza/fake-gcs-server:latest"))
        .withCommand("-scheme", "http")
        .waitingFor(Wait.forHttp("/storage/v1/b").forStatusCode(200))
        .withReuse(true)
        .withExposedPorts(SERVICE_PORT);

    container.start();

    final var mappedPort = container.getMappedPort(SERVICE_PORT);
    final var newUrl = "http://" + container.getHost() + ":" + mappedPort;

    System.setProperty("quarkus.google.cloud.storage.host-override", newUrl);

    System.out.println("🚀 Fake GCS Server rodando em: " + newUrl);
  }

  void onStop(final @Observes ShutdownEvent ev) {
    if (container != null) {
      container.stop();
      System.out.println("🛑 Fake GCS Server stopping.");
    }
  }
}
