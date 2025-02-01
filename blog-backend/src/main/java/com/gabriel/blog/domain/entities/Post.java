package com.gabriel.blog.domain.entities;

import com.gabriel.blog.domain.AbstractEntity;
import com.gabriel.blog.domain.valueobjects.Content;
import com.gabriel.blog.domain.valueobjects.CreationDate;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Title;

public class Post extends AbstractEntity {

    private final Title title;
    private final Content content;
    private final CreationDate creationDate;

    public Post(final Id id, final Title title, final Content content, final CreationDate creationDate) {
        super(id);
        this.title = nonNull(title, "Tried to create a Post with a null title");
        this.content = nonNull(content, "Tried to create a Post with a null content");
        this.creationDate = nonNull(creationDate, "Tried to create a Post with a null creationDate");
    }
}
