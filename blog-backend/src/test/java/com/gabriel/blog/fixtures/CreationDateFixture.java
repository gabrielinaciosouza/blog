package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.CreationDate;
import java.time.Instant;

public class CreationDateFixture {

  public static CreationDate creationDate() {
    return new CreationDate(Instant.parse("2024-12-12T01:00:00Z"));
  }
}
