package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.Assertions;

public record Title(String value) implements Assertions {

	public Title {
		nonNull(value, "Tried to create a Title with a null value");
	}
}
