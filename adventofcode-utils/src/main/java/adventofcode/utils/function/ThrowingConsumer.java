package adventofcode.utils.function;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

public interface ThrowingConsumer<T, E extends Throwable> {
    void acceptOrThrow(T val) throws E;

    static <T> Consumer<T> unchecked(ThrowingConsumer<T, ? extends IOException> consumer) {
        return val -> {
            try {
                consumer.acceptOrThrow(val);
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        };
    }
}
