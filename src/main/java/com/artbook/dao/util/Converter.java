package com.artbook.dao.util;

@FunctionalInterface
public interface Converter<T,U> {
    public U convert(T item) throws Exception;

    public default U convertUnchecked(T item) {
        try {
            return convert(item);
        } catch (RuntimeException re) {
            throw re;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public default <V> Converter<T,V> andThen(Converter<U,V> that) {
        return item -> that.convert(this.convert(item));
    }
}
