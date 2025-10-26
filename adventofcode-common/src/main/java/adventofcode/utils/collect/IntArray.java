package adventofcode.utils.collect;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.function.IntBinaryOperator;

import lombok.AllArgsConstructor;

import java.util.stream.IntStream;

@AllArgsConstructor
public class IntArray implements Comparable<IntArray> {
    private final int[] arr;
    private final int start;
    private final int end;

    public IntArray(int... arr) {
        this(arr, 0, arr.length);
    }

    public boolean isEmpty() {
        return start >= end;
    }

    public int length() {
        return end - start;
    }

    public int get(int i) {
        return arr[start + i];
    }

    public void set(int i, int val) {
        arr[start + i] = val;
    }

    public void setAll(int val) {
        Arrays.fill(arr, start, end, val);
    }

    public void inc(int i, int val) {
        arr[start + i] += val;
    }

    public void incAll(int val) {
        for (int i = start; i < end; i++)
            arr[i] += val;
    }

    public IntArray slice(int start, int end) {
        return new IntArray(arr, this.start + start, this.start + end);
    }

    public IntArray copy() {
        return new IntArray(Arrays.copyOfRange(arr, start, end));
    }

    @Override
    public int compareTo(IntArray other) {
        return Arrays.compare(arr, start, end, other.arr, other.start, other.end);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntArray other)
            return Arrays.equals(arr, start, end, other.arr, other.start, other.end);
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = start; i < end; i++)
            result = 31 * result + arr[i];
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        if (!isEmpty()) {
            sb.append(arr[start]);
            for (int i = start + 1; i < end; i++)
                sb.append(", ").append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    public void sort() {
        Arrays.sort(arr, start, end);
    }

    public int binarySearch(int val) {
        return Arrays.binarySearch(arr, start, end, val);
    }

    public IntStream stream() {
        return Arrays.stream(arr, start, end);
    }

    public boolean contains(int val) {
        for (int i = start; i < end; i++)
            if (arr[i] == val)
                return true;
        return false;
    }

    public int index(int val) {
        for (int i = start; i < end; i++)
            if (arr[i] == val)
                return i - start;
        throw new NoSuchElementException();
    }

    public int reduce(int identity, IntBinaryOperator op) {
        int acc = identity;
        for (int i = start; i < end; i++)
            acc = op.applyAsInt(acc, arr[i]);
        return acc;
    }

    public int reduce(IntBinaryOperator op) {
        if (isEmpty())
            throw new NoSuchElementException();
        int acc = arr[start];
        for (int i = start + 1; i < end; i++)
            acc = op.applyAsInt(acc, arr[i]);
        return acc;
    }

    public int sum() {
        return reduce(0, Integer::sum);
    }

    public int min() {
        return reduce(Integer::min);
    }

    public int max() {
        return reduce(Integer::max);
    }
}
