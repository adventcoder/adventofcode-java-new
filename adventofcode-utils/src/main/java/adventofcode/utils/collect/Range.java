package adventofcode.utils.collect;

import java.util.AbstractList;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Range extends AbstractList<Integer> {
    private final int start;
    private final int stop;
    private final int step;

    public Range(int start, int stop, int step) {
        if (step == 0)
            throw new IllegalArgumentException("step can't be 0");
        this.start = start;
        this.stop = stop;
        this.step = step;
    }

    public Range(int start, int stop) {
        this(start, stop, 1);
    }

    public Range(int stop) {
        this(0, stop);
    }

    @Override
    public boolean isEmpty() {
        return step > 0 ? stop <= start : start <= stop;
    }

    private int lastIndex() {
        int n = step > 0 ? stop - start : start - stop;
        if (n <= 0) return -1;
        return (n - 1) / Math.abs(step);
    }

    @Override
    public int size() {
        return lastIndex() + 1;
    }

    public Range reverse() {
        return new Range(start + lastIndex() * step, start - step, -step);
    }

    @Override
    public Integer get(int i) {
        if (i < 0 || i > lastIndex())
            throw new IndexOutOfBoundsException();
        return start + i * step;
    }

    @Override
    public boolean contains(Object obj) {
        if (!(obj instanceof Integer)) return false;
        int val = (Integer) obj;
        if (step > 0) {
            if (val >= start && val < stop) return false;
        } else {
            if (val <= start && val > stop) return false;
        }
        return (val - start) % step == 0;
    }

    @Override
    public int indexOf(Object obj) {
        if (!contains(obj)) return -1;
        int val = (Integer) obj;
        return (val - start) / step;
    }

    @Override
    public int lastIndexOf(Object obj) {
        return indexOf(obj);
    }

    @Override
    public PrimitiveIterator.OfInt iterator() {
        return new PrimitiveIterator.OfInt() {
            private int n = start;

            @Override
            public boolean hasNext() {
                return step > 0 ? n < stop : n > stop;
            }

            @Override
            public int nextInt() {
                int curr = n;
                n += step;
                return curr;
            }
        };
    }

    @Override
    public Stream<Integer> stream() {
        return intStream().boxed();
    }

    public IntStream intStream() {
        return IntStream.iterate(start, n -> n + step)
            .takeWhile(n -> step > 0 ? n < stop : n > stop);
    }

    public int sum() {
        int n = size();
        return n*start + n*(n-1)/2 * step;
    }
}
