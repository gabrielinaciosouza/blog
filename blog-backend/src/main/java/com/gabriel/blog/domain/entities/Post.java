package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.Entity;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Title;

public class Post extends Entity {

	private final Title title;
	private final Content content;
	private final CreationDate creationDate;

	public Post(final Id id, final Title title, final Content content, final CreationDate creationDate) {
		super(id);
		this.title = nonNull(title, "Tried to create a post with a null title");
		this.content = nonNull(content, "Tried to create a post with a null content");
		this.creationDate = nonNull(creationDate, "Tried to create a post with a null creationDate");
	}

	@Override
	public String toString() {
		return "Post{" +
				"id=" + getId() +
				"title=" + title +
				", content=" + content +
				", creationDate=" + creationDate +
				'}';
	}
}
