package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.valueobjects.CreationDate;
import java.time.LocalDate;

public class CreationDateFixture {

  public static CreationDate creationDate() {
    return new CreationDate(LocalDate.of(2024, 12, 12));
  }
}
