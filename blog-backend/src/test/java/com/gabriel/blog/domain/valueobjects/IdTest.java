package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class IdTest {

	@Test
	void shouldCreateCorrectId() {
		assertThrows(DomainException.class, () -> new Id(null));
		assertDoesNotThrow(() -> new Id("any"));
	}
}
