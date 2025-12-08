package adventofcode.utils.collect;

import adventofcode.utils.Fn;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class Counter<K> extends Object2IntOpenHashMap<K> {
    public Counter() {
        super();
    }

    public Counter(Iterable<K> vals) {
        this();
        incAll(vals);
    }

    public Counter(Object2IntMap<K> other) {
        this();
        putAll(other);
    }

    public int total() {
        return values().intStream().sum();
    }

    public Entry<K> mostCommon() {
        return Fn.argMax(object2IntEntrySet(), Entry::getIntValue);
    }

    public Entry<K> leastCommon() {
        return Fn.argMin(object2IntEntrySet(), Entry::getIntValue);
    }

    public int inc(K val) {
        return add(val, 1);
    }

    public void incAll(Iterable<? extends K> vals) {
        for (K val : vals) inc(val);
    }

    public int dec(K val) {
        return subtract(val, 1);
    }

    public void decAll(Iterable<? extends K> vals) {
        for (K val : vals) dec(val);
    }

    public int add(K val, int n) {
        return merge(val, n, Integer::sum);
    }

    public void add(Counter<? extends K> other) {
        for (K val : other.keySet())
            add(val, other.getInt(val));
    }

    public int subtract(K val, int n) {
        return merge(val, -n, Integer::sum);
    }

    public void subtract(Counter<? extends K> other) {
        for (K val : other.keySet())
            subtract(val, other.getInt(val));
    }
}
