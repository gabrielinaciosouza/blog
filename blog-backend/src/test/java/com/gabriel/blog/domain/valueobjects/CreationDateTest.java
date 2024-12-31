package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CreationDateTest {

	@Test
	void shouldCreateCorrectContent() {
		final var thrown = assertThrows(DomainException.class, () -> new CreationDate(null));
		assertEquals("Tried to create a CreationDate with a null value", thrown.getMessage());
		assertDoesNotThrow(() -> new CreationDate(LocalDate.now()));
	}

	@Test
	void shouldCreateCorrectCreationDateNow() {
		assertDoesNotThrow(() -> new CreationDate(LocalDate.now()));
		assertEquals(LocalDate.now(), CreationDate.now().value());
	}
}
