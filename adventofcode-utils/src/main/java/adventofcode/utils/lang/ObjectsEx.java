package adventofcode.utils.lang;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

import it.unimi.dsi.fastutil.Hash;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectsEx {
    public static <T, U> U ifNonNull(T o, Function<? super T, ? extends U> func) {
        return o != null ? func.apply(o) : null;
    }

    public static int deepHashCode(Object o) {
        if (o instanceof Object[] arr) return Arrays.deepHashCode(arr);
        if (o instanceof byte[] arr) return Arrays.hashCode(arr);
        if (o instanceof short[] arr) return Arrays.hashCode(arr);
        if (o instanceof int[] arr) return Arrays.hashCode(arr);
        if (o instanceof long[] arr) return Arrays.hashCode(arr);
        if (o instanceof float[] arr) return Arrays.hashCode(arr);
        if (o instanceof double[] arr) return Arrays.hashCode(arr);
        if (o instanceof char[] arr) return Arrays.hashCode(arr);
        if (o instanceof boolean[] arr) return Arrays.hashCode(arr);
        return Objects.hashCode(o);
    }

    public static String deepToString(Object o) {
        if (o instanceof Object[] arr) return Arrays.deepToString(arr);
        if (o instanceof byte[] arr) return Arrays.toString(arr);
        if (o instanceof short[] arr) return Arrays.toString(arr);
        if (o instanceof int[] arr) return Arrays.toString(arr);
        if (o instanceof long[] arr) return Arrays.toString(arr);
        if (o instanceof float[] arr) return Arrays.toString(arr);
        if (o instanceof double[] arr) return Arrays.toString(arr);
        if (o instanceof char[] arr) return Arrays.toString(arr);
        if (o instanceof boolean[] arr) return Arrays.toString(arr);
        return Objects.toString(o);
    }

    public static final Hash.Strategy<Object> DEEP_HASH_STRATEGY = new Hash.Strategy<Object>() {
        @Override
        public int hashCode(Object o) {
            return deepHashCode(o);
        }

        @Override
        public boolean equals(Object a, Object b) {
            return Objects.deepEquals(a, b);
        }
    };
}
