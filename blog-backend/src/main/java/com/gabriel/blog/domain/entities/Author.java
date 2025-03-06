package com.gabriel.blog.domain.entities;


import com.gabriel.blog.domain.abstractions.AbstractEntity;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;

/**
 * Represents a user in the system.
 */
public class Author extends AbstractEntity {

  private final Name name;
  private final Email email;
  private final CreationDate creationDate;
  private final Image profilePicture;


  /**
   * Constructs a {@link Author} instance with the given name, email, and creation date.
   *
   * @param id           the user id, must not be null
   * @param name         the username, must not be null
   * @param email        the user email, must not be null
   * @param creationDate the user creation date, must not be null
   */
  public Author(final Id id, final Name name, final Email email, final CreationDate creationDate,
                final Image profilePicture) {
    super(id);
    this.name = nonNull(name, "Tried to create a User with a null name");
    this.email = nonNull(email, "Tried to create a User with a null email");
    this.creationDate = nonNull(creationDate, "Tried to create a User with a null creationDate");
    this.profilePicture =
        nonNull(profilePicture, "Tried to create a User with a null profilePicture");
  }

  public Name getName() {
    return name;
  }

  public Email getEmail() {
    return email;
  }

  public CreationDate getCreationDate() {
    return creationDate;
  }

  public Image getProfilePicture() {
    return profilePicture;
  }
}
