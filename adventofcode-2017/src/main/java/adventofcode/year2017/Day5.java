package adventofcode.year2017;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 5, name = "A Maze of Twisty Trampolines, All Alike")
public class Day5 extends AbstractDay {
    private int[] offsets;

    @Override
    public void parse(String input) {
        offsets = Fn.parseInts(input, "\n");
    }

    @Override
    public Integer part1() {
        int[] copy = offsets.clone();
        int steps = 0;
        for (int i = 0; i < copy.length; ) {
            i += copy[i]++;
            steps++;
        }
        return steps;
    }

    @Override
    public Integer part2() {
        int[] copy = offsets.clone();
        int steps = 0;
        for (int i = 0; i < copy.length; ) {
            int offset = copy[i];
            copy[i] = offset >= 3 ? offset - 1 : offset + 1;
            i += offset;
            steps++;
        }
        return steps;
    }
}
