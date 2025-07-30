package com.gabriel.blog.presentation.exceptions;

/**
 * Internal class to represent standardized error responses.
 * Uses a record for simplicity and immutability.
 *
 * @param error   Type of the error.
 * @param message Detailed error message.
 */
public record ErrorResponse(String error, String message) {
}