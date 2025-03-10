package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.AuthorRepository;
import com.gabriel.blog.application.repositories.CommentRepository;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.fixtures.AuthorFixture;
import com.gabriel.blog.fixtures.CommentFixture;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GetCommentsByIdUseCaseTest {

  private CommentRepository commentRepository;
  private AuthorRepository authorRepository;
  private GetCommentsByIdUseCase getCommentsByIdUseCase;

  @BeforeEach
  void setUp() {
    commentRepository = mock(CommentRepository.class);
    authorRepository = mock(AuthorRepository.class);
    getCommentsByIdUseCase = new GetCommentsByIdUseCase(commentRepository, authorRepository);
  }

  @Test
  void getCommentsByIdSuccessfully() {
    final var comment = CommentFixture.comment();
    final var author = AuthorFixture.author();
    when(commentRepository.getCommentsById(anyList())).thenReturn(List.of(comment));
    when(authorRepository.findById(any(Id.class))).thenReturn(Optional.of(author));

    final var responses =
        getCommentsByIdUseCase.getCommentsById(List.of(comment.getId().getValue()));

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals("any", responses.getFirst().commentId());
  }

  @Test
  void getCommentsByIdWithNullInput() {
    final var responses = getCommentsByIdUseCase.getCommentsById(null);

    assertNotNull(responses);
    assertTrue(responses.isEmpty());
  }

  @Test
  void getCommentsByIdWithNonExistentAuthor() {
    final var comment = CommentFixture.comment();
    when(commentRepository.getCommentsById(anyList())).thenReturn(List.of(comment));
    when(authorRepository.findById(any(Id.class))).thenReturn(Optional.empty());

    final var responses = getCommentsByIdUseCase.getCommentsById(List.of("commentId"));

    assertNotNull(responses);
    assertTrue(responses.isEmpty());
  }

  @Test
  void getCommentsByIdWithNonExistentComment() {
    when(commentRepository.getCommentsById(anyList())).thenReturn(List.of());

    final var responses = getCommentsByIdUseCase.getCommentsById(List.of("commentId"));

    assertNotNull(responses);
    assertTrue(responses.isEmpty());
  }

  @Test
  void getCommentsByIdWithEmptyList() {
    final var responses = getCommentsByIdUseCase.getCommentsById(List.of());

    assertNotNull(responses);
    assertTrue(responses.isEmpty());
  }

  @Test
  void getCommentsByIdWithNullCommentId() {
    final var comment = CommentFixture.comment();
    final var author = AuthorFixture.author();
    when(commentRepository.getCommentsById(anyList())).thenReturn(List.of(comment));
    when(authorRepository.findById(any(Id.class))).thenReturn(Optional.of(author));

    final var responses = getCommentsByIdUseCase.getCommentsById(new ArrayList<>() {{
      add(null);
    }});

    assertNotNull(responses);
  }

  @Test
  void getCommentsByIdWithMixedValidAndInvalidIds() {
    final var comment = CommentFixture.comment();
    final var author = AuthorFixture.author();
    when(commentRepository.getCommentsById(anyList())).thenReturn(List.of(comment));
    when(authorRepository.findById(any(Id.class))).thenReturn(Optional.of(author));

    final var responses = getCommentsByIdUseCase.getCommentsById(List.of("validId", "invalidId"));

    assertNotNull(responses);
    assertEquals(1, responses.size());
    assertEquals("any", responses.getFirst().commentId());
  }
}