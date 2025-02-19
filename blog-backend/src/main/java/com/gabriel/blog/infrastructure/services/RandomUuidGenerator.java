package com.gabriel.blog.infrastructure.services;

import com.gabriel.blog.application.qualifiers.RandomGenerator;
import com.gabriel.blog.application.services.IdGenerator;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
@RandomGenerator
public class RandomUuidGenerator implements IdGenerator {
	@Override
	public String generateId(String domain) {
		return UUID.randomUUID() + "-" + domain;
	}
}
