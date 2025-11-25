package adventofcode.year2017;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Dir4;
import adventofcode.utils.geom.Point;
import adventofcode.utils.iter.Enumerable;

@Puzzle(day = 19, name = "A Series of Tubes")
public class Day19 extends AbstractDay {
    private String[] grid;

    @Override
    public void parse(String input) {
        grid = input.split("\n");
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

    private Enumerable<Point> path() {
        return action -> {
            Point pos = findStart();
            Dir4 dir = Dir4.SOUTH;
            while (charAt(pos) != ' ') {
                action.accept(pos);
                if (charAt(pos) == '+')
                    dir = charAt(pos.neighbour(dir.left90())) != ' ' ? dir.left90() : dir.right90();
                pos = pos.neighbour(dir);
            }
        };
    }

    private Point findStart() {
        for (int x = 0; x < grid[0].length(); x++)
            if (grid[0].charAt(x) == '|')
                return new Point(x, 0);
        return null;
    }

    private char charAt(Point pos) {
        return grid[pos.y].charAt(pos.x);
    }
}
