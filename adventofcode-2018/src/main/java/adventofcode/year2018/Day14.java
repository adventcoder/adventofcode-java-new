package adventofcode.year2018;

import java.util.Arrays;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 14, name = "Chocolate Charts")
public class Day14 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day14.class, args);
    }

    private String target;
    private Board board;

    @Override
    public void parse(String input) {
        this.target = input.trim();
        this.board = new Board(1024);
    }

    @Override
    public String part1() {
        int offset = Integer.parseInt(target);
        return board.subSequence(offset, 10);
    }

    @Override
    public Integer part2() {
        return board.indexOf(target);
    }

    public static class Board {
        private byte[] recipes = { 3, 7 };
        private int size = 2;
        private int a = 0;
        private int b = 1;

        public Board(int initialCapacity) {
            recipes = Arrays.copyOf(recipes, initialCapacity);
        }

        private void grow() {
            int n = recipes[a] + recipes[b];
            if (n >= 10) add(n / 10);
            add(n % 10);
            a = (a + recipes[a] + 1) % size;
            b = (b + recipes[b] + 1) % size;
        }

        private void add(int recipe) {
            if (size == recipes.length)
                recipes = Arrays.copyOf(recipes, recipes.length * 2);
            recipes[size++] = (byte) recipe;
        }

        public String subSequence(int offset, int length) {
            while (size < offset + length)
                grow();
            char[] seq = new char[length];
            for (int i = 0; i < length; i++)
                seq[i] = (char) (recipes[offset + i] + '0');
            return new String(seq);
        }

        public int indexOf(String seq) {
            int[][] dfa = buildDFA(seq);
            int state = 0;
            int i = 0;
            while (true) {
                while (i < size) {
                    if (state == seq.length())
                        return i - seq.length();
                    state = dfa[state][recipes[i++]];
                }
                grow();
            }
        }

        private int[][] buildDFA(String s) {
            int[][] dfa = new int[s.length()][10];

            // Base case: from state 0, only match advances.
            dfa[0][s.charAt(0) - '0'] = 1;

            int restart = 0;  // KMP's "X" state: the fallback state

            for (int state = 1; state < s.length(); state++) {
                // Copy mismatch transitions from the restart state
                for (int d = 0; d < 10; d++)
                    dfa[state][d] = dfa[restart][d];

                int d = s.charAt(state) - '0';
                // Match transition: advance one character
                dfa[state][d] = state + 1;
                // Update restart state for next iteration
                restart = dfa[restart][d];
            }

            return dfa;
        }
    }
}
