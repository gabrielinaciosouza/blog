package com.gabriel.blog.domain.exceptions;

public class DomainException extends RuntimeException {
	public DomainException(final String message) {
		super(message);
	}
}
