package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Generator<T> extends Iterable<T>, Enumerable<T> {
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
        return Iterators.generate(this::next, Objects::nonNull);
    }

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
}
