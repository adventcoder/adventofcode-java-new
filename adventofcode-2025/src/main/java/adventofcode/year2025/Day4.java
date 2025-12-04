package adventofcode.year2025;

import java.util.ArrayDeque;
import java.util.Queue;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Dir8;
import adventofcode.utils.geom.Grid;
import adventofcode.utils.geom.Point;

@Puzzle(day = 4, name = "Printing Department")
public class Day4 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day4.class, args);
    }

    private Grid grid;

    @Override
    public void parse(String input) {
        grid = Grid.parse(input);
    }

    @Override
    public Integer part1() {
        int removable = 0;
        for (int y = 0; y < grid.height; y++)
            for (int x = 0; x < grid.width; x++)
                if (grid.get(x, y) == '@' && neighbourCount(x, y) <= 3)
                    removable++;
        return removable;
    }

    @Override
    public Integer part2() {
        Queue<Point> queue = new ArrayDeque<>();
        int[][] counts = new int[grid.height][grid.width];
        for (int y = 0; y < grid.height; y++) {
            for (int x = 0; x < grid.width; x++) {
                if (grid.get(x, y) == '@') {
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
                if (grid.inBounds(n.x, n.y) && grid.get(n.x, n.y) == '@' && --counts[n.y][n.x] == 3)
                    queue.add(n);
            }
        }
        return removed;
    }

    private int neighbourCount(int x, int y) {
        int count = 0;
        for (Dir8 d : Dir8.values)
            if (grid.inBounds(x + d.x, y + d.y) && grid.get(x + d.x, y + d.y) == '@')
                count++;
        return count;
    }
}
