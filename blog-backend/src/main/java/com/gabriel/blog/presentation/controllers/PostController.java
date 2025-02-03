package com.gabriel.blog.presentation.controllers;

import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.responses.CreatePostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import com.gabriel.blog.presentation.exceptions.GlobalExceptionHandler;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

/**
 * Controller class for handling post-related operations.
 * Provides the endpoint to create a new blog post.
 */
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostController {

  private final CreatePostUseCase createPostUseCase;

  /**
   * Constructs a new PostController with the specified use case.
   *
   * @param createPostUseCase The use case for creating a blog post.
   */
  public PostController(final CreatePostUseCase createPostUseCase) {
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
  public Response create(final CreatePostRequest request) {
    return Response.status(Response.Status.CREATED)
            .type(MediaType.APPLICATION_JSON)
            .entity(createPostUseCase.create(request))
            .build();
  }
}
