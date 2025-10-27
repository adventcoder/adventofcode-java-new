package adventofcode.year2017;

import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import lombok.AllArgsConstructor;
import picocli.CommandLine.Option;

@Puzzle(day = 16, name = "Permutation Promenade")
public class Day16 extends AbstractDay {
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
    public CharSequence part1() {
        return dance.apply(start());
    }

    @Override
    public CharSequence part2() {
        return dance.repeat(1_000_000_000).apply(start());
    }

    private CharSequence start() {
        StringBuilder seq = new StringBuilder(size);
        for (int i = 0; i < size; i++)
            seq.append((char) ('a' + i));
        return seq;
    }

    @AllArgsConstructor
    private static class Dance {
        // index -> original index
        private Permutation indexPerm;
        // label -> original label
        private Permutation labelPerm;

        public Dance(int size) {
            indexPerm = Permutation.identity(size);
            labelPerm = Permutation.identity(size);
        }

        public void spin(int x) {
            indexPerm.rotateRight(x);
        }

        public void exchange(int index1, int index2) {
            indexPerm.swap(index1, index2);
        }

        public void partner(char label1, char label2) {
            labelPerm.swap(label1 - 'a', label2 - 'a');
        }

        public Dance repeat(long n) {
            return new Dance(indexPerm.pow(n), labelPerm.pow(n));
        }

        public CharSequence apply(CharSequence seq) {
            StringBuilder newSeq = new StringBuilder(seq.length());
            for (int newIndex = 0; newIndex < seq.length(); newIndex++) {
                int originalIndex = indexPerm.apply(newIndex);
                int originalLabel = seq.charAt(originalIndex) - 'a';
                int newLabel = labelPerm.applyInverse(originalLabel);
                newSeq.append((char) (newLabel + 'a'));
            }
            return newSeq;
        }
    }

    @AllArgsConstructor
    private static class Permutation {
        private final int[] arr;

        public static Permutation identity(int size) {
            return new Permutation(IntStream.range(0, size).toArray());
        }

        public int apply(int i) {
            return arr[i];
        }

        public int applyInverse(int target) {
            for (int i = 0; i < arr.length; i++)
                if (arr[i] == target)
                    return i;
            throw new IndexOutOfBoundsException();
        }

        public void rotateRight(int n) {
            n = Math.floorMod(-n, arr.length);
            reverse(0, n);
            reverse(n, arr.length);
            reverse(0, arr.length);
        }

        public void reverse(int start, int end) {
            while (end - start > 1)
                swap(start++, --end);
        }

        public void swap(int i, int j) {
            int t = arr[i];
            arr[i] = arr[j];
            arr[j] = t;
        }

        public Permutation pow(long n) {
            Permutation result = Permutation.identity(arr.length);
            Permutation base = this;
            while (n > 0) {
                if ((n & 1) != 0) {
                    result = result.compose(base);
                }
                base = base.compose(base);
                n >>= 1;
            }
            return result;
        }

        private Permutation compose(Permutation other) {
            int[] newArr = new int[arr.length];
            for (int i = 0; i < arr.length; i++)
                newArr[i] = arr[other.arr[i]];
            return new Permutation(newArr);
        }
    }
}
