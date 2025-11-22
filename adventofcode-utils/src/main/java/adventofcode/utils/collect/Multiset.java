package adventofcode.utils.collect;

import java.util.Set;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntRBTreeMap;

public class Multiset<T> {
    private Object2IntMap<T> counts = new Object2IntRBTreeMap<>();
    private int size = 0;

    public Multiset() {
    }

    public Multiset(Iterable<T> iter) {
        addAll(iter);
    }

    public Multiset(Multiset<T> other) {
        addAll(other);
    }

    public void clear() {
        counts.clear();
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public int count(T val) {
        return counts.getInt(val);
    }

    public Set<T> valueSet() {
        return counts.keySet();
    }

    public boolean contains(Object val) {
        return counts.containsKey(val);
    }

    public int add(T val, int n) {
        int oldCount = counts.getInt(val);
        if (n != 0) {
            if (-n >= oldCount) {
                counts.removeInt(val);
                size -= oldCount;
            } else {
                counts.put(val, oldCount + n);
                size += n;
            }
        }
        return oldCount;
    }

    public int add(T val) {
        return add(val, 1);
    }

    public int remove(T val, int n) {
        return add(val, -n);
    }

    public int remove(T val) {
        return remove(val, 1);
    }

    public boolean containsAll(Set<? extends T> vals) {
        return counts.keySet().containsAll(vals);
    }

    public void addAll(Iterable<? extends T> vals) {
        for (T val : vals)
            add(val);
    }

    public void removeAll(Iterable<? extends T> vals) {
        for (T val : vals)
            remove(val);
    }

    public boolean containsAll(Multiset<T> other) {
        if (size < other.size) return false;
        for (T val : other.valueSet())
            if (counts.getInt(val) < other.count(val)) return false;
        return true;
    }

    public void addAll(Multiset<T> other) {
        for (T val : other.valueSet())
            add(val, other.count(val));
    }

    public void removeAll(Multiset<T> other) {
        for (T val : other.valueSet())
            remove(val, other.count(val));
    }

    public void intersect(Multiset<T> other) {
        for (T val : counts.keySet()) {
            int newCount = Math.min(counts.getInt(val), other.count(val));
            if (newCount == 0) {
                size -= counts.removeInt(val);
            } else {
                size += newCount - counts.put(val, newCount);
            }
        }
    }

    public void merge(Multiset<T> other) {
        for (T val : other.valueSet()) {
            int newCount = Math.max(counts.getInt(val), other.count(val));
            size += newCount - counts.put(val, newCount);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Multiset other)) return false;
        return size == other.size && counts.equals(other.counts);
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
