package com.gabriel.blog.presentation.exceptions;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Internal class to represent standardized error responses.
 * Uses a record for simplicity and immutability.
 *
 * @param error   Type of the error.
 * @param message Detailed error message.
 */
@RegisterForReflection
public record ErrorResponse(String error, String message) {
}