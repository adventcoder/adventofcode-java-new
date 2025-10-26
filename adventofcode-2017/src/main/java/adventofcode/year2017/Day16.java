package adventofcode.year2017;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import picocli.CommandLine.Option;

@Puzzle(day = 16, name = "Permutation Promenage")
public class Day16 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day16.class, args);
    }

    @Option(names = "--size", defaultValue = "16")
    private int size;

    private Dance dance;

    @Override
    public void parse(String input) {
        dance = new Dance(size);
        for (String move : input.split(",")) {
            if (move.startsWith("s")) {
                dance.spin(Integer.parseInt(move.substring(1)));
            } else if (move.startsWith("x")) {
                String[] args = move.substring(1).split("/");
                dance.exchange(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
            } else if (move.startsWith("p")) {
                String[] args = move.substring(1).split("/");
                dance.partner(args[0].charAt(0), args[1].charAt(0));
            }
        }
    }
    
    @Override
    public String part1() {
        return dance.apply();
    }

    @Override
    public String part2() {
        dance.repeat(1_000_000_000);
        return dance.apply();
    }

    private static class Dance {
        private final int size;
        private int[] indexPerm;
        private int[] labelPerm;

        public Dance(int size) {
            this.size = size;
            indexPerm = IntStream.range(0, size).toArray();
            labelPerm = IntStream.range(0, size).toArray();
        }

        public void spin(int x) {
            rotateRight(indexPerm, x);
        }

        public void exchange(int i, int j) {
            swap(indexPerm, i, j);
        }

        public void partner(char a, char b) {
            swap(labelPerm, a - 'a', b - 'a');
        }

        public void repeat(long n) {
            indexPerm = pow(indexPerm, n);
            labelPerm = pow(labelPerm, n);
        }

        public String apply() {
            char[] result = new char[size];
            for (int i = 0; i < size; i++)
                result[i] = (char) ('a' + index(labelPerm, indexPerm[i]));
            return new String(result);
        }

        private static void rotateRight(int[] arr, int n) {
            rotateLeft(arr, -n);
        }

        private static void rotateLeft(int[] arr, int n) {
            n = Math.floorMod(n, arr.length);
            reverse(arr, 0, n);
            reverse(arr, n, arr.length);
            reverse(arr, 0, arr.length);
        }

        private static void reverse(int[] arr, int start, int end) {
            while (end - start > 1)
                swap(arr, start++, --end);
        }

        private static void swap(int[] arr, int i, int j) {
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }

        private static final int index(int[] arr, int x) {
            for (int i = 0; i < arr.length; i++)
                if (arr[i] == x)
                    return i;
            throw new NoSuchElementException();
        }

        private static int[] pow(int[] perm, long n) {
            int size = perm.length;
            int[] result = IntStream.range(0, size).toArray(); // identity
            int[] base = Arrays.copyOf(perm, size);

            while (n > 0) {
                if ((n & 1) != 0) {
                    result = compose(result, base);
                }
                base = compose(base, base);
                n >>= 1;
            }
            return result;
        }

        private static int[] compose(int[] a, int[] b) {
            // returns a ∘ b : apply b first, then a
            int[] out = new int[a.length];
            for (int i = 0; i < a.length; i++)
                out[i] = a[b[i]];
            return out;
        }
    }
}
