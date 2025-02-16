package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.FindPostsRequest;
import com.gabriel.blog.fixtures.PostFixture;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FindPostsUseCaseTest {

  private PostRepository postRepository;
  private FindPostsUseCase findPostsUseCase;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    findPostsUseCase = new FindPostsUseCase(postRepository);
  }

  @Test
  void shouldThrowValidationExceptionWhenRequestIsNull() {
    assertThrows(ValidationException.class, () -> findPostsUseCase.findPosts(null));
  }

  @Test
  void shouldThrowValidationExceptionWhenPageIsLessThanOne() {
    final var request = new FindPostsRequest(0, 10, "title", "ASCENDING");
    assertThrows(ValidationException.class, () -> findPostsUseCase.findPosts(request));
  }

  @Test
  void shouldReturnPostsSuccessfully() {
    var request = new FindPostsRequest(1, 10, "title", "ASCENDING");
    final var post = PostFixture.post();
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.ASCENDING))).thenReturn(List.of(post));
    when(postRepository.totalCount()).thenReturn(1);

    var response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());

    request = new FindPostsRequest(1, 10, "creationDate", "DESCENDING");
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.creationDate,
            PostRepository.SortOrder.DESCENDING))).thenReturn(List.of(post));

    response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());
  }

  @Test
  void shouldReturnPostsWithDefaultSortOrder() {
    final var request = new FindPostsRequest(1, 10, "title", null);
    final var post = PostFixture.post();
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))).thenReturn(List.of(post));
    when(postRepository.totalCount()).thenReturn(1);

    final var response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());
  }

  @Test
  void shouldReturnPostsWithDefaultSortBy() {
    final var request = new FindPostsRequest(1, 10, null, "ASCENDING");
    final var post = PostFixture.post();
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.creationDate,
            PostRepository.SortOrder.ASCENDING))).thenReturn(List.of(post));
    when(postRepository.totalCount()).thenReturn(1);

    final var response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());
  }

  @Test
  void shouldReturnPostsWithDefaultPageSize() {
    final var request = new FindPostsRequest(1, 0, "title", "ASCENDING");
    final var post = PostFixture.post();
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.ASCENDING))).thenReturn(List.of(post));
    when(postRepository.totalCount()).thenReturn(1);

    final var response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());
  }

  @Test
  void shouldReturnPostsWithDefaultSortOrderIfSortOrderIsNotValid() {
    final var request = new FindPostsRequest(1, 10, null, "any");
    final var post = PostFixture.post();
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.creationDate,
            PostRepository.SortOrder.DESCENDING))).thenReturn(List.of(post));
    when(postRepository.totalCount()).thenReturn(1);

    final var response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());
  }

  @Test
  void shouldReturnPostsWithDefaultSortByIfSortByIsNotValid() {
    final var request = new FindPostsRequest(1, 10, "any", "ASCENDING");
    final var post = PostFixture.post();
    when(postRepository.findPosts(
        new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.creationDate,
            PostRepository.SortOrder.ASCENDING))).thenReturn(List.of(post));
    when(postRepository.totalCount()).thenReturn(1);

    final var response = findPostsUseCase.findPosts(request);

    assertEquals(1, response.totalCount());
    assertEquals(1, response.posts().size());
    assertEquals("any title", response.posts().getFirst().title());
  }
}