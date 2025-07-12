package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import org.junit.jupiter.api.Test;

@QuarkusTest
class AuthenticationResourceIntegrationTest {

  private static final String userUid = "test-user-id";
  @Inject
  private FirebaseAuth firebaseAuth;

  private static String getIdToken() throws IOException,
      InterruptedException {
    try (final HttpClient client = HttpClient.newHttpClient()) {
      final HttpRequest loginRequest = HttpRequest.newBuilder()
          .uri(URI.create(
              "http://localhost:9099/identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=fake-api-key"))
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString("""
                  {
                    "email": "test@email.com",
                    "password": "123456",
                    "returnSecureToken": true
                  }
              """))
          .build();

      final String loginResponse =
          client.send(loginRequest, HttpResponse.BodyHandlers.ofString()).body();


      final ObjectMapper mapper = new ObjectMapper();
      final JsonNode json = mapper.readTree(loginResponse);
      return json.get("idToken").asText();
    }
  }

  private void createTestUser() {
    try {
      firebaseAuth.createUser(new UserRecord.CreateRequest()
          .setEmail("test@email.com")
          .setPassword("123456")
          .setDisplayName("Test User")
          .setUid(userUid));

      firebaseAuth.setCustomUserClaims(userUid, Map.of("role", "user"));
    } catch (final FirebaseAuthException e) {
      // do nothing if the user already exists
    }
  }

  @Test
  void shouldAuthenticateWithGoogleSuccessfully() throws IOException, InterruptedException {
    createTestUser();
    final var idToken = getIdToken();
    given()
        .when()
        .header("content-type", MediaType.APPLICATION_JSON)
        .body("{\"idToken\": \"" + idToken + "\"}")
        .post("/auth/google")
        .then()
        .log().ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("authToken", notNullValue())
        .body("userId", equalTo(userUid))
        .body("role", equalTo("USER"));
  }

  @Test
  void shouldReturn400WhenIdTokenIsMissing() {
    given()
        .when()
        .header("content-type", MediaType.APPLICATION_JSON)
        .body("{}")
        .post("/auth/google")
        .then()
        .log().ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("ID token must not be null or empty"));
  }

  @Test
  void shouldReturn400WhenIdTokenIsEmpty() {
    given()
        .when()
        .header("content-type", MediaType.APPLICATION_JSON)
        .body("{\"idToken\": \"\"}")
        .post("/auth/google")
        .then()
        .log().ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("ID token must not be null or empty"));
  }
}

