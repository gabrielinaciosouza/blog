package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.application.repositories.PostRepository;
import com.gabriel.blog.domain.entities.Post;
import com.gabriel.blog.fixtures.PostFixture;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.PostModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FirestorePostRepositoryTest {

  private static final Post POST = PostFixture.post();

  private Firestore firestore;
  private CollectionReference collectionReference;
  private ApiFuture apiFuture;
  private Query query;
  private DocumentReference documentReference;
  private FirestorePostRepository firestorePostRepository;

  @BeforeEach
  void setUp() {
    firestore = mock(Firestore.class);
    collectionReference = mock(CollectionReference.class);
    apiFuture = mock(ApiFuture.class);
    query = mock(Query.class);
    documentReference = mock(DocumentReference.class);
    firestorePostRepository = new FirestorePostRepository(firestore);
  }

  @Test
  void shouldThrowRepositoryExceptionOnThreadFailure()
      throws ExecutionException, InterruptedException {
    when(firestore.collection("posts")).thenReturn(collectionReference);
    when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
    when(query.whereEqualTo("slug", POST.getSlug().getValue()))
        .thenReturn(query);
    when(query.get()).thenReturn(apiFuture);
    when(apiFuture.get()).thenThrow(InterruptedException.class);

    assertThrows(RepositoryException.class,
        () -> firestorePostRepository.findBySlug(POST.getSlug()));
  }

  @Nested
  class SaveTests {

    @Test
    void shouldSavePostCorrectly() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.add(any(PostModel.class)))
          .thenReturn(apiFuture);

      firestorePostRepository.save(POST);

      verify(firestore).collection("posts");
      verify(collectionReference).add(any(PostModel.class));
      verify(apiFuture).get();
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.add(any(PostModel.class)))
          .thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);

      assertThrows(RepositoryException.class, () -> firestorePostRepository.save(POST));
    }
  }

  @Nested
  class FindBySlugTests {

    @Test
    void shouldGetPostBySlug() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.whereEqualTo("slug", POST.getSlug().getValue()))
          .thenReturn(query);
      when(query.get()).thenReturn(apiFuture);

      final var querySnapshot = mock(QuerySnapshot.class);
      final var documentSnapshot = mock(QueryDocumentSnapshot.class);

      when(apiFuture.get()).thenReturn(querySnapshot);
      when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
      when(documentSnapshot.toObject(PostModel.class)).thenReturn(PostModel.from(POST));

      final var result = firestorePostRepository.findBySlug(POST.getSlug());

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("deleted", false);
      verify(query).whereEqualTo("slug", POST.getSlug().getValue());
      verify(query).get();
      verify(apiFuture).get();
      verify(querySnapshot).getDocuments();
      verify(documentSnapshot).toObject(PostModel.class);
      assertEquals(POST, result.get());
    }
  }

  @Nested
  class FindPostsTests {

    @Test
    void shouldFindPostsWithValidParams() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.orderBy("creationDate", Query.Direction.ASCENDING)).thenReturn(
          query);
      when(query.offset(0)).thenReturn(query);
      when(query.limit(10)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);

      final var querySnapshot = mock(QuerySnapshot.class);
      final var documentSnapshot = mock(QueryDocumentSnapshot.class);

      when(apiFuture.get()).thenReturn(querySnapshot);
      when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
      when(documentSnapshot.toObject(PostModel.class)).thenReturn(PostModel.from(POST));
      final var params =
          new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.creationDate,
              PostRepository.SortOrder.ASCENDING, false);

      final var result = firestorePostRepository.findPosts(params);

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("deleted", false);
      verify(query).orderBy("creationDate", Query.Direction.ASCENDING);
      verify(query).offset(0);
      verify(query).limit(10);
      verify(query).get();
      verify(apiFuture).get();
      verify(querySnapshot).getDocuments();
      verify(documentSnapshot).toObject(PostModel.class);
      assertEquals(List.of(POST), result);
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {

      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.orderBy("title", Query.Direction.DESCENDING)).thenReturn(query);
      when(query.offset(0)).thenReturn(query);
      when(query.limit(10)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);
      final var params =
          new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
              PostRepository.SortOrder.DESCENDING, false);
      assertThrows(RepositoryException.class, () -> firestorePostRepository.findPosts(params));
    }

    @Test
    void shouldThrowRepositoryExceptionWhenParamsAreNull() {
      assertThrows(RepositoryException.class, () -> firestorePostRepository.findPosts(null));
    }

    @Test
    void shouldThrowRepositoryExceptionWhenPageIsLessThan1() {
      assertThrows(RepositoryException.class, () -> firestorePostRepository.findPosts(
          new PostRepository.FindPostsParams(0, 10, PostRepository.SortBy.title,
              PostRepository.SortOrder.ASCENDING, false)));
    }

    @Test
    void shouldSetDefaultSizeWhenSizeIsLessThan1() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.orderBy("title", Query.Direction.ASCENDING)).thenReturn(query);
      when(query.offset(0)).thenReturn(query);
      when(query.limit(10)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);

      final var querySnapshot = mock(QuerySnapshot.class);
      final var documentSnapshot = mock(QueryDocumentSnapshot.class);

      when(apiFuture.get()).thenReturn(querySnapshot);
      when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
      when(documentSnapshot.toObject(PostModel.class)).thenReturn(PostModel.from(POST));
      final var params =
          new PostRepository.FindPostsParams(1, 0, PostRepository.SortBy.title,
              PostRepository.SortOrder.ASCENDING, false);

      final var result = firestorePostRepository.findPosts(params);

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("deleted", false);
      verify(query).orderBy("title", Query.Direction.ASCENDING);
      verify(query).offset(0);
      verify(query).limit(10);
      verify(query).get();
      verify(apiFuture).get();
      verify(querySnapshot).getDocuments();
      verify(documentSnapshot).toObject(PostModel.class);
      assertEquals(List.of(POST), result);
    }

    @Test
    void shouldSetDefaultSortByWhenSortByIsNull() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.orderBy("creationDate", Query.Direction.ASCENDING)).thenReturn(
          query);
      when(query.offset(0)).thenReturn(query);
      when(query.limit(10)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);

      final var querySnapshot = mock(QuerySnapshot.class);
      final var documentSnapshot = mock(QueryDocumentSnapshot.class);

      when(apiFuture.get()).thenReturn(querySnapshot);
      when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
      when(documentSnapshot.toObject(PostModel.class)).thenReturn(PostModel.from(POST));
      final var params =
          new PostRepository.FindPostsParams(1, 10, null,
              PostRepository.SortOrder.ASCENDING, false);

      final var result = firestorePostRepository.findPosts(params);

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("deleted", false);
      verify(query).orderBy("creationDate", Query.Direction.ASCENDING);
      verify(query).offset(0);
      verify(query).limit(10);
      verify(query).get();
      verify(apiFuture).get();
      verify(querySnapshot).getDocuments();
      verify(documentSnapshot).toObject(PostModel.class);
      assertEquals(List.of(POST), result);
    }

    @Test
    void shouldSetDefaultSortOrderWhenSortOrderIsNull()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.orderBy("title", Query.Direction.DESCENDING)).thenReturn(query);
      when(query.offset(0)).thenReturn(query);
      when(query.limit(10)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);

      final var querySnapshot = mock(QuerySnapshot.class);
      final var documentSnapshot = mock(QueryDocumentSnapshot.class);

      when(apiFuture.get()).thenReturn(querySnapshot);
      when(querySnapshot.getDocuments()).thenReturn(List.of(documentSnapshot));
      when(documentSnapshot.toObject(PostModel.class)).thenReturn(PostModel.from(POST));
      final var params =
          new PostRepository.FindPostsParams(1, 10, PostRepository.SortBy.title,
              null, false);

      final var result = firestorePostRepository.findPosts(params);

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("deleted", false);
      verify(query).orderBy("title", Query.Direction.DESCENDING);
      verify(query).offset(0);
      verify(query).limit(10);
      verify(query).get();
      verify(apiFuture).get();
      verify(querySnapshot).getDocuments();
      verify(documentSnapshot).toObject(PostModel.class);
      assertEquals(List.of(POST), result);
    }
  }

  @Nested
  class TotalCountTests {

    @Test
    void shouldReturnTotalCount() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);
      when(apiFuture.get()).thenReturn(mock(QuerySnapshot.class));

      firestorePostRepository.totalCount();

      verify(firestore).collection("posts");
      verify(collectionReference).whereEqualTo("deleted", false);
      verify(query).get();
      verify(apiFuture).get();
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.whereEqualTo("deleted", false)).thenReturn(query);
      when(query.get()).thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);

      assertThrows(RepositoryException.class, () -> firestorePostRepository.totalCount());
    }
  }

  @Nested
  class UpdateTests {

    @Test
    void shouldUpdatePostCorrectly() throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.document(POST.getId().getValue())).thenReturn(documentReference);
      when(documentReference.set(any(PostModel.class))).thenReturn(apiFuture);

      final var updatedPost = firestorePostRepository.update(POST);

      verify(firestore).collection("posts");
      verify(collectionReference).document(POST.getId().getValue());
      verify(documentReference).set(any(PostModel.class));
      verify(apiFuture).get();
      assertEquals(POST, updatedPost);
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("posts")).thenReturn(collectionReference);
      when(collectionReference.document(POST.getId().getValue())).thenReturn(documentReference);
      when(documentReference.set(any(PostModel.class))).thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);

      assertThrows(RepositoryException.class, () -> firestorePostRepository.update(POST));
    }
  }
}