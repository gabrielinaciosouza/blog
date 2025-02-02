package com.gabriel.blog.domain;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Id;
import java.util.Objects;

/**
 * Represents an abstract base class for all domain entities.
 * Ensures that each entity has a unique identifier and logs its creation.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public abstract class AbstractEntity implements DomainObject {

  private final Id id;

  /**
   * Constructs a new entity with the given identifier.
   *
   * @param id the unique identifier of the entity; must not be {@code null}
   * @throws DomainException if {@code id} is {@code null}
   */
  protected AbstractEntity(final Id id) {
    this.id = nonNull(id, "Tried to create an Entity with a null id");
    logger().info("Created new entity with id: " + id.getValue());
  }

  @Override
  public boolean isEquals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
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
  public boolean equals(final Object obj) {
    return isEquals(obj);
  }

  @Override
  public String toString() {
    return stringify();
  }
}
