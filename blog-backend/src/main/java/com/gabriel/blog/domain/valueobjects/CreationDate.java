package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.Assertions;

import java.time.LocalDate;

public record CreationDate(LocalDate value) implements Assertions {

	public CreationDate {
		nonNull(value, "Tried to create a CreationDate with a null value");
	}

	public static CreationDate now() {
		return new CreationDate(LocalDate.now());
	}
}
