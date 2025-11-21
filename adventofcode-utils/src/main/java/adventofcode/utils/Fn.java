package adventofcode.utils;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
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

    public static String strip(String s, String prefix, String suffix) {
        int start = s.startsWith(prefix) ? prefix.length() : 0;
        int end = s.endsWith(suffix) ? s.length() - suffix.length() : s.length();
        return s.substring(start, end);
    }

    public static Stream<MatchResult> findall(String s, String regex) {
        return Pattern.compile(regex).matcher(s).results();
    }

    public static <T extends Comparable<T>> List<T> sorted(Iterable<T> xs) {
        List<T> result = new ArrayList<T>();
        for (T x : xs)
            result.add(x);
        result.sort(Comparator.naturalOrder());
        return result;
    }

    public static <T> T reduce(Iterable<T> domain, BinaryOperator<T> op) {
        Iterator<T> it = domain.iterator();
        T acc = it.next();
        while (it.hasNext())
            acc = op.apply(acc, it.next());
        return acc;
    }

    public static <T, U> List<U> map(T[] vals, Function<? super T, ? extends U> op) {
        return new AbstractList<U>() {
            @Override
            public int size() {
                return vals.length;
            }

            @Override
            public U get(int i) {
                return op.apply(vals[i]);
            }
        };
    }

    public static <T, U, V> Iterable<V> zip(Iterable<T> xs, Iterable<U> ys, BiFunction<? super T, ? super U, ? extends V> zipper) {
        return () -> {
            Iterator<T> xIt = xs.iterator();
            Iterator<U> yIt = ys.iterator();
            return new Iterator<>() {
                @Override
                public boolean hasNext() {
                    return xIt.hasNext() && yIt.hasNext();
                }

                @Override
                public V next() {
                    return zipper.apply(xIt.next(), yIt.next());
                }
            };
        };
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

    public static <T extends Comparable<T>> T min(Iterable<T> domain) {
        return min(domain, Function.identity());
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

    public static <T extends Comparable<T>> T max(Iterable<T> domain) {
        return max(domain, Function.identity());
    }

    public static <T, U extends Comparable<U>> T argMin(Iterable<T> domain, Function<? super T, ? extends U> expr) {
        Iterator<T> it = domain.iterator();
        T argMin = it.next();
        U min = expr.apply(argMin);
        while (it.hasNext()) {
            T argNext = it.next();
            U next = expr.apply(argNext);
            if (next.compareTo(min) < 0) {
                argMin = argNext;
                min = next;
            }
        }
        return argMin;
    }

    public static <T, U extends Comparable<U>> T argMax(Iterable<T> domain, Function<? super T, ? extends U> expr) {
        Iterator<T> it = domain.iterator();
        T argMax = it.next();
        U max = expr.apply(argMax);
        while (it.hasNext()) {
            T argNext = it.next();
            U next = expr.apply(argNext);
            if (next.compareTo(max) > 0) {
                argMax = argNext;
                max = next;
            }
        }
        return argMax;
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

    public static int bsearchFirst(int min, int max, IntUnaryOperator op) {
        while (min <= max) {
            int mid = min + (max - min) / 2;
            int val = op.applyAsInt(mid);
            if (val < 0) {
                min = mid + 1;
            } else {
                max = mid - 1;
            }
        }
        if (op.applyAsInt(min) == 0)
            return min;
        throw new NoSuchElementException();
    }

    public static int bsearchLast(int min, int max, IntUnaryOperator op) {
        while (min <= max) {
            int mid = min + (max - min) / 2;
            int val = op.applyAsInt(mid);
            if (val > 0) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        if (op.applyAsInt(max) == 0)
            return max;
        throw new NoSuchElementException();
    }
}