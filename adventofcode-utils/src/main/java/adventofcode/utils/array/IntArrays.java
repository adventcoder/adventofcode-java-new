package adventofcode.utils.array;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntArrays {
    public static int[] create(int size, int val) {
        int[] arr = new int[size];
        Arrays.fill(arr, val);
        return arr;
    }

    public static int[][] create(int width, int height, int val) {
        int[][] arr = new int[height][width];
        for (int[] row : arr)
            Arrays.fill(row, val);
        return arr;
    }

    public static int[] create(int size, IntUnaryOperator op) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++)
            arr[i] = op.applyAsInt(i);
        return arr;
    }
    public static int[][] create(int width, int height, IntBinaryOperator op) {
        int[][] arr = new int[height][width];
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                arr[y][x] = op.applyAsInt(x, y);
        return arr;
    }

    public static int[] concat(int[] a, int[] b) {
        if (a.length == 0) return b;
        if (b.length == 0) return a;
        int[] res = new int[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);
        return res;
    }

    public static PrimitiveIterator.OfInt iterator(int[] arr) {
        return new PrimitiveIterator.OfInt() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < arr.length;
            }

            @Override
            public int nextInt() {
                return arr[i++];
            }
        };
    }

    public static int index(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return i;
        throw new IndexOutOfBoundsException();
    }

    public static boolean contains(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return true;
        return false;
    }

    public static int reduce(int[] arr, int acc, IntBinaryOperator op) {
        for (int i = 0; i < arr.length; i++)
            acc = op.applyAsInt(acc, arr[i]);
        return acc;
    }

    public static int sum(int[] arr) {
        return reduce(arr, 0, Integer::sum);
    }

    public static int min(int[] arr) {
        return reduce(arr, Integer.MAX_VALUE, Integer::min);
    }

    public static int max(int[] arr) {
        return reduce(arr, Integer.MIN_VALUE, Integer::max);
    }

    public static int minIndex(int[] arr) {
        int min = arr[0];
        int minIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static int maxIndex(int[] arr) {
        int max = arr[0];
        int maxIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] > max) {
                max = arr[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    public static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    public static void reverse(int[] arr) {
        reverse(arr, 0, arr.length);
    }

    public static void reverse(int[] arr, int start, int stop) {
        while (stop - start > 1)
            swap(arr, start++, --stop);
    }

    public static void rotateLeft(int[] arr, int n) {
        if (n == 0) return;
        n = Math.floorMod(n, arr.length);
        reverse(arr, 0, n);
        reverse(arr, n, arr.length);
        reverse(arr, 0, arr.length);
    }

    public static void rotateRight(int[] arr, int n) {
        rotateLeft(arr, -n);
    }

    public static boolean nextPermutation(int[] arr) {
        if (arr.length < 2) return false;

        // Find the longest non-increasing suffix
        int i = arr.length - 2;
        while (i >= 0 && arr[i] >= arr[i + 1])
            i--;
        if (i < 0)
            return false;

        // Find the rightmost element larger than the pivot
        int j = arr.length - 1;
        while (arr[j] <= arr[i])
            j--;

        swap(arr, i, j);
        reverse(arr, i + 1, arr.length);
        return true;
    }

    public static boolean prevPermutation(int[] arr) {
        if (arr.length < 2) return false;

        // Find the longest non-decreasing suffix
        int i = arr.length - 2;
        while (i >= 0 && arr[i] <= arr[i + 1])
            i--;
        if (i < 0)
            return false;

        // Find the rightmost element smaller than the pivot
        int j = arr.length - 1;
        while (arr[j] >= arr[i])
            j--;

        swap(arr, i, j);
        reverse(arr, i + 1, arr.length);

        return true;
    }
}
