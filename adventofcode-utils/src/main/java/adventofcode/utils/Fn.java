package adventofcode.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import adventofcode.utils.function.IntBiFunction;
import adventofcode.utils.function.IntTriFunction;
import adventofcode.utils.function.LongBiFunction;
import adventofcode.utils.function.LongTriFunction;
import lombok.experimental.UtilityClass;

//TODO: a lot of this could be organised into lang utils, e.g. StringsEx, CollectionsEx
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

    public static <T> T parseIntPair(String s, String sep, IntBiFunction<? extends T> generator) {
        String[] tokens = s.split(sep, 2);
        int x = Integer.parseInt(tokens[0].trim());
        int y = Integer.parseInt(tokens[1].trim());
        return generator.apply(x, y);
    }

    public static <T> T parseIntTriple(String s, String sep, IntTriFunction<? extends T> generator) {
        String[] tokens = s.split(sep, 3);
        int x = Integer.parseInt(tokens[0].trim());
        int y = Integer.parseInt(tokens[1].trim());
        int z = Integer.parseInt(tokens[2].trim());
        return generator.apply(x, y, z);
    }

    public static <T> T parseLongPair(String s, String sep, LongBiFunction<? extends T> generator) {
        String[] tokens = s.split(sep, 2);
        long x = Long.parseLong(tokens[0].trim());
        long y = Long.parseLong(tokens[1].trim());
        return generator.apply(x, y);
    }

    public static <T> T parseLongTriple(String s, String sep, LongTriFunction<? extends T> generator) {
        String[] tokens = s.split(sep, 3);
        long x = Long.parseLong(tokens[0].trim());
        long y = Long.parseLong(tokens[1].trim());
        long z = Long.parseLong(tokens[2].trim());
        return generator.apply(x, y, z);
    }

    public static String lstrip(String s, String prefix) {
        return s.startsWith(prefix) ? s.substring(prefix.length()) : s;
    }

    public static String rstrip(String s, String suffix) {
        return s.endsWith(suffix) ? s.substring(s.length() - suffix.length()) : s;
    }

    public static String strip(String s, String prefix, String suffix) {
        int start = s.startsWith(prefix) ? prefix.length() : 0;
        int end = s.endsWith(suffix) ? s.length() - suffix.length() : s.length();
        return s.substring(start, end);
    }

    public static Stream<MatchResult> findAll(String s, String regex) {
        return Pattern.compile(regex).matcher(s).results();
    }

    public static <T extends Comparable<? super T>> List<T> sorted(Iterable<T> xs) {
        List<T> result = new ArrayList<T>();
        for (T x : xs) result.add(x);
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

    public static <T> int minInt(Iterable<T> domain, ToIntFunction<T> expr) {
        Iterator<T> it = domain.iterator();
        int min = expr.applyAsInt(it.next());
        while (it.hasNext())
            min = Math.min(min, expr.applyAsInt(it.next()));
        return min;
    }

    public static <T> int maxInt(Iterable<T> domain, ToIntFunction<T> expr) {
        Iterator<T> it = domain.iterator();
        int min = expr.applyAsInt(it.next());
        while (it.hasNext())
            min = Math.max(min, expr.applyAsInt(it.next()));
        return min;
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

    public static <T, U> int bisectLeft(List<T> list, U x, Function<? super T, ? extends U> key, Comparator<? super U> cmp) {
        if (list.isEmpty()) return 0;
        int firstIndex = bsearchFirst(0, list.size() - 1, i -> cmp.compare(key.apply(list.get(i)), x) >= 0 ? 0 : -1);
        return firstIndex >= 0 ? firstIndex : -firstIndex - 1;
    }

    public static <T, U> int bisectRight(List<T> list, U x, Function<? super T, ? extends U> key, Comparator<? super U> cmp) {
        if (list.isEmpty()) return 0;
        int lastIndex = bsearchLast(0, list.size() - 1, i -> cmp.compare(key.apply(list.get(i)), x) <= 0 ? 0 : 1);
        return lastIndex >= 0 ? lastIndex + 1 : -lastIndex - 1;
    }

    public static int bsearchFirst(int min, int max, IntUnaryOperator op) {
        while (min < max) {
            int mid = min + (max - min) / 2;
            int val = op.applyAsInt(mid);
            if (val < 0) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }
        assert min == max;
        int val = op.applyAsInt(min);
        if (val == 0) return min;
        int ins = val > 0 ? min : min + 1;
        return -ins - 1;
    }

    public static int bsearchLast(int min, int max, IntUnaryOperator op) {
        while (min < max) {
            int mid = min + (max - min + 1) / 2;
            int val = op.applyAsInt(mid);
            if (val > 0) {
                max = mid - 1;
            } else {
                min = mid;
            }
        }
        assert min == max;
        int val = op.applyAsInt(min);
        if (val == 0) return min;
        int ins = val > 0 ? min : min + 1;
        return -ins - 1;
    }
}