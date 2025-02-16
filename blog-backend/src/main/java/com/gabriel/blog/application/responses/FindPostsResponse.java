package com.gabriel.blog.application.responses;

import java.util.List;

/**
 * Response object containing a list of posts and the total count of posts.
 */
public record FindPostsResponse(List<PostResponse> posts, int totalCount) {
}
