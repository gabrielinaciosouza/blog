package com.gabriel.blog.domain;

import com.gabriel.blog.domain.exceptions.DomainException;

public interface Assertions {

	default  <T> T nonNull(final T obj, final String message) {
		if (obj == null) {
			throw new DomainException(message);
		}
		return obj;
	}
}
