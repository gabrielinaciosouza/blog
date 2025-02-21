package com.gabriel.blog.infrastructure.services;

import com.gabriel.blog.application.qualifiers.RandomGenerator;
import com.gabriel.blog.application.services.IdGenerator;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 * Random UUID generator.
 *
 * <p>Created by Gabriel Inacio de Souza on February 9, 2025.</p>
 */
@ApplicationScoped
@RandomGenerator
public class RandomUuidGenerator implements IdGenerator {
  @Override
  public String generateId(final String domain) {
    if (domain == null || domain.isBlank()) {
      return UUID.randomUUID().toString();
    }
    return UUID.randomUUID() + "-" + domain;
  }
}
