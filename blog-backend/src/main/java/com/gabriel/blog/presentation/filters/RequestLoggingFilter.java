package com.gabriel.blog.presentation.filters;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

/**
 * A JAX-RS filter that logs incoming HTTP requests.
 * It captures the HTTP method, request path, user agent, and client IP address.
 *
 * <p>Created by Gabriel Inacio de Souza on August 4th, 2025.</p>
 */
@Provider
public class RequestLoggingFilter implements ContainerRequestFilter {

  private static final Logger LOG = Logger.getLogger(RequestLoggingFilter.class);

  @Override
  public void filter(final ContainerRequestContext requestContext) {
    final String method = requestContext.getMethod();
    final String path = requestContext.getUriInfo().getRequestUri().getPath();
    final String userAgent = requestContext.getHeaderString("User-Agent");
    String ip = requestContext.getHeaderString("X-Forwarded-For");
    if (ip != null) {
      // Extract only the first IP address from the comma-separated list
      ip = ip.split(",")[0].trim();
    } else {
      ip = requestContext.getHeaderString("X-Real-IP");
      if (ip == null) {
        ip = "unknown";
      }
    }

    LOG.infof("Incoming request: %s %s | IP: %s | UA: %s", method, path, ip, sanitizedUserAgent);
  }

  /**
   * Sanitizes the user agent string by removing control characters and truncating to 200 chars.
   */
  private String sanitizeUserAgent(String userAgent) {
    if (userAgent == null) {
      return "unknown";
    }
    // Remove control characters (including newlines, carriage returns, etc.)
    String sanitized = userAgent.replaceAll("[\\p{Cntrl}]", "");
    // Truncate to 200 characters
    if (sanitized.length() > 200) {
      sanitized = sanitized.substring(0, 200) + "...";
    }
    return sanitized;
  }
}
