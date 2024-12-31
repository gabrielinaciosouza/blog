package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.exceptions.DomainException;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Title;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PostTest {

	private static final Id id = new Id("any id");
	private static final Title title = new Title("any title");
	private static final Content content = new Content("any content");
	private static final CreationDate creationDate = CreationDate.now();

	@Test
	void shouldCreateCorrectPost() {
		final var nullIdException = assertThrows(DomainException.class, () -> new Post(null, title, content, creationDate));
		assertEquals("Tried to create an entity with a null id", nullIdException.getMessage());

		final var nullTitleException = assertThrows(DomainException.class, () -> new Post(id, null, content, creationDate));
		assertEquals("Tried to create a post with a null title", nullTitleException.getMessage());

		final var nullContentException = assertThrows(DomainException.class, () -> new Post(id, title, null, creationDate));
		assertEquals("Tried to create a post with a null content", nullContentException.getMessage());

		final var nullCreationDateException = assertThrows(DomainException.class, () -> new Post(id, title, content, null));
		assertEquals("Tried to create a post with a null creationDate", nullCreationDateException.getMessage());

		assertDoesNotThrow(() -> new Post(id, title, content, creationDate));
	}

	@Test
	void shouldCreateCorrectToString() {
		final var post = new Post(id, title, content, creationDate);
		assertEquals("Post{id=Id[value=any id]title=Title[value=any title], content=Content[content=any content], creationDate=" + creationDate + "}", post.toString());
	}
}
