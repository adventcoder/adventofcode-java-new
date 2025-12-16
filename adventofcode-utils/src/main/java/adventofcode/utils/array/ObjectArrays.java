package adventofcode.utils.array;

import java.lang.reflect.Array;
import java.util.function.Function;
import java.util.function.ToIntFunction;

import lombok.experimental.UtilityClass;

//TODO: Consolidate into one ArraysEx class? need to investigate compile time code generation
@UtilityClass
public class ObjectArrays {
    @SuppressWarnings("unchecked")
    public static <BaseT, T extends BaseT> T[] newInstance(Class<BaseT> elClass, int size) {
        return (T[]) Array.newInstance(elClass, size);
    }

    public static <T, BaseU, U extends BaseU> U[] map(Class<BaseU> elClass, T[] arr, Function<T, U> func) {
        U[] newArr = newInstance(elClass, arr.length);
        for (int i = 0; i < arr.length; i++)
            newArr[i] = func.apply(arr[i]);
        return newArr;
    }

    public static <T> int[] mapToInt(T[] arr, ToIntFunction<T> func) {
        int[] newArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            newArr[i] = func.applyAsInt(arr[i]);
        return newArr;
    }

    public static void swap(Object[] arr, int i, int j) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
