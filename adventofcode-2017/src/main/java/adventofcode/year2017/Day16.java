package adventofcode.year2017;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.collect.Range;
import adventofcode.utils.lang.IntArrays;
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
        private int[] indexPerm;
        // label -> original label
        private int[] labelPerm;

        public Dance(int size) {
            indexPerm = new Range(size).toIntArray();
            labelPerm = new Range(size).toIntArray();
        }

        public void spin(int x) {
            IntArrays.rotateRight(indexPerm, x);
        }

        public void exchange(int index1, int index2) {
            IntArrays.swap(indexPerm, index1, index2);
        }

        public void partner(char label1, char label2) {
            IntArrays.swap(labelPerm, label1 - 'a', label2 - 'a');
        }

        public Dance repeat(long n) {
            return new Dance(pow(indexPerm, n), pow(labelPerm, n));
        }

        public CharSequence apply(CharSequence seq) {
            StringBuilder newSeq = new StringBuilder(seq.length());
            for (int newIndex = 0; newIndex < seq.length(); newIndex++) {
                int originalIndex = indexPerm[newIndex];
                int originalLabel = seq.charAt(originalIndex) - 'a';
                int newLabel = IntArrays.index(labelPerm, originalLabel);
                newSeq.append((char) (newLabel + 'a'));
            }
            return newSeq;
        }
    }

    private static int[] pow(int[] base, long n) {
        int[] result = new Range(base.length).toIntArray();
        while (n > 0) {
            if ((n & 1) != 0)
                result = compose(result, base);
            base = compose(base, base);
            n >>= 1;
        }
        return result;
    }

    private static int[] compose(int[] a, int[] b) {
        int[] result = new int[b.length];
        for (int i = 0; i < b.length; i++)
            result[i] = a[b[i]];
        return result;
    }
}
