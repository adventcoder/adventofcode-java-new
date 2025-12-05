package adventofcode.utils.array;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectArrays {
    @SuppressWarnings("unchecked")
    public static <T> T[] create(int size) {
        return (T[]) new Object[size];
    }
}
