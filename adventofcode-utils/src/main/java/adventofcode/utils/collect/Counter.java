package adventofcode.utils.collect;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class Counter<K> extends Object2IntOpenHashMap<K> {
    public Counter() {
    }

    public Counter(Iterable<K> iter) {
        addAll(iter, 1);
    }

    public int total() {
        return values().intStream().sum();
    }

    public K mostFrequentKey() {
        int maxCount = Integer.MIN_VALUE;
        K maxKey = null;
        for (K k : keySet()) {
            int n = getInt(k);
            if (maxKey == null || n > maxCount) {
                maxCount = n;
                maxKey = k;
            }
        }
        return maxKey;
    }

    public K leastFrequentKey() {
        int minCount = Integer.MAX_VALUE;
        K minKey = null;
        for (K k : keySet()) {
            int n = getInt(k);
            if (minKey == null || n < minCount) {
                minCount = n;
                minKey = k;
            }
        }
        return minKey;
    }

    public int add(K val, int n) {
        return put(val, getInt(val) + n);
    }

    public int subtract(K val, int n) {
        return add(val, -n);
    }

    public void add(Counter<K> other) {
        for (K val : other.keySet())
            add(val, other.getInt(val));
    }

    public void subtract(Counter<K> other) {
        for (K val : other.keySet())
            subtract(val, other.getInt(val));
    }

    public void addAll(Iterable<K> iter, int n) {
        for (K val : iter)
            add(val, n);
    }

    public void subtractAll(Iterable<K> iter, int n) {
        for (K val : iter)
            subtract(val, n);
    }
}
