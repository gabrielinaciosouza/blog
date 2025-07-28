package com.gabriel.blog.application.qualifiers;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * This qualifier is used to indicate that a bean should be injected with a random generator.
 * It can be used to differentiate between different implementations of the same interface.
 *
 * <p>Created by Gabriel Inacio de Souza on February 2, 2025.</p>
 */
@Qualifier
@Retention(RUNTIME)
@Target({ METHOD, FIELD, PARAMETER, TYPE })
public @interface RandomGenerator {
}
