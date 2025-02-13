package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import com.gabriel.blog.application.usecases.GetPostBySlug;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.RestPath;

/**
 * Controller class for handling post-related operations.
 * Provides the endpoint to create a new blog post.
 */
@Path("/posts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PostResource {

  private final CreatePostUseCase createPostUseCase;
  private final GetPostBySlug getPostBySlug;

  /**
   * Constructs a new PostController with the specified use case.
   *
   * @param createPostUseCase The use case for creating a blog post.
   */
  public PostResource(final CreatePostUseCase createPostUseCase,
                      final GetPostBySlug getPostBySlug) {
    this.createPostUseCase = createPostUseCase;
    this.getPostBySlug = getPostBySlug;
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
  @ResponseStatus(201)
  public PostResponse create(final CreatePostRequest request) {
    return createPostUseCase.create(request);
  }

  /**
   * Endpoint to retrieve a blog post by its slug.
   * This method receives a request to retrieve a post by its slug and delegates the
   * action to the corresponding use case, returning the result.
   *
   * @param slug The slug of the post to retrieve.
   * @return The response containing the post details.
   */
  @GET
  @Path("/{slug}")
  public PostResponse getPostBySlug(@RestPath final String slug) {
    return getPostBySlug.getPostBySlug(slug);
  }
}
