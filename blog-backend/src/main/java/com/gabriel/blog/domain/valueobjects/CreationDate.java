package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.AbstractValueObject;

import java.time.LocalDate;

public class CreationDate extends AbstractValueObject {

	private final LocalDate value;

	public CreationDate(final LocalDate value) {
		this.value = nonNull(value, "Tried to create a CreationDate with a null value");
	}

	public static CreationDate now() {
		return new CreationDate(LocalDate.now());
	}

	public LocalDate getValue() {
		return value;
	}
}
