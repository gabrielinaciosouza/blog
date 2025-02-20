package com.gabriel.blog.infrastructure.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gabriel.blog.application.services.IdGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RandomUuidGeneratorTest {

  private IdGenerator idGenerator;

  @BeforeEach
  void setUp() {
    idGenerator = new RandomUuidGenerator();
  }

  @Test
  void generateIdWithValidDomainReturnsUuidWithDomain() {
    final var domain = "example.com";
    final var generatedId = idGenerator.generateId(domain);

    assertNotNull(generatedId);
    assertTrue(generatedId.contains(domain));
    assertEquals(36 + 1 + domain.length(), generatedId.length());
  }

  @Test
  void generateIdWithEmptyDomainReturnsUuidWithDash() {
    final var domain = "";
    final var generatedId = idGenerator.generateId(domain);

    assertNotNull(generatedId);
    assertTrue(generatedId.endsWith("-"));
    assertEquals(36 + 1, generatedId.length());
  }

  @Test
  void generateIdWithoutDomainReturnsUuid() {
    final var generatedId = idGenerator.generateId(null);

    assertNotNull(generatedId);
    assertEquals(36, generatedId.length());
  }
}