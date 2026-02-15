package com.artbook.dao.util;

import com.jecklgamis.util.Try;
import com.jecklgamis.util.TryFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class TryHelper {

    private TryHelper() { }

    public static <T> Try<List<T>> sequence(Collection<Try<T>> tries) {
        return sequence(tries, ArrayList::new);
    }

    public static <T,CC extends Collection<T>> Try<CC> sequence(Collection<Try<T>> tries, Supplier<CC> constructor) {
        if (tries == null) {
            return null;
        }
        return TryFactory.attempt(() -> {
            CC results = constructor.get();
            for (Try<T> tryResult : tries) {
                results.add(tryResult.get());
            }
            return results;
        });
    }
}
