package adventofcode.utils.collect;

import java.util.AbstractList;
import java.util.Iterator;
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
        return step > 0 ? start >= stop : start <= stop;
    }

    @Override
    public int size() {
        if (isEmpty()) return 0;
        return (int) Math.max(0, (long)(stop - start + step - (step > 0 ? 1 : -1)) / step);
    }

    @Override
    public Integer get(int index) {
        int val = start + step * index;
        if (step > 0 ? val >= stop : val <= stop) {
            throw new IndexOutOfBoundsException("Index " + index + " out of bounds for range");
        }
        return val;
    }

    @Override
    public boolean contains(Object obj) {
        if (!(obj instanceof Integer)) return false;
        int val = (Integer) obj;
        if (step > 0) {
            if (val < start || val >= stop) return false;
        } else {
            if (val > start || val <= stop) return false;
        }
        return (val - start) % step == 0;
    }

    @Override
    public int indexOf(Object o) {
        if (!(o instanceof Integer)) return -1;
        int val = (Integer) o;

        if (!contains(val)) return -1;

        // Formula: index = (val - start) / step
        return (val - start) / step;
    }

    @Override
    public int lastIndexOf(Object o) {
        return indexOf(o); // all elements are unique in a Range
    }

    @Override
    public Iterator<Integer> iterator() {
        return new PrimitiveIterator.OfInt() {
            int i = start;

            @Override
            public boolean hasNext() {
                return step > 0 ? i < stop : i > stop;
            }

            @Override
            public int nextInt() {
                int curr = i;
                i += step;
                return curr;
            }
        };
    }

    @Override
    public Stream<Integer> stream() {
        return primitiveStream().boxed();
    }

    public IntStream primitiveStream() {
        return IntStream.iterate(start, n -> n + step).takeWhile(n -> step > 0 ? n < stop : n > stop);
    }

    public Range reverse() {
        int n = size();
        if (n == 0) return new Range(0, 0, 1); // empty range
        int newStart = start + step * (n - 1);
        int newStop = start - (step > 0 ? 1 : -1); // ensures the new range stops just past the original start
        int newStep = -step;
        return new Range(newStart, newStop, newStep);
    }

    public int sum() {
        int n = size();
        if (n == 0) return 0;
        int first = start;
        int last = start + step * (n - 1);
        return n * (first + last) / 2;  // sum of arithmetic sequence formula
    }
}
