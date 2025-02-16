package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class CreationDateTest {

  @Test
  void shouldCreateCorrectContent() {
    final var thrown = assertThrows(DomainException.class, () -> new CreationDate(null));
    assertEquals("Tried to create a CreationDate with a null value", thrown.getMessage());
    assertDoesNotThrow(() -> new CreationDate(Instant.now()));
  }

  @Test
  void shouldCreateCorrectCreationDateNow() {
    assertDoesNotThrow(() -> new CreationDate(Instant.now()));
  }
}
