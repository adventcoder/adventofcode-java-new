package adventofcode.year2017;

import java.util.ArrayList;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 17, name = "Spinlock")
public class Day17 extends AbstractDay{
    private int step;

    @Override
    public void parse(String input) {
        step = Integer.parseInt(input.trim());
    }

    @Override
    public Integer part1() {
        List<Integer> values = new ArrayList<>();
        values.add(0);
        int pos = 0;
        for (int n = 1; n <= 2017; n++) {
            pos = (pos + step) % values.size() + 1;
            values.add(pos, n);
        }
        return values.get((pos + 1) % values.size());
    }

    @Override
    public Integer part2() {
        int valueAfterZero = 0;
        int pos = 0;
        int size = 1;
        for (int n = 1; n <= 50_000_000; n++) {
            pos = (pos + step) % size + 1;
            if (pos == 1) valueAfterZero = n;
            size++;
        }
        return valueAfterZero;
    }
}
