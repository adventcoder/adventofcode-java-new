package adventofcode.year2017;

import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.collect.Traversable;
import adventofcode.utils.geom.Dir4;
import adventofcode.utils.geom.Point;

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
        path().forEach(pos -> {
            char c = charAt(pos);
            if (Character.isLetter(c))
                seq.append(c);
        });
        return seq.toString();
    }

    @Override
    public Long part2() {
        return path().count();
    }

    private Traversable<Point> path() {
        return action -> {
            Point pos = new Point(startX, 0);
            Dir4 dir = Dir4.SOUTH;
            while (charAt(pos) != ' ') {
                action.accept(pos);
                if (charAt(pos) == '+')
                    dir = charAt(pos.neighbour(dir.left90())) != ' ' ? dir.left90() : dir.right90();
                pos = pos.neighbour(dir);
            }
        };
    }

    private char charAt(Point pos) {
        return grid[pos.y].charAt(pos.x);
    }
}
