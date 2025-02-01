package com.gabriel.blog.domain;

import com.gabriel.blog.domain.exceptions.DomainException;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.jboss.logging.Logger;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

public interface DomainObject {

    boolean isEquals(Object o);

    int getHashCode();

    default String stringify() {
        return getClass().getSimpleName() + " " + reflectionToString(this, ToStringStyle.JSON_STYLE);
    }

    default Logger logger() {
        return Logger.getLogger(getClass());
    }

    default String[] excludeFieldsFromEquality(String... fields) {
        return fields;
    }

    default <T> T nonNull(final T obj, final String message) {
        if (obj == null) {
            logger().error(message);
            throw new DomainException(message);
        }
        return obj;
    }


}
