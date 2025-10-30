package adventofcode.utils.collect;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.IntBinaryOperator;

import lombok.AllArgsConstructor;

import java.util.stream.IntStream;

@AllArgsConstructor
public class IntArray implements Comparable<IntArray>, Iterable<Integer>, Cloneable {
    private final int[] arr;

    public static IntArray of(int... arr) {
        return new IntArray(arr);
    }

    public boolean isEmpty() {
        return arr.length == 0;
    }

    public int length() {
        return arr.length;
    }

    public int get(int i) {
        return arr[i];
    }

    public void set(int i, int val) {
        arr[i] = val;
    }

    public void fill(int val) {
        Arrays.fill(arr, val);
    }

    public void inc(int i, int val) {
        arr[i] += val;
    }

    public void incAll(int val) {
        for (int i = 0; i < arr.length; i++)
            arr[i] += val;
    }

    @Override
    public IntArray clone() {
        return new IntArray(arr.clone());
    }

    @Override
    public int compareTo(IntArray other) {
        return Arrays.compare(arr, other.arr);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntArray other)
            return Arrays.equals(arr, other.arr);
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(arr);
    }

    @Override
    public String toString() {
        return Arrays.toString(arr);
    }

    public void sort() {
        Arrays.sort(arr);
    }

    public int binarySearch(int target) {
        return Arrays.binarySearch(arr, target);
    }

    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < arr.length;
            }

            @Override
            public Integer next() {
                return arr[i++];
            }
        };
    }

    public IntStream stream() {
        return Arrays.stream(arr);
    }

    public boolean contains(int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return true;
        return false;
    }

    public int index(int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return i;
        throw new NoSuchElementException();
    }

    public int reduce(int identity, IntBinaryOperator op) {
        int acc = identity;
        for (int i = 0; i < arr.length; i++)
            acc = op.applyAsInt(acc, arr[i]);
        return acc;
    }

    public int reduce(IntBinaryOperator op) {
        if (isEmpty())
            throw new NoSuchElementException();
        int acc = arr[0];
        for (int i = 1; i < arr.length; i++)
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

    public IntArray add(IntArray other) {
        int[] newArray = new int[arr.length + other.arr.length];
        System.arraycopy(arr, 0, newArray, 0, arr.length);
        System.arraycopy(other.arr, 0, newArray, arr.length, other.arr.length);
        return new IntArray(newArray);
    }

    public IntArray multiply(int n) {
        int[] newArray = new int[arr.length * n];
        for (int i = 0; i < n; i++)
            System.arraycopy(arr, 0, newArray, i * arr.length, arr.length);
        return new IntArray(newArray);
    }
}
