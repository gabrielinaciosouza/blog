package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

class NameTest {

  @Test
  void shouldCreateCorrectName() {
    final var thrown = assertThrows(DomainException.class, () -> new Name(null));
    assertEquals("Tried to create an Name with a null value", thrown.getMessage());
    assertDoesNotThrow(() -> new Name("any"));
  }

}