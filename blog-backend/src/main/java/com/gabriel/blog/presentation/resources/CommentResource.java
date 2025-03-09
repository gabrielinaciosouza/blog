package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.responses.CommentResponse;
import com.gabriel.blog.application.usecases.AddCommentUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Resource for comments.
 */
@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {

  private final AddCommentUseCase addCommentUseCase;

  /**
   * Constructs a new {@link CommentResource} with the provided {@link AddCommentUseCase}.
   *
   * @param addCommentUseCase the use case for adding comments
   */
  public CommentResource(final AddCommentUseCase addCommentUseCase) {
    this.addCommentUseCase = addCommentUseCase;
  }

  /**
   * Adds a comment to a blog post.
   */
  @POST
  public CommentResponse addComment(final AddCommentRequest request) {
    return addCommentUseCase.addComment(request);
  }
}
