package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.AuthorRepository;
import com.gabriel.blog.application.repositories.CommentRepository;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.services.IdGenerator;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.fixtures.AuthorFixture;
import com.gabriel.blog.fixtures.PostFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AddCommentUseCaseTest {

  private PostRepository postRepository;
  private AuthorRepository authorRepository;
  private CommentRepository commentRepository;
  private IdGenerator idGenerator;
  private AddCommentUseCase addCommentUseCase;

  @BeforeEach
  void setUp() {
    postRepository = mock(PostRepository.class);
    authorRepository = mock(AuthorRepository.class);
    commentRepository = mock(CommentRepository.class);
    idGenerator = mock(IdGenerator.class);
    addCommentUseCase =
        new AddCommentUseCase(postRepository, authorRepository, commentRepository, idGenerator);
  }

  @Test
  void addCommentSuccessfully() {
    final var request = new AddCommentRequest("postId", "authorId", "content");
    final var post = PostFixture.post();
    final var author = AuthorFixture.author();
    when(postRepository.findById(any(Id.class))).thenReturn(Optional.of(post));
    when(authorRepository.findById(any(Id.class))).thenReturn(Optional.of(author));
    when(idGenerator.generateId(anyString())).thenReturn("generatedId");

    final var response = addCommentUseCase.addComment(request);

    assertEquals("generatedId", response.commentId());
    assertEquals("content", response.content());
    assertNotNull(response.creationDate());
    assertEquals("author id", response.author().authorId());
    assertEquals("any name", response.author().name());
    assertEquals("email@email.com", response.author().email());
    assertEquals("https://example.com/image.jpg", response.author().profilePicture());
    verify(commentRepository, times(1)).save(any());
    verify(postRepository, times(1)).update(post);
  }

  @Test
  void addCommentPostNotFound() {
    final var request = new AddCommentRequest("postId", "authorId", "content");
    when(postRepository.findById(any(Id.class))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(ValidationException.class, () -> addCommentUseCase.addComment(request));

    assertEquals("Post not found", exception.getMessage());
    verify(commentRepository, never()).save(any());
    verify(postRepository, never()).update(any());
  }

  @Test
  void addCommentAuthorNotFound() {
    final var request = new AddCommentRequest("postId", "authorId", "content");
    final var post = mock(Post.class);
    when(postRepository.findById(any(Id.class))).thenReturn(Optional.of(post));
    when(authorRepository.findById(any(Id.class))).thenReturn(Optional.empty());

    final var exception =
        assertThrows(ValidationException.class, () -> addCommentUseCase.addComment(request));

    assertEquals("Author not found", exception.getMessage());
    verify(commentRepository, never()).save(any());
    verify(postRepository, never()).update(any());
  }
}