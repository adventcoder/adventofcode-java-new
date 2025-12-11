package adventofcode.utils.array;

import java.lang.reflect.Array;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectArrays {
    @SuppressWarnings("unchecked")
    public static <BaseT, T extends BaseT> T[] newInstance(Class<BaseT> elClass, int size) {
        return (T[]) Array.newInstance(elClass, size);
    }
}
