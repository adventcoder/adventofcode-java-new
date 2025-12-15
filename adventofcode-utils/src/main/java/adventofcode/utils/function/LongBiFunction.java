package adventofcode.utils.function;

@FunctionalInterface
public interface LongBiFunction<T> {
    T apply(long x, long y);
}
