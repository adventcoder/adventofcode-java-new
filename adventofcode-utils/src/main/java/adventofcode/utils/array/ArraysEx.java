package adventofcode.utils.array;

import java.lang.reflect.Array;
import java.util.function.Supplier;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArraysEx {
    public static <BaseT, T extends BaseT> T[] create(Class<BaseT> elClass, int size, Supplier<T> supplier) {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(elClass, size);
        for (int i = 0; i < arr.length; i++)
            arr[i] = supplier.get();
        return arr;
    }
}
