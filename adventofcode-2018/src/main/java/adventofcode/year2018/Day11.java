package adventofcode.year2018;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 11, name = "Chronal Charge")
public class Day11 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day11.class, args);
    }

    private static int size = 300;

    // powerTotals[h][w] gives the total power in the (wxh) rect
    private int[][] powerTotals;

    @Override
    public void parse(String input) {
        int serialNo = Integer.parseInt(input.trim());
        powerTotals = new int[size + 1][size + 1];
        for (int y = 1; y <= size; y++) {
            for (int x = 1; x <= size; x++) {
                int rackId = x + 10;
                int n = (rackId * y + serialNo) * rackId;
                int d = n / 100 % 10;
                powerTotals[y][x] = d - 5;
            }
        }
        for (int y = 1; y <= size; y++)
            for (int x = 1; x <= size; x++)
                powerTotals[y][x] += powerTotals[y][x - 1];
        for (int y = 1; y <= size; y++)
            for (int x = 1; x <= size; x++)
                powerTotals[y][x] += powerTotals[y - 1][x];
    }

    public int powerTotal(int x, int y, int n) {
        return powerTotals[y + n][x + n] - powerTotals[y][x + n] - powerTotals[y + n][x] + powerTotals[y][x];
    }

    @Override
    public String part1() {
        int maxTotal = Integer.MIN_VALUE;
        String coord = null;
        for (int y = 0; y <= size - 3; y++) {
            for (int x = 0; x <= size - 3; x++) {
                int total = powerTotal(x, y, 3);
                if (coord == null || total > maxTotal) {
                    maxTotal = total;
                    coord = String.format("%d,%d", x + 1, y + 1);
                }
            }
        }
        return coord;
    }

    @Override
    public String part2() {
        int maxTotal = Integer.MIN_VALUE;
        String coord = null;
        for (int n = 1; n <= size; n++) {
            if (4 * n * n <= maxTotal) continue; // the max possible for this square size
            for (int y = 0; y <= size - n; y++) {
                for (int x = 0; x <= size - n; x++) {
                    int total = powerTotal(x, y, n);
                    if (coord == null || total > maxTotal) {
                        maxTotal = total;
                        coord = String.format("%d,%d,%d", x + 1, y + 1, n);
                    }
                }
            }
        }
        return coord;
    }
}
