package com.gabriel.blog.application.requests;

/**
 * Request object for finding posts based on the provided search criteria.
 */
public record FindPostsRequest(int page, int pageSize, String sortBy, String sortOrder) {
}
