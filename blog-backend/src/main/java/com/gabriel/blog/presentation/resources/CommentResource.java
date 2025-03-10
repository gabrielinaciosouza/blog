package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.AddCommentRequest;
import com.gabriel.blog.application.responses.CommentResponse;
import com.gabriel.blog.application.usecases.AddCommentUseCase;
import com.gabriel.blog.application.usecases.GetCommentsByIdUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

/**
 * Resource for comments.
 */
@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommentResource {

  private final AddCommentUseCase addCommentUseCase;
  private final GetCommentsByIdUseCase getCommentsByIdUseCase;

  /**
   * Constructs a new {@link CommentResource} with the provided use cases.
   *
   * @param addCommentUseCase the use case for adding comments
   * @param getCommentsByIdUseCase the use case for getting comments by ID
   */
  public CommentResource(final AddCommentUseCase addCommentUseCase,
                         final GetCommentsByIdUseCase getCommentsByIdUseCase) {
    this.addCommentUseCase = addCommentUseCase;
    this.getCommentsByIdUseCase = getCommentsByIdUseCase;
  }

  /**
   * Adds a comment to a blog post.
   */
  @POST
  public CommentResponse addComment(final AddCommentRequest request) {
    return addCommentUseCase.addComment(request);
  }

  /**
   * Retrieves comments by their IDs.
   */
  @POST
  @Path("/by-ids")
  public List<CommentResponse> getCommentsById(final List<String> commentIds) {
    return getCommentsByIdUseCase.getCommentsById(commentIds);
  }
}