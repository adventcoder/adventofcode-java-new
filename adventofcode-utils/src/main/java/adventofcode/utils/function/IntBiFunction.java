package adventofcode.utils.function;

@FunctionalInterface
public interface IntBiFunction<T> {
    T apply(int x, int y);
}
