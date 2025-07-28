package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;

/**
 * Represents a user in the system.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 */
public class User extends AbstractEntity {

  private static final Image DEFAULT_IMAGE = new Image(
      "https://media.licdn.com/dms/image/v2/D4D03AQFYx_k72IBkLg/profile-displayphoto-shrink_800_800/B4DZRGBk.9HYAc-/0/1736341607395?e=1745452800&v=beta&t=lWzoy4ZaGd3a4LDrxBNEA0_sderdTDQKL41lVFvtWug");

  private final Email email;
  private final Role role;
  private final Name name;
  private final Image pictureUrl;

  /**
   * Constructs a new entity with the given identifier.
   *
   * @param id the unique identifier of the entity; must not be {@code null}
   *           * @throws DomainException if {@code id} is {@code null}
   */
  public User(
      final Id id,
      final Email email,
      final Role role,
      final Name name,
      final Image pictureUrl) {
    super(id);
    this.email = nonNull(email, "Tried to create a User with a null email");
    this.role = nonNull(role, "Tried to create a User with a null role");
    this.name = nonNull(name, "Tried to create a User with a null name");
    this.pictureUrl = pictureUrl == null ? DEFAULT_IMAGE : pictureUrl;
  }

  public Email getEmail() {
    return email;
  }

  public Role getRole() {
    return role;
  }

  public Name getName() {
    return name;
  }

  public Image getPictureUrl() {
    return pictureUrl;
  }

  /**
   * Role of the user.
   */
  public enum Role {
    ADMIN,
    USER
  }
}
