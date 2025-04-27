package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.google.firebase.ErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FirebaseAuthUserRepositoryTest {

  private FirebaseAuth firebaseAuth;
  private FirebaseAuthUserRepository repository;

  @BeforeEach
  void setUp() {
    firebaseAuth = mock(FirebaseAuth.class);
    repository = new FirebaseAuthUserRepository(firebaseAuth);
  }

  @Nested
  class FindByEmail {
    @Test
    void returnsUserWhenFound() throws Exception {
      final var email = new Email("user@example.com");
      final var userRecord = mock(UserRecord.class);
      when(userRecord.getUid()).thenReturn("uid123");
      when(userRecord.getDisplayName()).thenReturn("Test User");
      when(userRecord.getPhotoUrl()).thenReturn("http://img");
      Map<String, Object> claims = new HashMap<>();
      claims.put("role", "USER");
      when(userRecord.getCustomClaims()).thenReturn(claims);
      when(firebaseAuth.getUserByEmail(email.getEmail())).thenReturn(userRecord);

      Optional<User> result = repository.findByEmail(email);

      assertTrue(result.isPresent());
      User user = result.get();
      assertEquals("uid123", user.getId().getValue());
      assertEquals(email, user.getEmail());
      assertEquals(User.Role.USER, user.getRole());
      assertEquals("Test User", user.getName().getValue());
      assertEquals("http://img", user.getPictureUrl().getValue().toString());
    }

    @Test
    void returnsEmptyWhenUserNotFound() throws Exception {
      var email = new Email("notfound@example.com");
      when(firebaseAuth.getUserByEmail(email.getEmail()))
          .thenThrow(new FirebaseAuthException(ErrorCode.NOT_FOUND, "msg", null, null, null));
      Optional<User> result = repository.findByEmail(email);
      assertTrue(result.isEmpty());
    }

    @Test
    void throwsRepositoryExceptionOnOtherErrors() throws Exception {
      var email = new Email("error@example.com");
      when(firebaseAuth.getUserByEmail(email.getEmail())).thenThrow(new RuntimeException("fail"));
      assertThrows(RepositoryException.class, () -> repository.findByEmail(email));
    }
  }

  @Nested
  class Save {
    @Test
    void savesUserSuccessfully() throws Exception {
      var user = new User(new Id("uid123"), new Email("user@example.com"), User.Role.USER,
          new Name("Test User"),
          new Image("http://img"));
      var userRecord = mock(UserRecord.class);
      when(userRecord.getUid()).thenReturn("uid123");
      when(userRecord.getCustomClaims()).thenReturn(new HashMap<>());
      when(firebaseAuth.createUser(any(UserRecord.CreateRequest.class))).thenReturn(userRecord);

      doNothing().when(firebaseAuth).setCustomUserClaims(eq("uid123"), any(Map.class));

      assertDoesNotThrow(() -> repository.save(user));
      verify(firebaseAuth).createUser(any(UserRecord.CreateRequest.class));
      verify(firebaseAuth).setCustomUserClaims(eq("uid123"), any(Map.class));
    }

    @Test
    void throwsRepositoryExceptionOnError() throws Exception {
      var user = new User(new Id("uid123"), new Email("user@example.com"), User.Role.USER,
          new Name("Test User"),
          new Image("http://img"));
      when(firebaseAuth.createUser(any(UserRecord.CreateRequest.class))).thenThrow(
          new RuntimeException("fail"));
      assertThrows(RepositoryException.class, () -> repository.save(user));
    }
  }
}
