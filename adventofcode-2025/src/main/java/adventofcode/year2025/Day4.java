package adventofcode.year2025;

import java.util.ArrayDeque;
import java.util.Queue;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Grid;
import adventofcode.utils.geom.Point;

@Puzzle(day = 4, name = "Printing Department")
public class Day4 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day4.class, args);
    }

    private Grid grid;
    private int[][] counts;
    private Queue<Point> queue;

    @Override
    public void parse(String input) {
        grid = Grid.parse(input);
        popCount();
    }

    private void popCount() {
        // Counts the populated cells of each 3x3 subgrid
        counts = new int[grid.height][grid.width];
        for (int y = 0; y < grid.height; y++)
            for (int x = 0; x < grid.width; x++)
                counts[y][x] = grid.get(x, y) == '@' ? 1 : 0;
        for (int y = 0; y < grid.height; y++) {
            int prev = 0;
            for (int x = 0; x < grid.width - 1; x++) {
                int curr = counts[y][x];
                counts[y][x] += prev + counts[y][x+1];
                prev = curr;
            }
            counts[y][grid.width - 1] += prev;
        }
        for (int x = 0; x < grid.width; x++) {
            int prev = 0;
            for (int y = 0; y < grid.height - 1; y++) {
                int curr = counts[y][x];
                counts[y][x] += prev + counts[y+1][x];
                prev = curr;
            }
            counts[grid.height - 1][x] += prev;
        }
    }

    @Override
    public Integer part1() {
        queue = new ArrayDeque<>();
        for (int y = 0; y < grid.height; y++)
            for (int x = 0; x < grid.width; x++)
                if (grid.get(x, y) == '@' && counts[y][x] <= 4)
                    queue.add(new Point(x, y));
        return queue.size();
    }

    @Override
    public Integer part2() {
        int removals = 0;
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            removals++;
            for (Point n : p.neighbours8())
                if (grid.getOrDefault(n.x, n.y, '.') == '@' && --counts[n.y][n.x] == 4)
                    queue.add(n);
        }
        return removals;
    }
}
