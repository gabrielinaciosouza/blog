package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;

/**
 * Represents a user in the system.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 */
public class User extends AbstractEntity {

  private final Email email;
  private final Role role;

  /**
   * Constructs a new entity with the given identifier.
   *
   * @param id the unique identifier of the entity; must not be {@code null}
   *           * @throws DomainException if {@code id} is {@code null}
   */
  public User(final Id id, final Email email, final Role role) {
    super(id);
    this.email = nonNull(email, "Tried to create a User with a null email");
    this.role = nonNull(role, "Tried to create a User with a null role");
  }

  public Email getEmail() {
    return email;
  }

  public Role getRole() {
    return role;
  }

  /**
   * Role of the user.
   */
  public enum Role {
    ADMIN,
    USER
  }
}
