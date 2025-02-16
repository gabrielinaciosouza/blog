package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Slug;
import com.gabriel.blog.fixtures.PostFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DeletePostUseCaseTest {

  private PostRepository postRepository;
  private DeletePostUseCase deletePostUseCase;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    deletePostUseCase = new DeletePostUseCase(postRepository);
  }

  @Test
  void shouldDeletePostWhenPostExists() {
    final var post = PostFixture.post();
    final var slug = post.getSlug();
    when(postRepository.findBySlug(slug)).thenReturn(Optional.of(post));

    deletePostUseCase.deletePost(slug.getValue());

    verify(postRepository).findBySlug(slug);
    verify(postRepository).save(post);
    assertTrue(post.isDeleted());
  }

  @Test
  void shouldDoNothingWhenPostDoesNotExist() {
    final var slug = Slug.fromString("non-existing-slug");
    when(postRepository.findBySlug(slug)).thenReturn(Optional.empty());

    deletePostUseCase.deletePost(slug.getValue());

    verify(postRepository).findBySlug(slug);
    verify(postRepository, never()).save(any(Post.class));
  }
}