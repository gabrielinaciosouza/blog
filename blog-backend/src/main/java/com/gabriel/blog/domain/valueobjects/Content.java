package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.Assertions;

public record Content(String value) implements Assertions {

	public Content {
		nonNull(value, "Tried to create a Content with a null value");
	}
}
