package adventofcode.utils.iter;

import java.util.NoSuchElementException;

public interface Maybe<T> {
    boolean isPresent();
    T get();

    public static <T> Maybe<T> present(T value) {
        return new Maybe<>() {
            @Override
            public boolean isPresent() {
                return true;
            }

            @Override
            public T get() {
                return value;
            }
        };
    }

    public static <T> Maybe<T> empty() {
        return new Maybe<>() {
            @Override
            public boolean isPresent() {
                return false;
            }

            @Override
            public T get() {
                throw new NoSuchElementException();
            }
        };
    }
}
