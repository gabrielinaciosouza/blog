package com.gabriel.blog.infrastructure.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.wildfly.common.Assert.assertNotNull;
import static org.wildfly.common.Assert.assertTrue;

import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.fixtures.PostFixture;
import com.google.cloud.Timestamp;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

class PostModelTest {

  @Test
  void shouldReturnPostModelFromEntity() {
    var post = PostFixture.post();
    var postModel = PostModel.from(post);

    assertEquals(post.getTitle().getValue(), postModel.getTitle());
    assertEquals(post.getContent().getValue(), postModel.getContent());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(
        post.getCreationDate().getValue().atZone(ZoneId.systemDefault()).toEpochSecond(),
        post.getCreationDate().getValue().getNano()), postModel.getCreationDate());
    assertEquals(post.getSlug().getValue(), postModel.getSlug());
    assertEquals(post.getId().getValue(), postModel.getPostId());
    assertEquals(post.isDeleted(), postModel.isDeleted());
    assertNull(postModel.getDeletionDate());
    assertEquals(post.getCoverImage().getValue().toString(), postModel.getCoverImage());

    post = PostFixture.deletedPost();
    postModel = PostModel.from(post);

    assertEquals(post.getTitle().getValue(), postModel.getTitle());
    assertEquals(post.getContent().getValue(), postModel.getContent());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(
        post.getCreationDate().getValue().atZone(ZoneId.systemDefault()).toEpochSecond(),
        post.getCreationDate().getValue().getNano()), postModel.getCreationDate());
    assertEquals(post.getSlug().getValue(), postModel.getSlug());
    assertEquals(post.getId().getValue(), postModel.getPostId());
    assertEquals(post.isDeleted(), postModel.isDeleted());
    assertEquals(Timestamp.ofTimeSecondsAndNanos(
        post.getDeletionDate().getEpochSecond(),
        post.getDeletionDate().getNano()), postModel.getDeletionDate());
    assertEquals(post.getCoverImage().getValue().toString(), postModel.getCoverImage());
  }

  @Test
  void shouldReturnPostFromQueryDocumentSnapshot() {
    var postModel = new PostModel();
    postModel.setPostId("id");
    postModel.setTitle("title");
    postModel.setContent("content");
    postModel.setCreationDate(Timestamp.now());
    postModel.setSlug("slug");
    postModel.setDeleted(false);
    postModel.setDeletionDate(Timestamp.now());
    postModel.setCoverImage("https://example.com/image.jpg");

    var post = postModel.toDomain();

    assertEquals("id", post.getId().getValue());
    assertEquals("title", post.getTitle().getValue());
    assertEquals("content", post.getContent().getValue());
    assertEquals(CreationDate.now().toString(), post.getCreationDate().toString());
    assertEquals("slug", post.getSlug().getValue());
    assertFalse(post.isDeleted());
    assertNull(post.getDeletionDate());

    postModel.setDeleted(true);
    postModel.setDeletionDate(Timestamp.now());

    post = postModel.toDomain();

    assertEquals("id", post.getId().getValue());
    assertEquals("title", post.getTitle().getValue());
    assertEquals("content", post.getContent().getValue());
    assertEquals(CreationDate.now().toString(), post.getCreationDate().toString());
    assertEquals("slug", post.getSlug().getValue());
    assertTrue(post.isDeleted());
    assertNotNull(post.getDeletionDate());
  }
}