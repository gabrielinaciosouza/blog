package com.gabriel.blog.domain.valueobjects;

import java.time.LocalDateTime;

public record CreationDate(LocalDateTime date) {

	public static CreationDate now() {
		return new CreationDate(LocalDateTime.now());
	}
}
