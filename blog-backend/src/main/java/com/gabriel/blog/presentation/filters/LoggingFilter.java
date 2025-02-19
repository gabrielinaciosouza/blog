package com.gabriel.blog.presentation.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

/**
 * Filter class to log requests and responses.
 *
 * <p>Created by Gabriel Inacio de Souza on February 9, 2025.</p>
 */
//@Provider
public class LoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {

  private static final Logger LOGGER = Logger.getLogger(LoggingFilter.class.getName());

  @Override
  public void filter(final ContainerRequestContext requestContext) {
    LOGGER.info("Request Method: " + requestContext.getMethod());
    LOGGER.info("Request URI: " + requestContext.getUriInfo().getRequestUri().toString());
    LOGGER.info("Request Headers: " + requestContext.getHeaders().toString());

    final var contentType = requestContext.getHeaders().get("Content-Type");

    if (requestContext.hasEntity() && contentType != null && contentType.contains("multipart/form-data")) {
      try {
        final var requestBodyBytes = requestContext.getEntityStream().readAllBytes();
        final var requestBody = new String(requestBodyBytes, StandardCharsets.UTF_8);
        LOGGER.info("Request Body: " + requestBody);
        requestContext.setEntityStream(new ByteArrayInputStream(requestBodyBytes));
      } catch (final Exception e) {
        LOGGER.warning("Error reading request body: " + e.getMessage());
      }

    }
  }

  @Override
  public void filter(final ContainerRequestContext containerRequestContext,
                     final ContainerResponseContext containerResponseContext) {
    LOGGER.info("Response Status: " + containerResponseContext.getStatus());
    LOGGER.info("Response Headers: " + containerResponseContext.getHeaders().toString());
    LOGGER.info("Response Body: " + containerResponseContext.getEntity());
  }
}
