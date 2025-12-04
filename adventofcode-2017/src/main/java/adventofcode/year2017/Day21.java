package adventofcode.year2017;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.decorators.Memoized;
import adventofcode.utils.geom.Grid;
import adventofcode.utils.geom.Point;

@Puzzle(day = 21, name = "Fractal Art")
public class Day21 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day21.class, args);
    }

    private Map<Grid, Grid> rules = new HashMap<>();

    @Override
    public void parse(String input) {
        for (String line : input.split("\n")) {
            String[] pair = line.split("=>");
            Grid lhs = parseGrid(pair[0].trim());
            Grid rhs = parseGrid(pair[1].trim());
            for (Grid s : symmetries(lhs))
                rules.put(s, rhs);
        }
    }

    private Grid parseGrid(String s) {
        return new Grid(s.split("/"));
    }

    @Override
    public Integer part1() {
        Grid grid = parseGrid(".#./..#/###");
        for (int i = 0; i < 5; i++)
            grid = expand(grid);
        return grid.count('#');
    }

    @Override
    public Integer part2() {
        return count(parseGrid(".#./..#/###"), 18);
    }

    @Memoized
    protected int count(Grid grid, int n) {
        if (n < 3) {
            while (n > 0)
                grid = expand(grid);
            return grid.count('#');
        }
        Grid newGrid = expand(expand(expand(grid)));
        int total = 0;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                total += count(newGrid.slice(i * 3, j * 3, 3, 3), n - 3);
        return total;
    }

    private Grid expand(Grid grid) {
        int size = grid.width % 2 == 0 ? 2 : 3;
        int newSize = size + 1;
        int n = grid.width / size;
        Grid newGrid = new Grid(newSize * n, newSize * n);
        for (int y = 0; y < n; y++) {
            for (int x = 0; x < n; x++) {
                Grid slice = grid.slice(x * size, y * size, size, size);
                newGrid.setSlice(x * newSize, y * newSize, rules.get(slice));
            }
        }
        return newGrid;
    }

    //TODO: these could be methods on grid
    public List<Grid> symmetries(Grid g) {
        return List.of(
            g,
            transform(g, p -> new Point(g.height - 1 - p.y, p.x)),               // rotate 90
            transform(g, p -> new Point(g.width - 1 - p.x, g.height - 1 - p.y)), // rotate 180
            transform(g, p -> new Point(p.y, g.width - 1 - p.x)),                // rotate 270
            transform(g, p -> new Point(g.width - 1 - p.x, p.y)),                // flip horizontal
            transform(g, p -> new Point(p.x, g.height - 1 - p.y)),               // flip vertical
            transform(g, p -> new Point(p.y, p.x)),                              // flip main diagonal
            transform(g, p -> new Point(g.height - 1 - p.y, g.width - 1 - p.x))  // flip anti-diagonal
        );
    }

    public Grid transform(Grid g, UnaryOperator<Point> op) {
        Grid result = new Grid(g.height, g.width);
        for (int y = 0; y < g.width; y++) {
            for (int x = 0; x < g.height; x++) {
                Point mapped = op.apply(new Point(x, y));
                result.set(mapped.x, mapped.y, g.get(x, y));
            }
        }
        return result;
    }
}
