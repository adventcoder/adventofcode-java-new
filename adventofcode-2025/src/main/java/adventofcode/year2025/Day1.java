package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 1, name = "Secret Entrance")
public class Day1 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day1.class, args);
    }

    private List<Integer> steps = new ArrayList<>();

    @Override
    public void parse(String input) {
        steps = Fn.parseVals(input, "\n", this::parseStep).toList();
    }

    private int parseStep(String s) {
        return Integer.parseInt(s.replace('L', '-').replace('R', '+'));
    }

    @Override
    public Integer part1() {
        int pos = 50;
        int count = 0;
        for (int step : steps) {
            pos = (pos + step) % 100;
            count += (pos == 0) ? 1 : 0;
        }
        return count;
    }

    @Override
    public Integer part2() {
        int pos = 50;
        int count = 0;
        for (int step : steps) {
            count += step < 0 ? countMultiples(pos + step - 1, pos - 1, 100) : countMultiples(pos, pos + step, 100);
            pos = (pos + step) % 100;
        }
        return count;
    }

    private int countMultiples(int startExc, int endInc, int m) {
        return Math.floorDiv(endInc, m) - Math.floorDiv(startExc, m);
    }
}
