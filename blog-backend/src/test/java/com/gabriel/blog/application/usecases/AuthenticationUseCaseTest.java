package com.gabriel.blog.application.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.UserRepository;
import com.gabriel.blog.application.services.TokenGenerator;
import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.fixtures.IdFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuthenticationUseCaseTest {

  private UserRepository userRepository;
  private TokenGenerator tokenGenerator;
  private AuthenticationUseCase useCase;

  @BeforeEach
  void setUp() {
    userRepository = mock(UserRepository.class);
    tokenGenerator = mock(TokenGenerator.class);
    useCase = new AuthenticationUseCase(userRepository, tokenGenerator);
  }

  @Test
  void shouldAuthenticateExistingUser() {
    final var email = new Email("test@example.com");
    final var existingUser = new User(IdFixture.withId("123"), email, User.Role.USER);

    when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
    when(tokenGenerator.generate(email)).thenReturn("generated-token");

    final var response = useCase.continueWithGoogle(email);

    assertNotNull(response);
    assertEquals("generated-token", response.authToken());
    assertEquals("123", response.userId());
    assertEquals("USER", response.role());
    verify(userRepository).findByEmail(email);
  }

  @Test
  void shouldCreateUserIfNotFound() {
    final var email = new Email("new@example.com");
    final var newUser = new User(IdFixture.withId("456"), email, User.Role.USER);

    when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
    when(userRepository.create(email)).thenReturn(newUser);
    when(tokenGenerator.generate(email)).thenReturn("new-token");

    final var response = useCase.continueWithGoogle(email);

    assertNotNull(response);
    assertEquals("new-token", response.authToken());
    assertEquals("456", response.userId());
    assertEquals("USER", response.role());
    verify(userRepository).create(email);
  }
}