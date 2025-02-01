package com.gabriel.blog.domain;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Id;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


class AbstractEntityTest {

    private static final Id ID = new Id("any");
    private static final Id ID2 = new Id("any2");

    @Test
    void shouldCreateCorrectEntity() {
        final var thrown = assertThrows(DomainException.class, () -> new TestAbstractEntity(null));
        assertEquals("Tried to create an Entity with a null id", thrown.getMessage());
        assertDoesNotThrow(() -> new TestAbstractEntity(ID));
    }

    @Test
    void shouldGenerateCorrectEquals() {
        final var testEntity = new TestAbstractEntity(ID);
        final var testEntity2 = new TestAbstractEntity(ID2);
        final var testEntity3 = new TestAbstractEntity(ID);

        assertEquals(testEntity, testEntity3);
        assertEquals(testEntity, testEntity);
        assertNotEquals(testEntity, "any type");
        assertNotEquals(testEntity, null);
        assertNotEquals(testEntity, testEntity2);
    }

    @Test
    void shouldGenerateCorrectHashCode() {
        final var testEntity = new TestAbstractEntity(ID);
        final var testEntity2 = new TestAbstractEntity(ID);

        final var set = new HashSet<TestAbstractEntity>();
        set.add(testEntity);
        set.add(testEntity2);

        assertEquals(1, set.size());
    }

    @Test
    void shouldEnsureToStringReturnsCorrectFormat() {
        final var testEntity = new TestAbstractEntity(ID, "Gabriel", 30);
        final var expected = "TestAbstractEntity {\"age\":30,\"name\":\"Gabriel\",\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}";
        assertEquals(expected, testEntity.toString());
    }

    @Test
    void shouldPrintNullValuesCorrectly() {
        final var testEntity = new TestAbstractEntity(ID, null, 25);
        final var expected = "TestAbstractEntity {\"age\":25,\"name\":null,\"id\":\"Id {\\\"value\\\":\\\"any\\\"}\"}";
        assertEquals(expected, testEntity.toString());
    }


    public static class TestAbstractEntity extends AbstractEntity {

        private String name;
        private int age;

        protected TestAbstractEntity(final Id id) {
            super(id);
            this.name = null;
            this.age = 0;
        }

        public TestAbstractEntity(final Id id, final String name, int age) {
            super(id);
            this.name = name;
            this.age = age;
        }

    }
}
