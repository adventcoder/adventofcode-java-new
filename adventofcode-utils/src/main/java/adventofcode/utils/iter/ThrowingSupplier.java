package adventofcode.utils.iter;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Supplier;

public interface ThrowingSupplier<T, E extends Throwable> {
    T getOrThrow() throws E;

    static <T> Supplier<T> unchecked(ThrowingSupplier<T, ? extends IOException> supplier) {
        return () -> {
            try {
                return supplier.getOrThrow();
            } catch (IOException ioe) {
                throw new UncheckedIOException(ioe);
            }
        };
    }
}
