package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FakeUserRepositoryTest {

  @Test
  void shouldReturnTrueWhenExistsById() {
    final var fakeUserRepository = new FakeUserRepository();
    assertTrue(fakeUserRepository.existsById(null));
  }

}