package com.gabriel.blog.application.responses;

/**
 * Response for a comment.
 */
public record CommentResponse(
    String commentId,
    String content,
    String creationDate,
    AuthorResponse author
) {
}
