package com.gabriel.blog.application.requests;

/**
 * Represents a request to add a new comment to a post.
 * This class contains the data required to add a new comment to a post.
 *
 * <p>Created by Gabriel Inacio de Souza on February 27, 2025.</p>
 */
public record AddCommentRequest(String postId, String authorId, String content) {
}
