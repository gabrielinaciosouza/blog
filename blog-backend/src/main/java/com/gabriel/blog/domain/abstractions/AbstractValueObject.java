package com.gabriel.blog.domain.abstractions;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

/**
 * Represents an abstract base class for all value objects in the domain model.
 * Ensures that value objects adhere to the domain-driven design principles.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public abstract class AbstractValueObject implements DomainObject {

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

  @Override
  public boolean isEquals(final Object object) {
    return reflectionEquals(this, object, excludeFieldsFromEquality());
  }

  @Override
  public int getHashCode() {
    return reflectionHashCode(this, excludeFieldsFromEquality());
  }
}
