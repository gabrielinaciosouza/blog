package com.gabriel.blog.application.responses;

/**
 * This class represents the response for authorization-related use cases.
 * It contains information about the authorization status and any relevant details.
 *
 * <p>Created by Gabriel Inacio de Souza on April 26, 2025.</p>
 */
public record AuthenticationResponse(
    String authToken,
    String userId,
    String role,
    String name,
    String email,
    String pictureUrl
) {
}
