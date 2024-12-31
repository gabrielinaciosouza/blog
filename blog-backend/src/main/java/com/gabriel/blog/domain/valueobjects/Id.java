package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.Assertions;

public record Id(String value) implements Assertions {

	public Id {
		nonNull(value, "Tried to create an id with a null value");
	}
}
