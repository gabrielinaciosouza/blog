package com.gabriel.blog.domain;

import com.gabriel.blog.domain.valueobjects.Id;

import java.util.Objects;

public abstract class AbstractEntity implements DomainObject {

    private final Id id;

    protected AbstractEntity(final Id id) {
        this.id = nonNull(id, "Tried to create an Entity with a null id");
        logger().info("Created new entity with id: " + id.getValue());
    }

    @Override
    public boolean isEquals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractEntity entity = (AbstractEntity) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int getHashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public int hashCode() {
        return getHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return isEquals(obj);
    }

    @Override
    public String toString() {
        return stringify();
    }
}
