package com.gabriel.blog.it;

import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Mock
@ApplicationScoped
public class GoogleCredentialsMockProducer {

	@Produces
	@Singleton
	@Default
	public Credentials googleCredential() {
		return NoCredentials.getInstance();
	}
}
