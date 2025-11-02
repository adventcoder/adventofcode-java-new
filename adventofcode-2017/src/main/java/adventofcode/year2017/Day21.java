package adventofcode.year2017;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Vec2;
import lombok.AllArgsConstructor;

@Puzzle(day = 21, name = "Fractal Art")
public class Day21 extends AbstractDay {
    private Map<Grid, Grid> rules = new HashMap<>();
    private Map<List<Object>, Integer> memo = new HashMap<>();

    @Override
    public void parse(String input) {
        for (String line : input.split("\n")) {
            String[] pair = line.split("=>");
            Grid lhs = Grid.parse(pair[0].trim());
            Grid rhs = Grid.parse(pair[1].trim());
            for (Grid s : lhs.symmetries())
                rules.put(s, rhs);
        }
    }

    @Override
    public Integer part1() {
        Grid grid = Grid.parse(".#./..#/###");
        for (int i = 0; i < 5; i++)
            grid = expand(grid);
        return grid.count('#');
    }

    @Override
    public Integer part2() {
        Grid grid = Grid.parse(".#./..#/###");
        return countMemoized(grid, 18);
    }

    private int countMemoized(Grid grid, int n) {
        List<Object> key = List.of(grid, n);
        Integer value = memo.get(key);
        if (value == null) {
            value = count(grid, n);
            memo.put(key, value);
        }
        return value;
    }

    private int count(Grid grid, int n) {
        if (n == 0)
            return grid.count('#');
        Grid newGrid = expand(expand(expand(grid)));
        int total = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                total += countMemoized(newGrid.slice(i * 3, j * 3, 3), n - 3);
        return total;
    }

    private Grid expand(Grid grid) {
        int sliceSize = grid.size % 2 == 0 ? 2 : 3;
        int newSliceSize = sliceSize + 1;
        int n = grid.size / sliceSize;
        Grid newGrid = new Grid(newSliceSize * n);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                Grid subGrid = grid.slice(i * sliceSize, j * sliceSize, sliceSize);
                newGrid.setSlice(i * newSliceSize, j * newSliceSize, rules.get(subGrid));
            }
        }
        return newGrid;
    }

    @AllArgsConstructor
    public static class Grid {
        private final char[][] data;
        private final int startX;
        private final int startY;
        public final int size;

        public Grid(int size) {
            this.size = size;
            data = new char[size][size];
            startX = 0;
            startY = 0;
        }

        public char get(int x, int y) {
            return data[startY + y][startX + x];
        }

        public void set(int x, int y, char c) {
            data[startY + y][startX + x] = c;
        }

        public Grid slice(int x, int y, int sliceSize) {
            return new Grid(data, startX + x, startY + y, sliceSize);
        }

        public void setSlice(int destX, int destY, Grid slice) {
            for (int y = 0; y < slice.size; y++)
                System.arraycopy(slice.data[slice.startY + y], slice.startX, data[destY + y], destX, slice.size);
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Grid other)) return false;
            if (size != other.size) return false;
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++)
                    if (get(x, y) != other.get(x, y)) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int acc = 1;
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++)
                    acc = acc * 31 + get(x, y);
            return acc;
        }

        public static Grid parse(String input) {
            String[] rows = input.split("/");
            Grid grid = new Grid(rows.length);
            for (int y = 0; y < rows.length; y++)
                for (int x = 0; x < rows[y].length(); x++)
                    grid.set(x, y, rows[y].charAt(x));
            return grid;
        }

        public int count(char c) {
            int count = 0;
            for (int y = 0; y < size; y++)
                for (int x = 0; x < size; x++)
                    if (get(x, y) == c)
                        count++;
            return count;
        }

        public List<Grid> symmetries() {
            return List.of(
                this,
                transform(p -> new Vec2(size - 1 - p.y, p.x)),            // rotate 90
                transform(p -> new Vec2(size - 1 - p.x, size - 1 - p.y)), // rotate 180
                transform(p -> new Vec2(p.y, size - 1 - p.x)),            // rotate 270
                transform(p -> new Vec2(size - 1 - p.x, p.y)),            // flip horizontal
                transform(p -> new Vec2(p.x, size - 1 - p.y)),            // flip vertical
                transform(p -> new Vec2(p.y, p.x)),                       // flip main diagonal
                transform(p -> new Vec2(size - 1 - p.y, size - 1 - p.x))  // flip anti-diagonal
            );
        }

        public Grid transform(UnaryOperator<Vec2> op) {
            Grid result = new Grid(size);
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    Vec2 mapped = op.apply(new Vec2(x, y));
                    result.set(mapped.x, mapped.y, get(x, y));
                }
            }
            return result;
        }
    }
}
