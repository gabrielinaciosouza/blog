package com.gabriel.blog.presentation.resources;

import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.application.requests.FindPostsRequest;
import com.gabriel.blog.application.responses.FindPostsResponse;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.application.usecases.CreatePostUseCase;
import com.gabriel.blog.application.usecases.DeletePostUseCase;
import com.gabriel.blog.application.usecases.FindPostsUseCase;
import com.gabriel.blog.application.usecases.GetPostBySlug;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
  private final FindPostsUseCase findPostsUseCase;
  private final DeletePostUseCase deletePostUseCase;

  /**
   * Default constructor for the {@link PostResource} class.
   *
   * @param createPostUseCase The use case for creating a new post.
   * @param getPostBySlug     The use case for retrieving a post by its slug.
   * @param findPostsUseCase  The repository for managing post data.
   * @param deletePostUseCase The use case for deleting a post.
   */
  public PostResource(final CreatePostUseCase createPostUseCase,
                      final GetPostBySlug getPostBySlug,
                      final FindPostsUseCase findPostsUseCase,
                      final DeletePostUseCase deletePostUseCase) {
    this.createPostUseCase = createPostUseCase;
    this.getPostBySlug = getPostBySlug;
    this.findPostsUseCase = findPostsUseCase;
    this.deletePostUseCase = deletePostUseCase;
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

  /**
   * Endpoint to find blog posts based on search criteria.
   * This method receives a request to find posts based on search criteria and delegates the
   * action to the corresponding repository, returning the result.
   *
   * @param params The search criteria for finding posts.
   * @return The list of posts matching the search criteria.
   */
  @POST
  @Path("/find")
  public FindPostsResponse findPosts(final FindPostsRequest params) {
    return findPostsUseCase.findPosts(params);
  }

  /**
   * Endpoint to delete a blog post by its slug.
   * This method receives a request to delete a post by its slug and delegates the
   * action to the corresponding use case.
   *
   * @param slug The slug of the post to delete.
   */
  @DELETE
  @Path("/{slug}")
  @ResponseStatus(204)
  public void deletePost(@RestPath final String slug) {
    deletePostUseCase.deletePost(slug);
  }
}
