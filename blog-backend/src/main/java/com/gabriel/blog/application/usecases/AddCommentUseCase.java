package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.AuthorRepository;
import com.gabriel.blog.application.repositories.CommentRepository;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.responses.AuthorResponse;
import com.gabriel.blog.application.responses.CommentResponse;
import com.gabriel.blog.application.services.IdGenerator;
import com.gabriel.blog.domain.entities.Comment;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.DeletedStatus;
import com.gabriel.blog.domain.valueobjects.Id;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Use case to add a comment to a post.
 */
@ApplicationScoped
public class AddCommentUseCase {

  private static final String COMMENT_DOMAIN = "comment";

  private final PostRepository postRepository;
  private final AuthorRepository authorRepository;
  private final CommentRepository commentRepository;
  private final IdGenerator idGenerator;

  /**
   * Default constructor for the {@link AddCommentUseCase} class.
   *
   * @param postRepository   the repository for managing post data.
   * @param authorRepository the repository for managing user data.
   * @param idGenerator      the service for generating IDs.
   */
  public AddCommentUseCase(final PostRepository postRepository,
                           final AuthorRepository authorRepository,
                           final CommentRepository commentRepository,
                           final IdGenerator idGenerator) {
    this.postRepository = postRepository;
    this.authorRepository = authorRepository;
    this.commentRepository = commentRepository;
    this.idGenerator = idGenerator;
  }


  /**
   * Adds a comment to a post.
   *
   * @param request the request containing the comment data.
   */
  public CommentResponse addComment(final AddCommentRequest request) {
    final var content = new Content(request.content());
    final var postId = new Id(request.postId());
    final var postOptional = postRepository.findById(postId);

    if (postOptional.isEmpty()) {
      throw new ValidationException("Post not found");
    }

    final var authorId = new Id(request.authorId());
    final var userOptional = authorRepository.findById(authorId);

    if (userOptional.isEmpty()) {
      throw new ValidationException("Author not found");
    }

    final var author = userOptional.get();

    final var post = postOptional.get();

    final var comment =
        new Comment(new Id(idGenerator.generateId(COMMENT_DOMAIN)), authorId, content,
            CreationDate.now(), DeletedStatus.notDeleted());
    commentRepository.save(comment);

    post.linkComment(comment.getId());
    postRepository.update(post);

    return new CommentResponse(
        comment.getId().getValue(),
        comment.getContent().getValue(),
        comment.getCreationDate().toString(),
        new AuthorResponse(
            author.getId().getValue(),
            author.getName().getValue(),
            author.getEmail().getEmail(),
            author.getProfilePicture().toString()
        )
    );
  }
}
