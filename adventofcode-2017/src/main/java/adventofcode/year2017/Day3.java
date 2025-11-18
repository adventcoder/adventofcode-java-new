package adventofcode.year2017;

import java.util.HashMap;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Point;

@Puzzle(day = 3, name = "Spiral Memory")
public class Day3 extends AbstractDay {
    private int n;

    @Override
    public void parse(String input) {
        this.n = Integer.parseInt(input.trim());
    }

    @Override
    public Integer part1() {
        return pos(n - 1).distanceToOrigin();
    }

    @Override
    public Integer part2() {
        var grid = new HashMap<Point, Integer>();
        grid.put(new Point(0, 0), 1);
        for (int i = 1; ; i++) {
            Point p = pos(i);
            int newVal = Fn.sum(p.neighbours8(), n -> grid.getOrDefault(n, 0));
            if (newVal > n)
                return newVal;
            grid.put(p, newVal);
        }
    }

    private Point pos(int i) {
        int r = ((int) Math.sqrt(i) + 1) / 2;
        int size = 2*r;
        int offset = i - (size - 1)*(size - 1);
        int side = offset / size;
        offset %= size;
        if (side == 0)
            return new Point(r, r - 1 - offset);
        else if (side == 1)
            return new Point(r - 1 - offset, -r);
        else if (side == 2)
            return new Point(-r, -r + 1 + offset);
        else
            return new Point(-r + 1 + offset, r);
    }
}
