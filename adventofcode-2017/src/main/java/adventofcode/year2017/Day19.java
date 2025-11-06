package adventofcode.year2017;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.math.IntVec2;

@Puzzle(day = 19, name = "A Series of Tubes")
public class Day19 extends AbstractDay {
    private String[] grid;
    private int startX;

    @Override
    public void parse(String input) {
        grid = input.split("\n");
        startX = IntStream.range(0, grid[0].length()).filter(i -> grid[0].charAt(i) == '|').findFirst().orElseThrow();
    }

    @Override
    public String part1() {
        StringBuilder seq = new StringBuilder();
        traverse(pos -> {
            char c = charAt(pos);
            if (Character.isLetter(c))
                seq.append(c);
        });
        return seq.toString();
    }

    @Override
    public Integer part2() {
        List<IntVec2> path = new ArrayList<>();
        traverse(path::add);
        return path.size();
    }

    private void traverse(Consumer<IntVec2> action) {
        IntVec2 pos = new IntVec2(startX, 0);
        IntVec2 dir = IntVec2.SOUTH;
        while (charAt(pos) != ' ') {
            action.accept(pos);
            if (charAt(pos) == '+')
                dir = charAt(pos.add(dir.rotateLeft())) != ' ' ? dir.rotateLeft() : dir.rotateRight();
            pos = pos.add(dir);
        }
    }

    private char charAt(IntVec2 pos) {
        return grid[pos.y].charAt(pos.x);
    }
}
