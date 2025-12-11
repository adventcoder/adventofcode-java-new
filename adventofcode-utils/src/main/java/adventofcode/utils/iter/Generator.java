package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Generator<T> extends Enumerable<T>, Iterable<T> {
    T next();

    default boolean cond(T look) {
        return look != null;
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    default void forEach(Consumer<? super T> action) {
        T look = next();
        while (cond(look)) {
            action.accept(look);
            look = next();
        }
    }

    default Iterator<T> iterator() {
        return new Iterator<>() {
            T look = next();

            @Override
            public boolean hasNext() {
                return cond(look);
            }

            @Override
            public T next() {
                if (!cond(look))
                    throw new NoSuchElementException();
                T curr = look;
                look = next();
                return curr;
            }
        };
    }
}
