package adventofcode.utils.collect;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Counter<T> {
    private final Map<T, Integer> counts = new HashMap<>();
    private int total = 0;

    public Counter(Iterable<T> it) {
        for (T el : it)
            add(el);
    }

    public Set<T> elements() {
        return Collections.unmodifiableSet(counts.keySet());
    }

    public int count(T el) {
        return counts.getOrDefault(el, 0);
    }

    public int setCount(T el, int count) {
        Integer oldCount;
        if (count == 0) {
            oldCount = counts.remove(el);
        } else {
            oldCount = counts.put(el, count);
            total += count;
        }
        if (oldCount != null)
            total -= oldCount;
        return oldCount == null ? 0 : oldCount;
    }

    public int add(T el, int n) {
        return setCount(el, count(el) + n);
    }

    public int add(T el) {
        return add(el, 1);
    }

    public int remove(T el, int n) {
        return setCount(el, count(el) - n);
    }

    public int remove(T el) {
        return remove(el, 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Counter<?> other)
            return counts.equals(other.counts);
        return false;
    }

    @Override
    public int hashCode() {
        return counts.hashCode();
    }

    @Override
    public String toString() {
        return counts.toString();
    }
}
