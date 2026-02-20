package com.artbook.dao.util;

import io.vavr.Tuple;
import io.vavr.Value;

import java.util.Collection;
import java.util.Optional;

public class Preconditions {
    private Preconditions() { }

    public static <T> T requireNonEmpty(T value) {
        return requireNonEmpty(value, "Value cannot be null or empty");
    }

    public static <T> T requireNonEmpty(T value, String message) {
        // null
        if (value == null) {
            throw new IllegalArgumentException(message);
        }
        // Strings
        if (value instanceof String s && s.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        // Collections
        if (value instanceof Collection<?> c && c.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        // Optional
        if (value instanceof Optional<?> o && o.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        // Tuple
        if (value instanceof Tuple t && t.arity() == 0) {
            throw new IllegalArgumentException(message);
        }
        // option, try, ...
        if (value instanceof Value<?> v && v.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
