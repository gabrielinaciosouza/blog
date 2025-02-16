package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.CreationDate;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class CreationDateFixture {

  public static CreationDate creationDate() {
    return new CreationDate(
        LocalDate.of(2024, 12, 12).atStartOfDay().toInstant(ZoneOffset.UTC));
  }
}
