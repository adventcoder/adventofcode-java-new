package adventofcode.year2025;

import java.util.stream.LongStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Grid;
import adventofcode.utils.geom.Point;

@Puzzle(day = 7, name = "Laboratories")
public class Day7 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day7.class, args);
    }

    private Grid grid;

    @Override
    public void parse(String input) {
        grid = Grid.parse(input);
    }

    @Override
    public Integer part1() {
        Point start = grid.find('S');
        boolean[][] occupied = new boolean[grid.height][grid.width];
        occupied[start.y][start.x] = true;
        int splits = 0;
        for (int y = start.y + 1; y < grid.height; y++) {
            for (int x = 0; x < grid.width; x++) {
                if (!occupied[y - 1][x]) continue;
                if (grid.get(x, y) == '^') {
                    splits++;
                    occupied[y][x - 1] = true;
                    occupied[y][x + 1] = true;
                } else {
                    occupied[y][x] = true;
                }
            }
        }
        return splits;
    }

    @Override
    public Long part2() {
        Point start = grid.find('S');
        long[][] counts = new long[grid.height][grid.width];
        counts[start.y][start.x] = 1L;
        for (int y = start.y + 1; y < grid.height; y++) {
            for (int x = 0; x < grid.width; x++) {
                long n = counts[y - 1][x];
                if (n == 0) continue;
                if (grid.get(x, y) == '^') {
                    counts[y][x - 1] += n;
                    counts[y][x + 1] += n;
                } else {
                    counts[y][x] += n;
                }
            }
        }
        return LongStream.of(counts[grid.height - 1]).sum();
    }
}
