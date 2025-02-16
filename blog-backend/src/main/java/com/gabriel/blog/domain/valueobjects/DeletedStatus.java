package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.abstractions.AbstractValueObject;
import com.gabriel.blog.domain.exceptions.DomainException;
import java.time.Instant;

/**
 * Represents the deletion status of an entity as a value object.
 * This class ensures that the status is not null
 * and provides methods to mark as deleted or not deleted.
 *
 * <p>Created by Gabriel Inacio de Souza on February 1, 2025.</p>
 */
public class DeletedStatus extends AbstractValueObject {

  private final boolean value;
  private final Instant deletionDate;

  /**
   * Constructs a {@code DeletedStatus} instance with the given status and deletion date.
   *
   * @param value        the deletion status, must not be null
   * @param deletionDate the deletion date, can be null if not deleted
   * @throws DomainException if the provided status is null
   */
  public DeletedStatus(final boolean value, final Instant deletionDate) {
    this.value = value;
    this.deletionDate = nonNull(deletionDate, "Deletion date must not be null");
  }

  /**
   * Creates a {@code DeletedStatus} instance representing a non-deleted status.
   *
   * @return a new {@code DeletedStatus} set to false with no deletion date
   */
  public static DeletedStatus notDeleted() {
    return new DeletedStatus(false, Instant.MIN);
  }

  /**
   * Creates a {@code DeletedStatus} instance representing a deleted status.
   *
   * @return a new {@code DeletedStatus} set to true with the current date as deletion date
   */
  public static DeletedStatus deleted() {
    return new DeletedStatus(true, Instant.now());
  }

  public boolean isDeleted() {
    return value;
  }

  public Instant getDeletionDate() {
    return deletionDate;
  }
}