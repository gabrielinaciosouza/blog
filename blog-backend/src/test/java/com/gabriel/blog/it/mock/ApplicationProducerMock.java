package com.gabriel.blog.it.mock;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.auth.Credentials;
import com.google.cloud.NoCredentials;
import io.quarkus.test.Mock;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;

@Mock
@ApplicationScoped
public class ApplicationProducerMock {

	@Produces
	@Singleton
	@Default
	public Credentials googleCredential() {
		return NoCredentials.getInstance();
	}

	@Produces
	@Singleton
	@Default
	public CredentialsProvider credentialsProvider() {
		return NoCredentialsProvider.create();
	}
}
