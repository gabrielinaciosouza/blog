package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.gabriel.blog.application.requests.CreatePostRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PostResourceTest {

  @Test
  void shouldCreatePost() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("title", "content"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(201)
        .body("postId", notNullValue())
        .body("title", equalTo("title"))
        .body("content", equalTo("content"))
        .body("creationDate", equalTo(LocalDate.now().toString()));
  }

  @Test
  void shouldValidateTitle() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest(null, "content"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(422)
        .body("message", equalTo("Tried to create a Title with a null value"));
  }

  @Test
  void shouldValidateContent() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("title", null))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(422)
        .body("message", equalTo("Tried to create a Content with a null value"));
  }

  @Test
  void shouldValidateRequest() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Tried to create a Post with null request"));
  }
}


