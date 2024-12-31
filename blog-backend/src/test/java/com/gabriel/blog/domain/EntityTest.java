package com.gabriel.blog.domain;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityTest {

	@Test
	void shouldCreateCorrectEntity() {
		final var thrown = assertThrows(DomainException.class, () -> new TestEntity(null));
		assertEquals("Tried to create an entity with a null id", thrown.getMessage());
		assertDoesNotThrow(() -> new TestEntity(new Id("any")));
	}

	@Test
	void shouldGenerateCorrectEquals() {
		final var testEntity = new TestEntity(new Id("any"));
		final var testEntity2 = new TestEntity(new Id("any2"));
		final var testEntity3 = new TestEntity(new Id("any"));

		assertEquals(testEntity, testEntity3);
		assertNotEquals(testEntity, testEntity2);
	}

	@Test
	void shouldGenerateCorrectHashCode() {
		final var testEntity = new TestEntity(new Id("any"));
		final var testEntity2 = new TestEntity(new Id("any"));

		final var set = new HashSet<TestEntity>();
		set.add(testEntity);
		set.add(testEntity2);

		assertEquals(1, set.size());
	}

	private static class TestEntity extends Entity {

		protected TestEntity(final Id id) {
			super(id);
		}

		@Override
		public String toString() {
			return "TestEntity{" + "id=" + getId() + "}";
		}
	}
}
