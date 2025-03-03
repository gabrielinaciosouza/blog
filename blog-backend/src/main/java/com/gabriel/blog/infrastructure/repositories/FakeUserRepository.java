package com.gabriel.blog.infrastructure.repositories;

import com.gabriel.blog.application.repositories.UserRepository;
import com.gabriel.blog.domain.valueobjects.Id;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Fake implementation of the {@link UserRepository} interface.
 */
@ApplicationScoped
public class FakeUserRepository implements UserRepository {

  @Override
  public boolean existsById(final Id id) {
    return true;
  }
}
