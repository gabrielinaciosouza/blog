package com.gabriel.blog.it;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.application.requests.CreatePostRequest;
import com.gabriel.blog.fixtures.CreationDateFixture;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.Test;

@QuarkusTest
class PostResourceIntegrationTest {

  @Inject
  private Firestore firestore;

  @Test
  void shouldCreatePost() throws InterruptedException {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("title", "content", "https://example.com/image.jpg"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(201)
        .body("postId", notNullValue())
        .body("title", equalTo("title"))
        .body("content", equalTo("content"))
        .body("slug", equalTo("title"))
        .body("creationDate", notNullValue())
        .body("coverImage", equalTo("https://example.com/image.jpg"));

    deleteTestPosts();
  }

  @Test
  void shouldNotCreatePostIfAlreadyExists() throws InterruptedException {
    final var timestamp = Timestamp.ofTimeSecondsAndNanos(
        CreationDateFixture.creationDate().getValue().getEpochSecond(),
        CreationDateFixture.creationDate().getValue().getNano());
    firestore.collection("posts").document("title").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "title",
        "creationDate", timestamp,
        "coverImage", "https://example.com/image.jpg",
        "isDeleted", false
    ));

    Thread.sleep(1000);

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("title", "content", "https://example.com/image.jpg"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(409)
        .body("message", equalTo("Post with slug title already exists"));

    deleteTestPosts();
  }

  @Test
  void shouldValidateTitle() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest(null, "content", "https://example.com/image.jpg"))
        .post("/posts")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(422)
        .body("message", equalTo("Tried to create a Title with a null value"));

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new CreatePostRequest("   ", "content", "https://example.com/image.jpg"))
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
        .body(new CreatePostRequest("title", null, "https://example.com/image.jpg"))
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
    final var timestamp = Timestamp.ofTimeSecondsAndNanos(
        CreationDateFixture.creationDate().getValue().getEpochSecond(),
        CreationDateFixture.creationDate().getValue().getNano());
    firestore.collection("posts").document("slug").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "slug",
        "creationDate", timestamp,
        "coverImage", "https://example.com/image.jpg",
        "isDeleted", false
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
        .body("creationDate", equalTo("2024-12-12 01:00"));

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
  void shouldFindNoPostsWhenNoneExist() {
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
        .body("totalCount", equalTo(0))
        .body("posts.size()", equalTo(0));
  }

  @Test
  void shouldFindPostsWithAscendingOrder() throws InterruptedException {
    createTestPosts();

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
        .body("totalCount", equalTo(2))
        .body("posts[0].postId", notNullValue())
        .body("posts[0].title", equalTo("title"))
        .body("posts[0].content", equalTo("content"))
        .body("posts[0].slug", equalTo("slug"))
        .body("posts[0].creationDate", equalTo("2024-12-12 01:00"))
        .body("posts[0].coverImage", equalTo("https://example.com/image.jpg"))
        .body("posts[1].postId", notNullValue())
        .body("posts[1].title", equalTo("title2"))
        .body("posts[1].content", equalTo("content"))
        .body("posts[1].slug", equalTo("slug"))
        .body("posts[1].creationDate", equalTo("2024-12-12 01:00"))
        .body("posts[1].coverImage", equalTo("https://example.com/image.jpg"));

    deleteTestPosts();
  }

  @Test
  void shouldFindPostsWithDescendingOrder() throws InterruptedException {
    createTestPosts();

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
        .body("totalCount", equalTo(2))
        .body("posts[0].postId", notNullValue())
        .body("posts[0].title", equalTo("title2"))
        .body("posts[0].content", equalTo("content"))
        .body("posts[0].slug", equalTo("slug"))
        .body("posts[0].creationDate", equalTo("2024-12-12 01:00"))
        .body("posts[0].coverImage", equalTo("https://example.com/image.jpg"))
        .body("posts[1].postId", notNullValue())
        .body("posts[1].title", equalTo("title"))
        .body("posts[1].content", equalTo("content"))
        .body("posts[1].slug", equalTo("slug"))
        .body("posts[1].creationDate", equalTo("2024-12-12 01:00"))
        .body("posts[1].coverImage", equalTo("https://example.com/image.jpg"));

    deleteTestPosts();
  }

  @Test
  void shouldReturnErrorForInvalidPage() {
    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(0, 10, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(400)
        .body("message", equalTo("Page must be greater than 0"));
  }

  @Test
  void shouldFindPostsWithDefaultSize() throws InterruptedException {
    createTestPosts();

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 0, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("totalCount", equalTo(2))
        .body("posts.size()", equalTo(2));

    deleteTestPosts();
  }

  @Test
  void shouldFindPostsWithDefaultSortOrder() throws InterruptedException {
    createTestPosts();

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 0, PostRepository.SortBy.title, null))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("totalCount", equalTo(2))
        .body("posts.size()", equalTo(2));

    deleteTestPosts();
  }

  @Test
  void shouldFindPostsWithDefaultSortBy() throws InterruptedException {
    createTestPosts();

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 0, null, PostRepository.SortOrder.DESCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("totalCount", equalTo(2))
        .body("posts.size()", equalTo(2));

    deleteTestPosts();
  }

  @Test
  void shouldReturnCorrectPageSize() throws InterruptedException {
    createTestPosts();

    given()
        .when()
        .header(new Header("content-type", MediaType.APPLICATION_JSON))
        .body(new PostRepository.FindPostsParams(1, 1, PostRepository.SortBy.title,
            PostRepository.SortOrder.DESCENDING))
        .post("/posts/find")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(200)
        .body("totalCount", equalTo(2))
        .body("posts.size()", equalTo(1));

    deleteTestPosts();
  }

  @Test
  void shouldDeletePost() throws InterruptedException, ExecutionException {
    final var timestamp = Timestamp.ofTimeSecondsAndNanos(
        CreationDateFixture.creationDate().getValue().getEpochSecond(),
        CreationDateFixture.creationDate().getValue().getNano());
    firestore.collection("posts").document("delete").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "slug",
        "creationDate", timestamp,
        "coverImage", "https://example.com/image.jpg",
        "isDeleted", false
    ));

    Thread.sleep(1000);

    given()
        .when()
        .delete("/posts/slug")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(204);

    Thread.sleep(1000);

    assertTrue(
        Objects.requireNonNull(
            firestore
                .collection("posts")
                .document("delete")
                .get()
                .get()
                .toObject(PostModel.class)).isDeleted());

    given()
        .when()
        .delete("/posts/slug")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(204);

    given()
        .when()
        .get("/posts/slug")
        .then()
        .log()
        .ifValidationFails(LogDetail.BODY)
        .statusCode(404)
        .body("message", equalTo("Post with slug slug not found"));

    deleteTestPosts();
  }

  private void createTestPosts() throws InterruptedException {
    final var timestamp = Timestamp.ofTimeSecondsAndNanos(
        CreationDateFixture.creationDate().getValue().getEpochSecond(),
        CreationDateFixture.creationDate().getValue().getNano());
    firestore.collection("posts").document("find").set(Map.of(
        "title", "title",
        "content", "content",
        "slug", "slug",
        "creationDate", timestamp,
        "coverImage", "https://example.com/image.jpg",
        "isDeleted", false));

    firestore.collection("posts").document("find2").set(Map.of(
        "title", "title2",
        "content", "content",
        "slug", "slug",
        "creationDate", timestamp,
        "coverImage", "https://example.com/image.jpg",
        "isDeleted", false));
    Thread.sleep(1000);
  }

  private void deleteTestPosts() throws InterruptedException {
    firestore.collection("posts").listDocuments().forEach(DocumentReference::delete);
    Thread.sleep(1000);
  }
}


