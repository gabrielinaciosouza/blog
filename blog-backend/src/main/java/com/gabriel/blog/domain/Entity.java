package com.gabriel.blog.domain;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Id;

import java.util.Objects;

public abstract class Entity {

	private final Id id;

	protected Entity(final Id id) {
		this.id = nonNull(id, "Tried to create an entity with a null id");
	}

	public Id getId() {
		return id;
	}

	protected static <T> T nonNull(final T obj, final String message) {
		if (obj == null) {
			throw new DomainException(message);
		}
		return obj;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Entity entity = (Entity) o;
		return Objects.equals(id, entity.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

	@Override
	public abstract String toString();
}
