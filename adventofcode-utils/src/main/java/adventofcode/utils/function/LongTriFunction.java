package adventofcode.utils.function;

@FunctionalInterface
public interface LongTriFunction<T> {
    T apply(long x, long y, long z);
}
