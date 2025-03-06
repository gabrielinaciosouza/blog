package com.gabriel.blog.infrastructure.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.fixtures.CommentFixture;
import com.gabriel.blog.infrastructure.exceptions.RepositoryException;
import com.gabriel.blog.infrastructure.models.CommentModel;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FirestoreCommentRepositoryTest {

  private Firestore firestore;
  private CollectionReference collectionReference;
  private DocumentReference documentReference;
  private ApiFuture apiFuture;
  private DocumentSnapshot documentSnapshot;
  private FirestoreCommentRepository firestoreCommentRepository;

  @BeforeEach
  void setUp() {
    firestore = mock(Firestore.class);
    collectionReference = mock(CollectionReference.class);
    documentReference = mock(DocumentReference.class);
    apiFuture = mock(ApiFuture.class);
    documentSnapshot = mock(DocumentSnapshot.class);
    firestoreCommentRepository = new FirestoreCommentRepository(firestore);
  }

  @Nested
  class SaveTests {

    @Test
    void shouldSaveCommentCorrectly() throws ExecutionException, InterruptedException {
      when(firestore.collection("comments")).thenReturn(collectionReference);
      when(collectionReference.document("any")).thenReturn(documentReference);
      when(documentReference.set(any(CommentModel.class))).thenReturn(apiFuture);

      final var comment = CommentFixture.comment();
      firestoreCommentRepository.save(comment);

      verify(firestore).collection("comments");
      verify(collectionReference).document("any");
      verify(documentReference).set(any(CommentModel.class));
      verify(apiFuture).get();
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("comments")).thenReturn(collectionReference);
      when(collectionReference.add(any(CommentModel.class))).thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);

      final var comment = CommentFixture.comment();
      assertThrows(RepositoryException.class, () -> firestoreCommentRepository.save(comment));
    }
  }

  @Nested
  class GetCommentsByIdTests {

    @Test
    void shouldGetCommentsByIdCorrectly() throws ExecutionException, InterruptedException {
      when(firestore.collection("comments")).thenReturn(collectionReference);
      when(collectionReference.document("any")).thenReturn(documentReference);
      when(documentReference.get()).thenReturn(apiFuture);
      when(apiFuture.get()).thenReturn(documentSnapshot);
      when(documentSnapshot.exists()).thenReturn(true);
      when(documentSnapshot.toObject(CommentModel.class)).thenReturn(
          CommentModel.from(CommentFixture.comment()));

      final var comments = firestoreCommentRepository.getCommentsById(List.of(new Id("any")));

      verify(firestore).collection("comments");
      verify(collectionReference).document("any");
      verify(documentReference).get();
      verify(apiFuture).get();
      assertEquals(comments.getFirst(), CommentFixture.comment());
    }

    @Test
    void shouldThrowRepositoryExceptionOnThreadFailure()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("comments")).thenReturn(collectionReference);
      when(collectionReference.document("any")).thenReturn(documentReference);
      when(documentReference.get()).thenReturn(apiFuture);
      when(apiFuture.get()).thenThrow(InterruptedException.class);

      assertThrows(RepositoryException.class,
          () -> firestoreCommentRepository.getCommentsById(List.of(new Id("any"))));
    }

    @Test
    void shouldNotAddCommentToCommentsListIfDocumentDoesNotExist()
        throws ExecutionException, InterruptedException {
      when(firestore.collection("comments")).thenReturn(collectionReference);
      when(collectionReference.document("any")).thenReturn(documentReference);
      when(documentReference.get()).thenReturn(apiFuture);
      when(apiFuture.get()).thenReturn(documentSnapshot);
      when(documentSnapshot.exists()).thenReturn(false);

      final var comments = firestoreCommentRepository.getCommentsById(List.of(new Id("any")));

      verify(firestore).collection("comments");
      verify(collectionReference).document("any");
      verify(documentReference).get();
      verify(apiFuture).get();
      assertEquals(comments.size(), 0);
    }
  }
}