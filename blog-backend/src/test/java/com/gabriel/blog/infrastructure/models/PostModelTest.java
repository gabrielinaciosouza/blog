package com.gabriel.blog.infrastructure.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.fixtures.PostFixture;
import com.google.cloud.Timestamp;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class PostModelTest {

  @Test
  void shouldReturnPostModelFromEntity() {
    final var post = PostFixture.post();
    final var postModel = PostModel.from(post);

    assertEquals(post.getTitle().getValue(), postModel.getTitle());
    assertEquals(post.getContent().getValue(), postModel.getContent());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(
        post.getCreationDate().getValue().atStartOfDay(ZoneId.systemDefault()).toEpochSecond(),
        0), postModel.getCreationDate());
    assertEquals(post.getSlug().getValue(), postModel.getSlug());
    assertEquals(post.getId().getValue(), postModel.getPostId());
  }

  @Test
  void shouldReturnPostFromQueryDocumentSnapshot() {
    final var postModel = new PostModel();
    postModel.setPostId("id");
    postModel.setTitle("title");
    postModel.setContent("content");
    postModel.setCreationDate(Timestamp.now());
    postModel.setSlug("slug");


    final var post = postModel.toDomain();

    assertEquals("id", post.getId().getValue());
    assertEquals("title", post.getTitle().getValue());
    assertEquals("content", post.getContent().getValue());
    assertEquals(CreationDate.now().toString(), post.getCreationDate().toString());
    assertEquals("slug", post.getSlug().getValue());
  }
}