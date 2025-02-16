package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.CreatePostRequest;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.Firestore;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PostResourceIntegrationTest {

  @Inject
  private Firestore firestore;

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
        .body("slug", equalTo("title"))
        .body("creationDate", equalTo(LocalDate.now().toString()));
  }

  @Test
  void shouldNotCreatePostIfAlreadyExists() throws InterruptedException {
    firestore.collection("posts").document("title").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "title",
        "creationDate", LocalDate.now().toString()
    ));

    Thread.sleep(1000);

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("title", "content"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(409)
        .body("message", equalTo("Post with slug title already exists"));

    firestore.collection("posts").document("title").delete();
    Thread.sleep(1000);
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

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("   ", "content"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(422)
        .body("message", equalTo("Tried to create a Title with a blank value"));
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

  @Test
  void shouldGetPostBySlug() throws InterruptedException {
    firestore.collection("posts").document("slug").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "slug",
        "creationDate", Timestamp.now()
    ));

    Thread.sleep(1000);

    given()
        .when()
        .get("/posts/slug")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("postId", notNullValue())
        .body("title", equalTo("title"))
        .body("content", equalTo("content"))
        .body("slug", equalTo("slug"))
        .body("creationDate", equalTo(LocalDate.now().toString()));

    firestore.collection("posts").document("slug").delete();
    Thread.sleep(1000);
  }

  @Test
  void shouldReturn404WhenPostNotFound() {
    given()
        .when()
        .get("/posts/not-found")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(404)
        .body("message", equalTo("Post with slug not-found not found"));
  }

  @Test
  void shouldFindPosts() throws InterruptedException {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.ASCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("size()", equalTo(0));

    firestore.collection("posts").document("find").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "slug",
        "creationDate", Timestamp.now()));

    firestore.collection("posts").document("find2").set(Map.of(
        "title", "title2",
        "content", "content",
        "slug", "slug",
        "creationDate", Timestamp.now()));

    Thread.sleep(1000);

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.ASCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("[0].postId", notNullValue())
        .body("[0].title", equalTo("title"))
        .body("[0].content", equalTo("content"))
        .body("[0].slug", equalTo("slug"))
        .body("[0].creationDate", equalTo(LocalDate.now().toString()))
        .body("[1].postId", notNullValue())
        .body("[1].title", equalTo("title2"))
        .body("[1].content", equalTo("content"))
        .body("[1].slug", equalTo("slug"))
        .body("[1].creationDate", equalTo(LocalDate.now().toString()));

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("[0].postId", notNullValue())
        .body("[0].title", equalTo("title2"))
        .body("[0].content", equalTo("content"))
        .body("[0].slug", equalTo("slug"))
        .body("[0].creationDate", equalTo(LocalDate.now().toString()))
        .body("[1].postId", notNullValue())
        .body("[1].title", equalTo("title"))
        .body("[1].content", equalTo("content"))
        .body("[1].slug", equalTo("slug"))
        .body("[1].creationDate", equalTo(LocalDate.now().toString()));

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(0, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))
        .when()
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(500)
        .body("message", equalTo("Page must be greater than 0"));

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 0, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))
        .when()
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("size()", equalTo(2));



    firestore.collection("posts").document("find").delete();
    Thread.sleep(1000);
  }
}


