package com.gabriel.blog.domain.valueobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import org.junit.jupiter.api.Test;

class DeletedStatusTest {

  @Test
  void shouldCreateNotDeletedStatus() {
    final var status = DeletedStatus.notDeleted();
    assertFalse(status.isDeleted());
    assertNull(status.getDeletionDate());
  }

  @Test
  void shouldCreateDeletedStatus() {
    final var status = DeletedStatus.deleted();
    assertTrue(status.isDeleted());
    assertNotNull(status.getDeletionDate());
  }

  @Test
  void shouldCreateDeletedStatusWithCustomDate() {
    final var customDate = Instant.parse("2023-01-01T00:00:00Z");
    final var status = new DeletedStatus(true, customDate);
    assertTrue(status.isDeleted());
    assertEquals(customDate, status.getDeletionDate());
  }
}