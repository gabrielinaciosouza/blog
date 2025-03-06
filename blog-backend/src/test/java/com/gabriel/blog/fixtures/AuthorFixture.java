package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.entities.Author;
import com.gabriel.blog.domain.valueobjects.CreationDate;

public class AuthorFixture {

  public static Author author() {
    return new Author(IdFixture.authorId(), NameFixture.name(), EmailFixture.email(),
        CreationDate.now(), ImageFixture.image());
  }
}
