package adventofcode.utils.array;

import java.lang.reflect.Array;

public class ObjectArrays {
    @SuppressWarnings("unchecked")
    public static <BaseT, T extends BaseT> T[] newInstance(Class<BaseT> elClass, int size) {
        return (T[]) Array.newInstance(elClass, size);
    }

    public void swap(Object[] arr, int i, int j) {
        Object t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }
}
