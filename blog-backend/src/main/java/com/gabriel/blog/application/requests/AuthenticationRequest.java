package com.gabriel.blog.application.requests;

/**
 * Represents a request for authentication using an ID token.
 * This record encapsulates the ID token required for authentication.
 *
 * @param idToken The ID token used for authentication.
 */
public record AuthenticationRequest(
    String idToken
) {
}
