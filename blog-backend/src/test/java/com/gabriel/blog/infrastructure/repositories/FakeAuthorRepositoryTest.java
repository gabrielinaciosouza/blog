package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FakeAuthorRepositoryTest {

  @Test
  void shouldReturnTrueWhenExistsById() {
    final var fakeUserRepository = new FakeAuthorRepository();
    assertTrue(fakeUserRepository.findById(null).isPresent());
  }

}