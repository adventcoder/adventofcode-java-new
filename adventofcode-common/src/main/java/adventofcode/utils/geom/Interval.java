package adventofcode.utils.geom;

import java.util.Iterator;
import java.util.stream.IntStream;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Interval implements Iterable<Integer> {
    public int min;
    public int max;

    public int length() {
        return max - min + 1;
    }

    public boolean overlaps(Interval other) {
        return min <= other.max && other.min <= max;
    }

    public boolean contains(long value) {
        return min <= value && value <= max;
    }

    public Interval or(Interval other) {
        return new Interval(Math.min(min, other.min), Math.max(max, other.max));
    }

    public Interval and(Interval other) {
        return new Interval(Math.max(min, other.min),  Math.min(max, other.max));
    }

    public IntStream stream() {
        return IntStream.rangeClosed(min, max);
    }

    @Override
    public Iterator<Integer> iterator() {
        return stream().iterator();
    }

    @Override
    public String toString() {
        return "[" + min + ", " + max + "]";
    }
}