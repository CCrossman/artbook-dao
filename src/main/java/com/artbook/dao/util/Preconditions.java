package com.artbook.dao.util;

public class Preconditions {
    private Preconditions() { }

    public static String requireNonEmpty(String value) {
        return requireNonEmpty(value, "Value cannot be null or empty");
    }

    public static String requireNonEmpty(String value, String message) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
