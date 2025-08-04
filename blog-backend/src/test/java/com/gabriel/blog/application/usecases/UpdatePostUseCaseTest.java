package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.NotFoundException;
import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.PostRequest;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UpdatePostUseCaseTest {

  @Mock
  private PostRepository postRepository;

  private UpdatePostUseCase updatePostUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    updatePostUseCase = new UpdatePostUseCase(postRepository);
  }

  @Test
  void shouldUpdatePostSuccessfully() {
    final var slug = "test-title";
    final var request = new PostRequest("New Title", "New Content", "https://img.com/new.jpg");
    final var post = mock(Post.class);
    when(postRepository.findBySlug(Slug.fromString(slug))).thenReturn(Optional.of(post));

    updatePostUseCase.updatePost(slug, request);

    verify(postRepository).findBySlug(Slug.fromString(slug));
    verify(postRepository).update(post);
  }

  @Test
  void shouldThrowValidationExceptionWhenRequestIsNull() {
    final var slug = "test-title";
    final var exception =
        assertThrows(ValidationException.class, () -> updatePostUseCase.updatePost(slug, null));
    assertEquals("Request cannot be null", exception.getMessage());
  }

  @Test
  void shouldThrowNotFoundExceptionWhenPostDoesNotExist() {
    final var slug = "not-found";
    final var request = new PostRequest("Title", "Content", "https://img.com/img.jpg");
    when(postRepository.findBySlug(Slug.fromString(slug))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(NotFoundException.class, () -> updatePostUseCase.updatePost(slug, request));
    assertEquals("Post not found with slug: not-found", exception.getMessage());
  }
}

