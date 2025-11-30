package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import adventofcode.utils.ObjectsEx;

public interface Generator<T> extends Iterable<T>, Enumerable<T> {
    T next();

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
                return look != null;
            }

            @Override
            public T next() {
                if (look == null)
                    throw new NoSuchElementException();
                T curr = look;
                look = Generator.this.next();
                return curr;
            }
        };
    }

    @Override
    default <U> Generator<U> map(Function<? super T, ? extends U> func) {
        return () -> ObjectsEx.map(next(), func);
    }

    @Override
    default Generator<T> filter(Predicate<? super T> pred) {
        return () -> {
            T look = next();
            while (look != null && !pred.test(look))
                look = next();
            return look;
        };
    }
}
