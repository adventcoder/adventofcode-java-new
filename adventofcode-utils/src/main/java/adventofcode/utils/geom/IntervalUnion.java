package adventofcode.utils.geom;

import java.util.Iterator;
import java.util.NavigableMap;
import java.util.TreeMap;

import adventofcode.utils.iter.Iterators;

public class IntervalUnion implements Iterable<Interval> {
    private final NavigableMap<Long, Long> tree = new TreeMap<>();

    public void add(long min, long max) {
        if (min > max) return;
        var prev = tree.floorEntry(min);
        if (prev != null && min <= prev.getValue() + 1) {
            tree.remove(prev.getKey());
            min = prev.getKey();
            max = Math.max(prev.getValue(), max);
        }
        var next = tree.ceilingEntry(min);
        while (next != null && next.getKey() <= max + 1) {
            max = Math.max(max, next.getValue());
            tree.remove(next.getKey());
            next = tree.ceilingEntry(min);
        }
        tree.put(min, max);
    }

    public boolean contains(Long val) {
        var prev = tree.floorEntry(val);
        return prev != null && val <= prev.getValue();
    }

    @Override
    public Iterator<Interval> iterator() {
        return Iterators.map(tree.entrySet().iterator(), entry -> new Interval(entry.getKey(), entry.getValue()));
    }
}
