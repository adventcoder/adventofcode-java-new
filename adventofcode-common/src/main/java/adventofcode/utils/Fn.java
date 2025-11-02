package adventofcode.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Fn {
    public static <T> Stream<T> parseVals(String s, String sep, Function<? super String, ? extends T> parser) {
        return Stream.of(s.split(sep)).map(String::trim).map(parser);
    }
    
    public static int[] parseInts(String s, String sep) {
        String[] tokens = s.split(sep);
        int[] result = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++)
            result[i] = Integer.parseInt(tokens[i].trim());
        return result;
    }

    public static byte[] parseBytes(String s, String sep) {
        String[] tokens = s.split(sep);
        byte[] result = new byte[tokens.length];
        for (int i = 0; i < tokens.length; i++)
            result[i] = (byte) Integer.parseInt(tokens[i].trim());
        return result;
    }

    public static String between(String s, String prefix, String suffix) {
        int start = s.indexOf(prefix);
        if (start < 0) throw new IndexOutOfBoundsException();
        int end = s.indexOf(suffix, start + prefix.length());
        if (end < 0) throw new IndexOutOfBoundsException();
        return s.substring(start + prefix.length(), end);
    }

    public static <T extends Comparable<T>> List<T> sorted(Iterable<T> xs) {
        List<T> result = new ArrayList<T>();
        for (T x : xs)
            result.add(x);
        result.sort(Comparator.naturalOrder());
        return result;
    }

    public static <T, U extends Comparable<U>> U min(Iterable<T> domain, Function<? super T, ? extends U> expr) {
        Iterator<T> it = domain.iterator();
        U min = expr.apply(it.next());
        while (it.hasNext()) {
            U next = expr.apply(it.next());
            if (next.compareTo(min) < 0)
                min = next;
        }
        return min;
    }

    public static <T, U extends Comparable<U>> U max(Iterable<T> domain, Function<? super T, ? extends U> expr) {
        Iterator<T> it = domain.iterator();
        U max = expr.apply(it.next());
        while (it.hasNext()) {
            U next = expr.apply(it.next());
            if (next.compareTo(max) > 0)
                max = next;
        }
        return max;
    }

    public static <T> int sum(Iterable<T> domain, ToIntFunction<? super T> expr) {
        int total = 0;
        for (T x : domain)
            total += expr.applyAsInt(x);
        return total;
    }

    public static <T> int count(Iterable<T> domain, Predicate<? super T> pred) {
        int count = 0;
        for (T x : domain)
            if (pred.test(x))
                count++;
        return count;
    }

    public static <T> boolean any(Iterable<T> domain, Predicate<? super T> pred) {
        for (T x : domain)
            if (pred.test(x))
                return true;
        return false;
    }

    public static <T> boolean all(Iterable<T> domain, Predicate<? super T> pred) {
        for (T x : domain)
            if (!pred.test(x))
                return false;
        return true;
    }

    public static int bsearch(int start, int stop, IntUnaryOperator cmp) {
        while (start < stop) {
            int mid = start + (stop - start) / 2;
            int val = cmp.applyAsInt(mid);
            if (val < 0) {
                start = mid + 1;
            } else {
                stop = mid;
            }
        }
        if (start >= stop || cmp.applyAsInt(start) != 0) {
            throw new NoSuchElementException();
        }
        return start;
    }

    public static <T1, T2, R> BiFunction<T1, T2, R> memoize(UnaryOperator<BiFunction<T1, T2, R>> op) {
        return new BiFunction<>() {
            private final Map<List<Object>, R> memo = new HashMap<>();
            private final BiFunction<T1, T2, R> func = op.apply(this);

            @Override
            public R apply(T1 arg1, T2 arg2) {
                List<Object> key = List.of(arg1, arg2);
                if (memo.containsKey(key))
                    return memo.get(key);
                R value = func.apply(arg1, arg2);
                memo.put(key, value);
                return value;
            }
        };
    }
}