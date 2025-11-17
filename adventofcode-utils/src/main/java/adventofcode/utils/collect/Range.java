package adventofcode.utils.collect;

import it.unimi.dsi.fastutil.ints.AbstractIntList;
import it.unimi.dsi.fastutil.ints.IntListIterator;

public class Range extends AbstractIntList {
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

    @Override
    public int size() {
        int n = Math.max(step > 0 ? stop - start : start - stop, 0);
        return -Math.floorDiv(-n, Math.abs(step));
    }

    public Range reverse() {
        return new Range(start + (size() - 1) * step, start - step, -step);
    }

    @Override
    public int getInt(int i) {
        if (i < 0 || i >= size())
            throw new IndexOutOfBoundsException();
        return start + i * step;
    }

    @Override
    public boolean contains(int n) {
        if (step > 0) {
            if (!(n >= start && n < stop)) return false;
        } else {
            if (!(n <= start && n > stop)) return false;
        }
        return (n - start) % step == 0;
    }

    @Override
    public int indexOf(int n) {
        return contains(n) ? (n - start) / step : -1;
    }

    @Override
    public int lastIndexOf(int n) {
        return indexOf(n);
    }

    @Override
    public IntListIterator iterator() {
        return new IntListIterator() {
            private int n = start;

            @Override
            public boolean hasNext() {
                return step > 0 ? n < stop : n > stop;
            }

            @Override
            public boolean hasPrevious() {
                return n != start;
            }

            @Override
            public int nextInt() {
                int curr = n;
                n += step;
                return curr;
            }

            @Override
            public int previousInt() {
                n -= step;
                return n;
            }

            @Override
            public int nextIndex() {
                return (n - start) / step;
            }

            @Override
            public int previousIndex() {
                return nextIndex() - 1;
            }
        };
    }

    public int sum() {
        int n = size();
        return n*start + n*(n-1)/2 * step;
    }
}
