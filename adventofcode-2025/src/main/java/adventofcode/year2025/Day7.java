package adventofcode.year2025;

import java.util.HashSet;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.decorators.Memoized;
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
        Set<Point> splitters = new HashSet<>();
        addSplitters(grid.find('S'), splitters);
        return splitters.size();
    }

    private void addSplitters(Point start, Set<Point> result) {
        Point splitter = nextSplitter(start);
        if (splitter != null && result.add(splitter)) {
            addSplitters(splitter.west(), result);
            addSplitters(splitter.east(), result);
        }
    }

    @Override
    public Long part2() {
        return timelines(grid.find('S'));
    }

    @Memoized
    protected long timelines(Point start) {
        Point splitter = nextSplitter(start);
        if (splitter == null) return 1L;
        return timelines(splitter.west()) + timelines(splitter.east());
    }

    private Point nextSplitter(Point start) {
        for (int y = start.y + 2; y < grid.height; y += 2)
            if (grid.get(start.x, y) == '^')
                return new Point(start.x, y);
        return null;
    }
}
