package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Interval;
import adventofcode.utils.geom.IntervalUnion;

@Puzzle(day = 5, name = "Cafeteria")
public class Day5 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day5.class, args);
    }

    private IntervalUnion intervals;
    private List<Long> vals;

    @Override
    public void parse(String input) {
        String[] chunks = input.split("\n\n");
        intervals = new IntervalUnion();
        for (String line : chunks[0].split("\n")) {
            String[] pair = line.trim().split("-");
            intervals.add(Long.parseLong(pair[0]), Long.parseLong(pair[1]));
        }
        vals = new ArrayList<>();
        for (String line : chunks[1].split("\n"))
            vals.add(Long.parseLong(line.trim()));
    }

    @Override
    public Integer part1() {
        return Fn.count(vals, intervals::contains);
    }

    public Long part2() {
        return Fn.sumLong(intervals, Interval::size);
    }
}
