package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdTest {

	@Test
	void shouldCreateCorrectId() {
		final var thrown = assertThrows(DomainException.class, () -> new Id(null));
		assertEquals("Tried to create an id with a null value", thrown.getMessage());
		assertDoesNotThrow(() -> new Id("any"));
	}
}
