package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.NotFoundException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.fixtures.PostFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetPostBySlugTest {

  private PostRepository postRepository;

  private GetPostBySlug getPostBySlug;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    getPostBySlug = new GetPostBySlug(postRepository);
  }

  @Test
  void shouldReturnPostBySlug() {
    when(postRepository.findBySlug(Slug.fromString("test-slug"))).thenReturn(
        Optional.of(PostFixture.post()));

    final var result = getPostBySlug.getPostBySlug("test-slug");

    verify(postRepository).findBySlug(Slug.fromString("test-slug"));
    assertEquals(
        new PostResponse("any", "any title", "any content", "2024-12-12 01:00", "any-title",
            "https://example.com/image.jpg"),
        result);
  }

  @Test
  void shouldThrowExceptionWhenSlugIsNull() {
    final var exception = assertThrows(DomainException.class,
        () -> getPostBySlug.getPostBySlug(null));
    assertEquals("Tried to create a Slug with a null value", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenPostNotFound() {
    when(postRepository.findBySlug(Slug.fromString("test-slug"))).thenReturn(Optional.empty());

    final var exception = assertThrows(NotFoundException.class,
        () -> getPostBySlug.getPostBySlug("test-slug"));
    assertEquals("Post with slug test-slug not found", exception.getMessage());
  }

}