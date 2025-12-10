package adventofcode.year2025;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 10, name = "Factory")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
    }

    // @Override
    // protected String getInput() throws IOException {
    //     return "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
    //         "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
    //         "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}";
    // }

    private List<Machine> machines;

    @Override
    public void parse(String input) {
        machines = Fn.parseVals(input, "\n", Machine::parse).toList();
    }

    @Override
    public Object part1() {
        int total = 0;
        for (Machine m : machines) {
            int leastButtons = Integer.MAX_VALUE;
            int combinations = 1 << m.buttons.length;
            for (int mask = 0; mask < combinations; mask++) {
                if (press(m.buttons, mask) == m.target)
                    leastButtons = Math.min(leastButtons, Integer.bitCount(mask));
            }
            total += leastButtons;
        }
        return total;
    }

    public int press(int[] buttons, int mask) {
        int result = 0;
        for (int i = 0; i < buttons.length; i++) {
            if ((mask & (1 << i)) != 0)
                result ^= buttons[i];
        }
        return result;
    }
    
    // @Override
    // protected String getInput() throws IOException {
    //     return "[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}\n" +
    //         "[...#.] (0,2,3,4) (2,3) (0,4) (0,1,2) (1,2,3,4) {7,5,12,7,2}\n" +
    //         "[.###.#] (0,1,2,3,4) (0,3,4) (0,1,2,4,5) (1,2) {10,11,11,5,10,5}";
    // }

    private int[] solveFancy(int[] cols, int target, int height) {
        int width = cols.length;

        // Build augmented matrix
        int[] lhs = new int[height];
        int[] rhs = new int[1];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++)
                lhs[i] |= ((cols[j] >>> i) & 1) << j;
            rhs[i] = (target >> i) & 1;
        }

        //
        // bit vectors are stored with index = 0 in lsb
        // cols = { 0b1000, 0b1010, 0b0100, 0b1100, 0b0101, 0b0011 } = { 8, 10, 4, 12, 5, 3 }
        // target = 0b0110 = 6
        //
        // [0 0 0 0 1 1] [a]   [0]
        // [0 1 0 0 0 1] [b] = [1]
        // [0 0 1 1 1 0] [c]   [1]
        // [1 1 0 1 0 0] [d]   [0]
        //               [e]
        //               [f]
        //
        // a b c d e f
        // -----------
        // 0 0 0 0 1 1 | 0
        // 0 1 0 0 0 1 | 1   augmented
        // 0 0 1 1 1 0 | 1
        // 1 1 0 1 0 0 | 0
        // 
        // a b c d e f
        // -----------
        // 1 0 0 1 0 1 | 1
        // 0 1 0 0 0 1 | 1   full row reduce
        // 0 0 1 1 1 0 | 1   rank = 4  (nullity = 2)
        // 0 0 0 0 1 1 | 0
        //
        // d, f are free
        //
        // a = 1 ^ d ^ f
        // b = 1     ^ f
        // c = 1 ^ d
        // e =         f
        //
        // 
        //
        // sols = [[1 ^ d ^ f, 1 ^ f, 1 ^ d, d, 0 ^ e, f] for d in (0, 1) for e in (0, 1)]
        //
        return null; //TODO
    }

    public record Machine(int size, int target, int[] buttons, int[] joltages, String src) {
        public static Machine parse(String s) {
            String[] tokens = s.trim().split("\\s+");
            int size = Fn.strip(tokens[0], "[", "]").length();
            int target = parseTarget(tokens[0]);
            int[] buttons = Arrays.stream(tokens, 1, tokens.length - 1).mapToInt(Machine::parseButton).toArray();
            int[] joltages = Fn.parseInts(Fn.strip(tokens[tokens.length - 1], "{", "}"), ",");
            return new Machine(size, target, buttons, joltages, s);
        }

        private static int parseTarget(String s) {
            int vec = 0;
            s = Fn.strip(s, "[", "]");
            for (int i = 0; i < s.length(); i++) {
                int bit = (s.charAt(i) == '#' ? 1 : 0);
                vec |= (bit << i);
            }
            return vec;
        }

        private static int parseButton(String s) {
            int vec = 0;
            for (int i : Fn.parseInts(Fn.strip(s, "(", ")"), ","))
                vec |= (1 << i);
            return vec;
        }
    }
}
