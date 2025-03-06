package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.repositories.AuthorRepository;
import com.gabriel.blog.application.repositories.CommentRepository;
import com.gabriel.blog.application.responses.AuthorResponse;
import com.gabriel.blog.application.responses.CommentResponse;
import com.gabriel.blog.domain.entities.Comment;
import com.gabriel.blog.domain.valueobjects.Id;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Use case to get a comment by its ID.
 */
public class GetCommentById {

  private final CommentRepository commentRepository;
  private final AuthorRepository authorRepository;

  /**
   * Default constructor for the {@link GetCommentById} class.
   *
   * @param commentRepository the repository for managing comment data.
   * @param authorRepository  the repository for managing author data.
   */
  public GetCommentById(final CommentRepository commentRepository,
                        final AuthorRepository authorRepository) {
    this.commentRepository = commentRepository;
    this.authorRepository = authorRepository;
  }

  /**
   * Gets a list of comments by their IDs.
   *
   * @param comments the list of comments to get.
   * @return a list of {@link CommentResponse}.
   */
  public List<CommentResponse> getCommentsById(final List<String> comments) {
    if (comments == null) {
      return List.of();
    }
    final var commentsIds = comments
        .stream()
        .filter(Objects::nonNull)
        .map(Id::new)
        .toList();

    final var commentsList = commentRepository.getCommentsById(commentsIds);

    return commentsList.stream()
        .filter(Objects::nonNull)
        .map(this::mapToCommentResponse)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toList();
  }

  private Optional<CommentResponse> mapToCommentResponse(final Comment comment) {
    final var authorOptional = authorRepository.findById(comment.getAuthorId());
    if (authorOptional.isEmpty()) {
      return Optional.empty();
    }
    final var author = authorOptional.get();
    final var authorResponse = new AuthorResponse(
        author.getId().getValue(),
        author.getName().getValue(),
        author.getEmail().getEmail(),
        author.getProfilePicture().toString()
    );
    return Optional.of(new CommentResponse(
        comment.getId().getValue(),
        comment.getContent().getValue(),
        comment.getCreationDate().toString(),
        authorResponse
    ));
  }
}