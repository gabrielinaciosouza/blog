package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.exceptions.ValidationException;
import com.gabriel.blog.application.repositories.UserRepository;
import com.gabriel.blog.application.services.TokenService;
import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class AuthenticationUseCaseTest {

  private UserRepository userRepository;
  private TokenService tokenService;
  private AuthenticationUseCase useCase;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    tokenService = mock(TokenService.class);
    useCase = new AuthenticationUseCase(userRepository, tokenService);
  }

  @Test
  void shouldAuthenticateExistingUser() {
    final String idToken = "valid-token";
    final var decodedToken =
        new TokenService.DecodedToken("123", "test@example.com", "Test User", "http://img");
    final var email = new Email("test@example.com");
    final var existingUser = new User(new Id("123"), email, User.Role.USER, new Name("Test User"),
        new Image("http://img"));

    when(tokenService.decode(idToken)).thenReturn(decodedToken);
    when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
    when(tokenService.generate(email)).thenReturn("generated-token");

    final var response = useCase.authenticateWithGoogle(idToken);

    assertNotNull(response);
    assertEquals("generated-token", response.authToken());
    assertEquals("123", response.userId());
    assertEquals("USER", response.role());
    assertEquals("Test User", response.name());
    assertEquals("test@example.com", response.email());
    assertEquals("http://img", response.pictureUrl());
    verify(userRepository).findByEmail(email);
    verify(userRepository, never()).save(ArgumentMatchers.any());
  }

  @Test
  void shouldCreateUserIfNotFound() {
    final String idToken = "new-token";
    final var decodedToken =
        new TokenService.DecodedToken("456", "new@example.com", "New User", null);
    final var email = new Email("new@example.com");

    when(tokenService.decode(idToken)).thenReturn(decodedToken);
    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    when(tokenService.generate(email)).thenReturn("new-token");

    final var response = useCase.authenticateWithGoogle(idToken);

    assertNotNull(response);
    assertEquals("new-token", response.authToken());
    assertEquals("456", response.userId());
    assertEquals("USER", response.role());
    assertEquals("New User", response.name());
    assertEquals("new@example.com", response.email());
    assertNotNull(response.pictureUrl());
    verify(userRepository).save(ArgumentMatchers.any(User.class));
  }

  @Test
  void shouldThrowValidationExceptionIfIdTokenIsNullOrEmpty() {
    Assertions.assertThrows(ValidationException.class, () -> useCase.authenticateWithGoogle(null));
    Assertions.assertThrows(ValidationException.class, () -> useCase.authenticateWithGoogle(""));
  }
}