package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.responses.CreatePostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * Controller class for handling post-related operations.
 * Provides the endpoint to create a new blog post.
 */
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

  private final CreatePostUseCase createPostUseCase;

  /**
   * Constructs a new PostController with the specified use case.
   *
   * @param createPostUseCase The use case for creating a blog post.
   */
  public PostResource(final CreatePostUseCase createPostUseCase) {
    this.createPostUseCase = createPostUseCase;
  }

  /**
   * Endpoint to create a new blog post.
   * This method receives a request to create a new post and delegates the
   * action to the corresponding use case, returning the result.
   *
   * @param request The request containing the details of the post to be created.
   * @return The response containing the created post details.
   */
  @POST
  public CreatePostResponse create(final CreatePostRequest request) {
    return createPostUseCase.create(request);
  }
}
