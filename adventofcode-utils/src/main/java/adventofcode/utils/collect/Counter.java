package adventofcode.utils.collect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import it.unimi.dsi.fastutil.ints.IntCollection;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Counter<T> implements Iterable<Counter.Entry<T>> {
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Entry<T> {
        public final T value;
        public final int count;
    }

    private Object2IntMap<T> counts = new Object2IntOpenHashMap<>();

    public Counter() {
    }

    public Counter(T[] arr) {
        addAll(Arrays.asList(arr));
    }

    public Counter(Iterable<T> iter) {
        addAll(iter);
    }

    public Counter(Counter<T> other) {
        addAll(other);
    }

    public boolean isEmpty() {
        return counts.isEmpty();
    }

    public int size() {
        return counts.size();
    }

    public boolean contains(T val) {
        return counts.containsKey(val);
    }

    public Stream<Entry<T>> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    public Spliterator<Entry<T>> spliterator() {
        return Spliterators.spliterator(iterator(), counts.size(), Spliterator.DISTINCT | Spliterator.NONNULL);
    }

    public Iterator<Entry<T>> iterator() {
        Iterator<T> it = counts.keySet().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Entry<T> next() {
                T val = it.next();
                return new Entry<>(val, counts.getInt(val));
            }
        };
    }

    public Set<T> valueSet() {
        return counts.keySet();
    }

    public IntCollection counts() {
        return counts.values();
    }

    public int total() {
        return counts().intStream().sum();
    }

    public int count(T val) {
        return counts.getInt(val);
    }

    public T mostCommon() {
        int maxCount = 0;
        T maxVal = null;
        for (T val : valueSet()) {
            int n = count(val);
            if (maxVal == null || n > maxCount) {
                maxCount = n;
                maxVal = val;
            }
        }
        return maxVal;
    }

    public T leastCommon() {
        int minCount = Integer.MAX_VALUE;
        T minVal = null;
        for (T val : valueSet()) {
            int n = count(val);
            if (minVal == null || n < minCount) {
                minCount = n;
                minVal = val;
            }
        }
        return minVal;
    }

    public List<T> mostCommon(int n) {
        List<T> list = new ArrayList<>(valueSet());
        list.sort(Comparator.comparingInt(this::count).reversed());
        return list.subList(0, Math.min(n, list.size()));
    }


    public List<T> leastCommon(int n) {
        List<T> list = new ArrayList<>(valueSet());
        list.sort(Comparator.comparingInt(this::count));
        return list.subList(0, Math.min(n, list.size()));
    }

    public void add(T val) {
        counts.put(val, counts.getOrDefault(val, 0) + 1);
    }

    public void add(T val, int n) {
        int newCount = counts.getOrDefault(val, 0) + n;
        if (newCount <= 0)
            counts.removeInt(val);
        else
            counts.put(val, newCount);
    }

    public void remove(T val) {
        add(val, -1);
    }

    public void remove(T val, int n) {
        add(val, -n);
    }

    public void addAll(Iterable<T> iter) {
        for (T val : iter)
            add(val);
    }

    public void addAll(Counter<T> other) {
        for (T val : other.valueSet())
            add(val, other.count(val));
    }

    public void removeAll(Iterable<T> other) {
        for (T val : other)
            remove(val);
    }

    public void removeAll(Counter<T> other) {
        for (T val : other.valueSet())
            remove(val, other.count(val));
    }

    public void orEq(Counter<T> other) {
        for (T val : other.valueSet()) {
            if (counts.containsKey(val)) {
                counts.put(val, Math.max(counts.getInt(val), other.count(val)));
            } else {
                counts.put(val, other.count(val));
            }
        }
    }

    public void andEq(Counter<T> other) {
        Iterator<T> it = counts.keySet().iterator();
        while (it.hasNext()) {
            T val = it.next();
            if (other.contains(val)) {
                counts.put(val, Math.min(counts.getInt(val), other.count(val)));
            } else {
                it.remove();
            }
        }
    }

    public Counter<T> or(Counter<T> other) {
        Counter<T> result = new Counter<>();
        result.addAll(this);
        result.orEq(other);
        return result;
    }

    public Counter<T> and(Counter<T> other) {
        Counter<T> result = new Counter<>();
        result.addAll(this);
        result.andEq(other);
        return result;
    }
}
