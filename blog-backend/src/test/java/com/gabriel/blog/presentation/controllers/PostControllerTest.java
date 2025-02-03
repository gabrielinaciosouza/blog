package com.gabriel.blog.presentation.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import com.gabriel.blog.application.requests.CreatePostRequest;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PostControllerTest {

//  @Test
//  void shouldCreatePost() {
//    final var postRequest = PostFixture.postRequest();
//    given()
//        .when()
//        .header(new Header("content-type", MediaType.APPLICATION_JSON))
//        .body(postRequest)
//        .post("/posts")
//        .then()
//        .log()
//        .ifValidationFails(LogDetail.BODY)
//        .statusCode(200)
//        .body("postId", notNullValue())
//        .body("title", equalTo(postRequest.title()))
//        .body("content", equalTo(postRequest.content()))
//        .body("creationDate", equalTo(LocalDate.now().toString()));
//  }
//
//  @Test
//  void shouldValidateTitle() {
//    given()
//        .when()
//        .header(new Header("content-type", MediaType.APPLICATION_JSON))
//        .body(new CreatePostRequest(null, "content"))
//        .post("/posts")
//        .then()
//        .log()
//        .ifValidationFails(LogDetail.BODY)
//        .statusCode(422)
//        .body("message", equalTo("Tried to create a Title with a null value"));
//  }
//
//  @Test
//  void shouldValidateContent() {
//    given()
//        .when()
//        .header(new Header("content-type", MediaType.APPLICATION_JSON))
//        .body(new CreatePostRequest("title", null))
//        .post("/posts")
//        .then()
//        .log()
//        .ifValidationFails(LogDetail.BODY)
//        .statusCode(422)
//        .body("message", equalTo("Tried to create a Content with a null value"));
//  }

  @Test
  void shouldValidateRequest() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("title", null))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Tried to create a Post with null request"));
  }
}


