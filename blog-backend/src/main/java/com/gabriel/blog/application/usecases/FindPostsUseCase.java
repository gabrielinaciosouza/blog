package com.gabriel.blog.application.usecases;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.FindPostsRequest;
import com.gabriel.blog.application.responses.FindPostsResponse;
import com.gabriel.blog.application.responses.PostResponse;
import com.gabriel.blog.domain.entities.Post;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.concurrent.atomic.AtomicInteger;

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
    validateRequest(request);

    final var sortBy = determineSortBy(request.sortBy());
    final var sortOrder = determineSortOrder(request.sortOrder());
    final var pageSize = determinePageSize(request.size());

    final var params =
        new PostRepository.FindPostsParams(request.page(), pageSize, sortBy, sortOrder);
    final var findResult = postRepository.findPosts(params);
    final var total = new AtomicInteger(postRepository.totalCount());

    return new FindPostsResponse(
        findResult.stream()
            .filter(post -> filterDeletedPosts(post, total))
            .map(this::mapToPostResponse)
            .toList(), total.get());
  }

  private void validateRequest(final FindPostsRequest request) {
    if (request == null) {
      throw new ValidationException("Request must not be null");
    }

    if (request.page() < 1) {
      throw new ValidationException("Page must be greater than 0");
    }
  }

  private PostRepository.SortBy determineSortBy(final String sortBy) {
    if (sortBy != null && (sortBy.equals("title") || sortBy.equals("creationDate"))) {
      return PostRepository.SortBy.valueOf(sortBy);
    }
    return PostRepository.SortBy.creationDate;
  }

  private PostRepository.SortOrder determineSortOrder(final String sortOrder) {
    if (sortOrder != null && (sortOrder.equalsIgnoreCase("ASCENDING") || sortOrder.equalsIgnoreCase(
        "DESCENDING"))) {
      return PostRepository.SortOrder.valueOf(sortOrder.toUpperCase());
    }
    return PostRepository.SortOrder.DESCENDING;
  }

  private int determinePageSize(final int size) {
    return size < 1 ? 10 : size;
  }

  private boolean filterDeletedPosts(final Post post, final AtomicInteger total) {
    if (post.isDeleted()) {
      total.decrementAndGet();
      return false;
    }
    return true;
  }

  private PostResponse mapToPostResponse(final Post post) {
    return new PostResponse(
        post.getId().getValue(),
        post.getTitle().getValue(),
        post.getContent().getValue(),
        post.getCreationDate().toString(),
        post.getSlug().getValue(),
        post.getCoverImage().toString());
  }
}