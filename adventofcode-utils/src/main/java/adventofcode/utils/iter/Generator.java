package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Generator<T> extends Iterable<T>, Enumerable<T> {
    T next();

    default boolean stop(T val) {
        return val == null;
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    default void forEach(Consumer<? super T> action) {
        Iterable.super.forEach(action);
    }

    @Override
    default Iterator<T> iterator() {
        return new Iterator<T>() {
            private T look = Generator.this.next();

            @Override
            public boolean hasNext() {
                return !stop(look);
            }

            @Override
            public T next() {
                if (stop(look))
                    throw new NoSuchElementException();
                T curr = look;
                look = Generator.this.next();
                return curr;
            }
        };
    }
}
