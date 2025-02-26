package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.fixtures.PostFixture;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GetDeletedPostsTest {

  private static final PostRepository.FindPostsParams params =
      new PostRepository.FindPostsParams(1, Integer.MAX_VALUE, null, null, true);
  private PostRepository postRepository;
  private GetDeletedPosts getDeletedPosts;

  @BeforeEach
  void setUp() {
    postRepository = Mockito.mock(PostRepository.class);
    getDeletedPosts = new GetDeletedPosts(postRepository);
  }

  @Test
  void retrievesDeletedPostsSuccessfully() {
    final var post = PostFixture.post();
    when(postRepository.findPosts(params)).thenReturn(List.of(post));

    final var result = getDeletedPosts.getDeletedPosts();

    assertEquals(1, result.size());
    assertEquals("any title", result.getFirst().title());
  }

  @Test
  void retrievesNoDeletedPostsWhenNoneExist() {
    when(postRepository.findPosts(params)).thenReturn(Collections.emptyList());

    final var result = getDeletedPosts.getDeletedPosts();

    assertEquals(0, result.size());
  }
}