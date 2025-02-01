package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class TitleTest {

  @Test
  void shouldCreateCorrectTitle() {
    final var thrown = assertThrows(DomainException.class, () -> new Title(null));
    assertEquals("Tried to create a Title with a null value", thrown.getMessage());
    assertDoesNotThrow(() -> new Title("any"));
  }
}
