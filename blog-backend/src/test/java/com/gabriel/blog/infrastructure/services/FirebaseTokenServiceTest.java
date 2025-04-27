package com.gabriel.blog.infrastructure.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.infrastructure.exceptions.ServiceException;
import com.google.firebase.ErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FirebaseTokenServiceTest {

  private final FirebaseAuth firebaseAuth = mock(FirebaseAuth.class);
  private final FirebaseTokenService service = new FirebaseTokenService(firebaseAuth);

  @Nested
  @DisplayName("decode()")
  class DecodeTests {

    @Test
    @DisplayName("should decode token successfully")
    void shouldDecodeTokenSuccessfully() throws FirebaseAuthException {
      final var mockToken = mock(FirebaseToken.class);
      when(mockToken.getUid()).thenReturn("user123");
      when(mockToken.getEmail()).thenReturn("user@example.com");
      when(mockToken.getName()).thenReturn("User Name");
      when(mockToken.getPicture()).thenReturn("http://example.com/picture.jpg");
      when(firebaseAuth.verifyIdToken("validToken")).thenReturn(mockToken);

      final var decodedToken = service.decode("validToken");

      assertEquals("user123", decodedToken.id());
      assertEquals("user@example.com", decodedToken.email());
      assertEquals("User Name", decodedToken.name());
      assertEquals("http://example.com/picture.jpg", decodedToken.pictureUrl());
    }

    @Test
    @DisplayName("should throw ServiceException when token is invalid")
    void shouldThrowServiceExceptionWhenTokenIsInvalid() throws FirebaseAuthException {
      when(firebaseAuth.verifyIdToken("invalidToken")).thenThrow(new FirebaseAuthException(
          ErrorCode.ALREADY_EXISTS, "Invalid token", null, null, null));

      final var exception =
          assertThrows(ServiceException.class, () -> service.decode("invalidToken"));

      assertEquals("Failed to decode token", exception.getMessage());
    }
  }

  @Nested
  @DisplayName("generate()")
  class GenerateTests {

    @Test
    @DisplayName("should generate token successfully")
    void shouldGenerateTokenSuccessfully() throws FirebaseAuthException {
      final var mockUserRecord = mock(UserRecord.class);
      when(mockUserRecord.getUid()).thenReturn("user123");
      when(firebaseAuth.getUserByEmail("user@example.com")).thenReturn(mockUserRecord);
      when(firebaseAuth.createCustomToken("user123")).thenReturn("customToken");

      final var token = service.generate(new Email("user@example.com"));

      assertEquals("customToken", token);
    }

    @Test
    @DisplayName("should throw ServiceException when user is not found")
    void shouldThrowServiceExceptionWhenUserIsNotFound() throws FirebaseAuthException {
      when(firebaseAuth.getUserByEmail("nonexistent@example.com")).thenThrow(
          new FirebaseAuthException(ErrorCode.UNAUTHENTICATED, "User not found", null, null, null));

      final var exception = assertThrows(ServiceException.class,
          () -> service.generate(new Email("nonexistent@example.com")));

      assertEquals("Failed to generate token", exception.getMessage());
    }
  }
}