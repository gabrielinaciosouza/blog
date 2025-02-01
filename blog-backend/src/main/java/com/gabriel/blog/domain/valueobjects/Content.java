package com.gabriel.blog.domain.valueobjects;

import com.gabriel.blog.domain.AbstractValueObject;

public class Content extends AbstractValueObject {

    final String value;

    public Content(final String value) {
        this.value = nonNull(value, "Tried to create a Content with a null value");
    }

}
