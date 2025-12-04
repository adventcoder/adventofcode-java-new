package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Dir8;
import adventofcode.utils.geom.Point;

@Puzzle(day = 4, name = "Printing Department")
public class Day4 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day4.class, args);
    }

    private char[][] grid;

    @Override
    public void parse(String input) {
        grid = Stream.of(input.split("\n"))
            .map(String::toCharArray)
            .toArray(char[][]::new);
    }

    @Override
    public Integer part1() {
        List<Point> removals = new ArrayList<>();
        findAccessible(removals);
        return removals.size();
    }

    @Override
    public Integer part2() {
        int total = 0;
        List<Point> removals = new ArrayList<>();
        findAccessible(removals);
        while (!removals.isEmpty()) {
            total += removals.size();
            for (Point p : removals)
                grid[p.y][p.x] = 'x';
            removals.clear();
            findAccessible(removals);
        }
        return total;
    }

    private List<Point> findAccessible(List<Point> removals) {
        for (int y = 0; y < grid.length; y++)
            for (int x = 0; x < grid[y].length; x++)
                if (grid[y][x] == '@' && neighbourCount(x, y) < 4)
                    removals.add(new Point(x, y));
        return removals;
    }

    private int neighbourCount(int x, int y) {
        int count = 0;
        for (Dir8 d : Dir8.values) {
            int nx = x + d.x, ny = y + d.y;
            if (ny >= 0 && ny < grid.length && nx >= 0 && nx < grid[ny].length && grid[ny][nx] == '@')
                count++;
        }
        return count;
    }

}
