package adventofcode.year2017;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.year2017.utils.KnotHash;

@Puzzle(day = 14, name = "Disk Defragmentation")
public class Day14 extends AbstractDay {
    private int[][] grid;

    @Override
    public void parse(String key) {
        grid = new int[128][];
        for (int y = 0; y < grid.length; y++) {
            byte[] hash = KnotHash.standard(key + "-" + y).toByteArray();
            grid[y] = new int[128];
            for (int i = 0; i < hash.length; i++) {
                int b = Byte.toUnsignedInt(hash[i]);
                for (int j = 0; j < 8; j++) {
                    int x = (i << 3) | j;
                    grid[y][x] = (b >>> (7 - j)) & 1;
                }
            }
        }
    }

    @Override
    public Integer part1() {
        return Stream.of(grid)
            .mapToInt(row -> IntStream.of(row).sum())
            .sum();
    }

    @Override
    public Integer part2() {
        int[][] regions = new int[128][];
        for (int i = 0; i < regions.length; i++)
            regions[i] = new int[128];

        int numRegions = 0;
        for (int y = 0; y < regions.length; y++)
            for (int x = 0; x < regions[y].length; x++)
                if (unmarked(regions, x, y))
                    mark(regions, x, y, ++numRegions);

        return numRegions;
    }

    private void mark(int[][] regions, int x, int y, int region) {
        regions[y][x] = region;
        if (x - 1 >= 0 && unmarked(regions, x - 1, y))
            mark(regions, x - 1, y, region);
        if (x + 1 < 128 && unmarked(regions, x + 1, y))
            mark(regions, x + 1, y, region);
        if (y - 1 >= 0 && unmarked(regions, x, y - 1))
            mark(regions, x, y - 1, region);
        if (y + 1 < 128 && unmarked(regions, x, y + 1))
            mark(regions, x, y + 1, region);
    }

    private boolean unmarked(int[][] regions, int x, int y) {
        return grid[y][x] != 0 && regions[y][x] == 0;
    }
}
