package adventofcode.year2018;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Rect;

//TODO: revisit this
@Puzzle(day = 3, name = "No Matter How You Slice It")
public class Day3 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day3.class, args);
    }

    private Map<Integer, Rect> claims;

    @Override
    public void parse(String input) {
        claims = new HashMap<>();
        for (String line : input.split("\n")) {
            int[] vals = Fn.findAll(line, "\\d+").mapToInt(m -> Integer.parseInt(m.group())).toArray();
            int id = vals[0], x = vals[1], y = vals[2], w = vals[3], h = vals[4];
            claims.put(id, new Rect(x, y, x + w - 1, y + h - 1));
        }
    }

    @Override
    public Integer part1() {
        Rect bounds = Fn.reduce(claims.values(), Rect::or);

        int[][] grid = new int[bounds.height()][bounds.width()];
        for (Rect r : claims.values())
            for (int y = r.yMin; y <= r.yMax; y++)
                for (int x = r.xMin; x <= r.xMax; x++)
                    grid[y - bounds.yMin][x - bounds.xMin]++;

        return Stream.of(grid)
            .mapToInt(row -> IntStream.of(row).map(x -> x > 1 ? 1 : 0).sum())
            .sum();
    }

    @Override
    public Integer part2() {
        return claims.keySet().stream()
            .filter(this::overlapsNone)
            .findFirst().orElse(null);
    }

    private boolean overlapsNone(Integer id) {
        Rect claim = claims.get(id);
        for (Integer oid : claims.keySet())
            if (!id.equals(oid) && claim.overlaps(claims.get(oid)))
                return false;
        return true;
    }
}
