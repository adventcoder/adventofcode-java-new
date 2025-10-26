package adventofcode.year2017;

import java.util.HashMap;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Vec2;

@Puzzle(day = 3, name = "Spiral Memory")
public class Day3 extends AbstractDay {
    private int n;

    @Override
    public void parse(String input) {
        this.n = Integer.parseInt(input.trim());
    }

    @Override
    public Integer part1() {
        return pos(n - 1).abs();
    }

    @Override
    public Integer part2() {
        var grid = new HashMap<Vec2, Integer>();
        grid.put(new Vec2(0, 0), 1);
        for (int i = 1; ; i++) {
            Vec2 p = pos(i);
            int newVal = 0;
            for (int dy = -1; dy <= 1; dy++)
                for (int dx = -1; dx <= 1; dx++)
                    newVal += grid.getOrDefault(new Vec2(p.x + dx , p.y + dy), 0);
            if (newVal > n)
                return newVal;
            grid.put(p, newVal);
        }
    }

    private Vec2 pos(int i) {
        int r = ((int) Math.sqrt(i) + 1) / 2;
        int size = 2*r;
        int offset = i - (size - 1)*(size - 1);
        int side = offset / size;
        offset %= size;
        if (side == 0)
            return new Vec2(r, r - 1 - offset);
        else if (side == 1)
            return new Vec2(r - 1 - offset, -r);
        else if (side == 2)
            return new Vec2(-r, -r + 1 + offset);
        else
            return new Vec2(-r + 1 + offset, r);
    }
}
