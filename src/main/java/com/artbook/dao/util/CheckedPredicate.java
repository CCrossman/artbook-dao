package com.artbook.dao.util;

public interface CheckedPredicate<T> {
    public boolean test(T t) throws Exception;
}
