package com.gabriel.blog.presentation.filters;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggingFilterTest {

  private LoggingFilter loggingFilter;
  private ContainerRequestContext requestContext;
  private ContainerResponseContext responseContext;

  @BeforeEach
  void setUp() {
    loggingFilter = new LoggingFilter();
    requestContext = mock(ContainerRequestContext.class);
    responseContext = mock(ContainerResponseContext.class);
  }

  @Test
  void shouldLogRequestDetails() throws Exception {
    when(requestContext.getMethod()).thenReturn("POST");
    when(requestContext.getUriInfo()).thenReturn(mock(UriInfo.class));
    when(requestContext.getUriInfo().getRequestUri()).thenReturn(
        new java.net.URI("http://localhost:8080/test"));
    when(requestContext.getHeaders()).thenReturn(new MultivaluedHashMap<>() {{
      put("Content-Type", List.of("application/json"));
    }});
    when(requestContext.hasEntity()).thenReturn(true);
    when(requestContext.getEntityStream()).thenReturn(
        new ByteArrayInputStream("test body".getBytes(StandardCharsets.UTF_8)));

    loggingFilter.filter(requestContext);

    verify(requestContext).setEntityStream(any(ByteArrayInputStream.class));
    verify(requestContext).getMethod();
    verify(requestContext, times(2)).getUriInfo();
    verify(requestContext, times(2)).getHeaders();
    verify(requestContext).hasEntity();
  }

  @Test
  void shouldLogResponseDetails() {
    when(responseContext.getStatus()).thenReturn(200);
    when(responseContext.getHeaders()).thenReturn(new MultivaluedHashMap<>());
    when(responseContext.getEntity()).thenReturn("test response");

    loggingFilter.filter(requestContext, responseContext);

    verify(responseContext).getStatus();
    verify(responseContext).getHeaders();
    verify(responseContext).getEntity();
  }

  @Test
  void shouldLogWarningWhenErrorReadingRequestBody() throws Exception {
    when(requestContext.getMethod()).thenReturn("POST");
    when(requestContext.getUriInfo()).thenReturn(mock(UriInfo.class));
    when(requestContext.getUriInfo().getRequestUri()).thenReturn(
        new java.net.URI("http://localhost:8080/test"));
    when(requestContext.getHeaders()).thenReturn(new MultivaluedHashMap<>());
    when(requestContext.hasEntity()).thenReturn(true);
    when(requestContext.getEntityStream()).thenThrow(new RuntimeException("Test Exception"));

    loggingFilter.filter(requestContext);

    verify(requestContext).getMethod();
    verify(requestContext, times(2)).getUriInfo();
    verify(requestContext, times(2)).getHeaders();
    verify(requestContext).hasEntity();
  }

  @Test
  void shouldNotLogRequestBodyWhenContentTypeIsMultipartFormData() throws Exception {
    when(requestContext.getMethod()).thenReturn("POST");
    when(requestContext.getUriInfo()).thenReturn(mock(UriInfo.class));
    when(requestContext.getUriInfo().getRequestUri()).thenReturn(
        new java.net.URI("http://localhost:8080/test"));
    when(requestContext.getHeaders()).thenReturn(new MultivaluedHashMap<>() {{
      put("Content-Type", List.of("multipart/form-data"));
    }});
    when(requestContext.hasEntity()).thenReturn(true);
    when(requestContext.getEntityStream()).thenReturn(
        new ByteArrayInputStream("test body".getBytes(StandardCharsets.UTF_8)));

    loggingFilter.filter(requestContext);

    verify(requestContext, never()).setEntityStream(any(ByteArrayInputStream.class));
    verify(requestContext).getMethod();
    verify(requestContext, times(2)).getUriInfo();
    verify(requestContext, times(2)).getHeaders();
    verify(requestContext).hasEntity();
  }

  @Test
  void shouldNotLogRequestBodyWhenContentTypeIsNull() throws Exception {
    when(requestContext.getMethod()).thenReturn("POST");
    when(requestContext.getUriInfo()).thenReturn(mock(UriInfo.class));
    when(requestContext.getUriInfo().getRequestUri()).thenReturn(
        new java.net.URI("http://localhost:8080/test"));
    when(requestContext.getHeaders()).thenReturn(new MultivaluedHashMap<>());
    when(requestContext.hasEntity()).thenReturn(true);
    when(requestContext.getEntityStream()).thenReturn(
        new ByteArrayInputStream("test body".getBytes(StandardCharsets.UTF_8)));

    loggingFilter.filter(requestContext);

    verify(requestContext, never()).setEntityStream(any(ByteArrayInputStream.class));
    verify(requestContext).getMethod();
    verify(requestContext, times(2)).getUriInfo();
    verify(requestContext, times(2)).getHeaders();
    verify(requestContext).hasEntity();
  }

  @Test
  void shouldLogRequestBodyWhenContentTypeDoesNotContainMultipartFormData() throws Exception {
    when(requestContext.getMethod()).thenReturn("POST");
    when(requestContext.getUriInfo()).thenReturn(mock(UriInfo.class));
    when(requestContext.getUriInfo().getRequestUri()).thenReturn(
        new java.net.URI("http://localhost:8080/test"));
    when(requestContext.getHeaders()).thenReturn(new MultivaluedHashMap<>() {{
      put("Content-Type", List.of("application/json"));
    }});
    when(requestContext.hasEntity()).thenReturn(true);
    when(requestContext.getEntityStream()).thenReturn(
        new ByteArrayInputStream("test body".getBytes(StandardCharsets.UTF_8)));

    loggingFilter.filter(requestContext);

    verify(requestContext).setEntityStream(any(ByteArrayInputStream.class));
    verify(requestContext).getMethod();
    verify(requestContext, times(2)).getUriInfo();
    verify(requestContext, times(2)).getHeaders();
    verify(requestContext).hasEntity();
  }
}