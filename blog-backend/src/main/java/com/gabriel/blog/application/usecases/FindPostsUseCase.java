package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.FindPostsRequest;
import com.gabriel.blog.application.responses.FindPostsResponse;
import com.gabriel.blog.application.responses.PostResponse;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Use case for finding posts based on the provided search criteria.
 */
@ApplicationScoped
public class FindPostsUseCase {

  private final PostRepository postRepository;

  /**
   * Default constructor for the {@link FindPostsUseCase} class.
   *
   * @param postRepository The repository for managing post data.
   */
  public FindPostsUseCase(final PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  /**
   * Find posts based on the provided request.
   *
   * @param request The request containing the search criteria.
   * @return The response containing the list of posts and the total count.
   */
  public FindPostsResponse findPosts(final FindPostsRequest request) {
    if (request == null) {
      throw new ValidationException("Request must not be null");
    }

    if (request.page() < 1) {
      throw new ValidationException("Page must be greater than 0");
    }

    var sortBy = PostRepository.SortBy.creationDate;
    if (request.sortBy() != null && (request.sortBy().equalsIgnoreCase("title") || request.sortBy()
        .equalsIgnoreCase("creationDate"))) {
      sortBy = PostRepository.SortBy.valueOf(request.sortBy().toLowerCase());
    }

    var sortOrder = PostRepository.SortOrder.DESCENDING;
    if (request.sortOrder() != null && (request.sortOrder().equalsIgnoreCase("ASCENDING")
        || request.sortOrder()
        .equalsIgnoreCase("DESCENDING"))) {
      sortOrder = PostRepository.SortOrder.valueOf(request.sortOrder().toUpperCase());
    }

    final var pageSize = request.size() < 1 ? 10 : request.size();
    final var params =
        new PostRepository.FindPostsParams(request.page(), pageSize, sortBy, sortOrder);

    final var findResult = postRepository.findPosts(params);
    final var total = postRepository.totalCount();

    return new FindPostsResponse(findResult.stream().map(post -> new PostResponse(
        post.getId().getValue(),
        post.getTitle().getValue(),
        post.getContent().getValue(),
        post.getCreationDate().toString(),
        post.getSlug().getValue())).toList(), total);
  }
}
