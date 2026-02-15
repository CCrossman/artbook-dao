package com.artbook.dao.util;

import com.jecklgamis.util.Try;

@FunctionalInterface
public interface Converter<T,U> {
    public Try<U> convert(T item);

    public default <V> Converter<T,V> andThen(Converter<U,V> that) {
        return item -> this.convert(item).flatMap(that::convert);
    }
}
