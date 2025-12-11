package adventofcode.utils.array;

import java.lang.reflect.Array;

import lombok.experimental.UtilityClass;

//TODO: Consolidate into one ArraysEx class? need to investigate compile time code generation
@UtilityClass
public class ObjectArrays {
    @SuppressWarnings("unchecked")
    public static <BaseT, T extends BaseT> T[] newInstance(Class<BaseT> elClass, int size) {
        return (T[]) Array.newInstance(elClass, size);
    }

    public static void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
