package adventofcode.utils.collect;

import java.util.NoSuchElementException;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntArrays {
    public static int[] concat(int[] a, int[] b) {
        if (a.length == 0) return b;
        if (b.length == 0) return a;
        int[] res = new int[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);
        return res;
    }

    public static int index(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return i;
        throw new NoSuchElementException();
    }

    public static boolean contains(int[] arr, int val) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == val)
                return true;
        return false;
    }

    public void swap(int[] arr, int i, int j) {
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
