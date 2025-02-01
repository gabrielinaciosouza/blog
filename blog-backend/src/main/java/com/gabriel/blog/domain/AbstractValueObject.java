package com.gabriel.blog.domain;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public abstract class AbstractValueObject implements DomainObject {

    @Override
    public int hashCode() {
        return getHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return isEquals(obj);
    }

    @Override
    public String toString() {
        return stringify();
    }

    @Override
    public boolean isEquals(Object object) {
        return reflectionEquals(this, object, excludeFieldsFromEquality());
    }

    @Override
    public int getHashCode() {
        return reflectionHashCode(this, excludeFieldsFromEquality());
    }
}
