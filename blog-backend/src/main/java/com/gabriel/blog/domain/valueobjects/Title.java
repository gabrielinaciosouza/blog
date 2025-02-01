package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.AbstractValueObject;

public class Title extends AbstractValueObject {

	private final String value;

	public Title(final String value) {
		this.value = nonNull(value, "Tried to create a Title with a null value");
	}
}
