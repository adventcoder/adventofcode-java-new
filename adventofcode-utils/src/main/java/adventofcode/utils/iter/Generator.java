package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Generator<T> extends Enumerable<T>, Iterable<T> {
    T next();

    @Override
    default void forEach(Consumer<? super T> action) {
        T look = next();
        while (look != null) {
            action.accept(look);
            look = next();
        }
    }

    @Override
    default Iterator<T> iterator() {
        return new Iterator<>() {
            T look = Generator.this.next();

            @Override
            public boolean hasNext() {
                return look != null;
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                T curr = look;
                look = Generator.this.next();
                return curr;
            }
        };
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
