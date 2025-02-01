package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.AbstractValueObject;

public class Id extends AbstractValueObject {

	private final String value;

	public Id(final String value) {
		this.value = nonNull(value, "Tried to create an id with a null value");
	}

	public String getValue() {
		return value;
	}
}
