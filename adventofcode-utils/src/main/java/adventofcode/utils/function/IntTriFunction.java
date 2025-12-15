package adventofcode.utils.function;

@FunctionalInterface
public interface IntTriFunction<T> {
    T apply(int x, int y, int z);
}
