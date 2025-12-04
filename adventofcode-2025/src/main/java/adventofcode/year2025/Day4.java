package adventofcode.year2025;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.stream.Stream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
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
        int accessed = 0;
        for (int y = 0; y < grid.length; y++)
            for (int x = 0; x < grid[y].length; x++)
                if (grid[y][x] == '@' && neighbourCount(x, y) <= 3)
                    accessed++;
        return accessed;
    }

    @Override
    public Integer part2() {
        Queue<Point> queue = new ArrayDeque<>();
        int[][] counts = new int[grid.length][];
        for (int y = 0; y < grid.length; y++) {
            counts[y] = new int[grid[y].length];
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == '@') {
                    counts[y][x] = neighbourCount(x, y);
                    if (counts[y][x] <= 3)
                        queue.add(new Point(x, y));
                }
            }
        }

        int removed = 0;
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            removed++;
            for (Point n : p.neighbours8()) {
                if (occupied(n.x, n.y)) {
                    counts[n.y][n.x]--;
                    if (counts[n.y][n.x] == 3)
                        queue.add(n);
                }
            }
        }
        return removed;
    }

    private int neighbourCount(int x, int y) {
        int count = 0;
        for (Point n : new Point(x, y).neighbours8())
            if (occupied(n.x, n.y))
                count++;
        return count;
    }

    private boolean occupied(int x, int y) {
        return y >= 0 && y < grid.length && x >= 0 && x < grid[y].length && grid[y][x] == '@';
    }
}
