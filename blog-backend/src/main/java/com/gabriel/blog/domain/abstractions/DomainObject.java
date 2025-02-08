package com.gabriel.blog.domain.abstractions;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.logging.Logger;

/**
 * Represents a domain object that provides common functionality.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public interface DomainObject {

  /**
   * Determines if the given object is equal to the current domain object.
   *
   * @param o the object to compare to
   * @return {@code true} if the objects are considered equal, {@code false} otherwise
   */
  boolean isEquals(Object o);

  /**
   * Returns the hash code of the current domain object.
   *
   * @return the hash code of the object
   */
  int getHashCode();

  /**
   * Converts the domain object to a JSON-like string representation using reflection.
   *
   * @return a string representation of the object in JSON style
   */
  default String stringify() {
    return getClass().getSimpleName() + " " + reflectionToString(this, ToStringStyle.JSON_STYLE);
  }

  /**
   * Provides a logger for the domain object class.
   *
   * @return a logger instance for the class
   */
  default Logger logger() {
    return Logger.getLogger(getClass());
  }

  /**
   * Excludes specified fields from equality checks.
   *
   * @param fields the names of the fields to exclude from equality checks
   * @return the array of excluded field names
   */
  default String[] excludeFieldsFromEquality(final String... fields) {
    return fields;
  }

  /**
   * Checks if an object is {@code null} and throws a {@link DomainException} if it is.
   *
   * @param obj     the object to check for null
   * @param message the message to include in the exception if the object is null
   * @param <T>     the type of the object
   * @return the object if it is not {@code null}
   * @throws DomainException if the object is {@code null}
   */
  default <T> T nonNull(final T obj, final String message) {
    if (obj == null) {
      logger().error(message);
      throw new DomainException(message);
    }
    return obj;
  }
}
