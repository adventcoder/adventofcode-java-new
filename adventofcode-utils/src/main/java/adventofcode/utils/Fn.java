package adventofcode.utils;

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
import java.util.function.ToLongFunction;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import adventofcode.utils.iter.Generator;
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

    public static Generator<MatchResult> findAll(String s, String regex) {
        Matcher m = Pattern.compile(regex).matcher(s);
        return () -> m.find() ? m.toMatchResult() : null;
    }

    public static <T extends Comparable<? super T>> List<T> sorted(Iterable<T> xs) {
        List<T> result = new ArrayList<T>();
        for (T x : xs)
            result.add(x);
        result.sort(Comparator.naturalOrder());
        return result;
    }

    public static <T, U> U reduce(Iterable<T> domain, U acc, BiFunction<? super U, ? super T, ? extends U> op) {
        for (T x : domain)
            acc = op.apply(acc, x);
        return acc;
    }

    public static <T> T reduce(Iterable<T> domain, BinaryOperator<T> op) {
        Iterator<T> it = domain.iterator();
        T acc = it.next();
        while (it.hasNext())
            acc = op.apply(acc, it.next());
        return acc;
    }

    public static <T, U extends Comparable<? super U>> U min(Iterable<T> domain, Function<? super T, ? extends U> expr) {
        Iterator<T> it = domain.iterator();
        U min = expr.apply(it.next());
        while (it.hasNext()) {
            U next = expr.apply(it.next());
            if (next.compareTo(min) < 0)
                min = next;
        }
        return min;
    }

    public static <T extends Comparable<? super T>> T min(Iterable<T> domain) {
        return min(domain, Function.identity());
    }

    public static <T, U extends Comparable<? super U>> U max(Iterable<T> domain, Function<? super T, ? extends U> expr) {
        Iterator<T> it = domain.iterator();
        U max = expr.apply(it.next());
        while (it.hasNext()) {
            U next = expr.apply(it.next());
            if (next.compareTo(max) > 0)
                max = next;
        }
        return max;
    }

    public static <T extends Comparable<? super T>> T max(Iterable<T> domain) {
        return max(domain, Function.identity());
    }

    public static <T, U extends Comparable<? super U>> T argMin(Iterable<T> domain, Function<? super T, ? extends U> expr) {
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

    public static <T, U extends Comparable<? super U>> T argMax(Iterable<T> domain, Function<? super T, ? extends U> expr) {
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

    public static <T> int sumInt(Iterable<T> domain, ToIntFunction<? super T> expr) {
        int total = 0;
        for (T x : domain)
            total += expr.applyAsInt(x);
        return total;
    }

    public static <T> long sumLong(Iterable<T> domain, ToLongFunction<? super T> expr) {
        long total = 0;
        for (T x : domain)
            total += expr.applyAsLong(x);
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