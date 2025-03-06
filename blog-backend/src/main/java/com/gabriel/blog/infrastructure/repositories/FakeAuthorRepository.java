package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.AuthorRepository;
import com.gabriel.blog.domain.entities.Author;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Optional;

/**
 * Fake implementation of the {@link AuthorRepository} interface.
 */
@ApplicationScoped
public class FakeAuthorRepository implements AuthorRepository {


  @Override
  public Optional<Author> findById(final Id id) {
    return Optional.of(new Author(new Id("any"), new Name("John Doe"), new Email("email@email.com"),
        CreationDate.now(), new Image("http://image.com")));
  }
}
