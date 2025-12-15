package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.LongInterval;

@Puzzle(day = 5, name = "Cafeteria")
public class Day5 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day5.class, args);
    }

    private List<LongInterval> ivals;
    private List<Long> vals;

    @Override
    public void parse(String input) {
        String[] chunks = input.split("\n\n");

        ivals = new ArrayList<>();
        for (String line : chunks[0].split("\n"))
            ivals.add(Fn.parseLongPair(line, "-", LongInterval::new));
        ivals = union(ivals);

        vals = new ArrayList<>();
        for (String line : chunks[1].split("\n"))
            vals.add(Long.parseLong(line.trim()));
    }

    private static List<LongInterval> union(List<LongInterval> ivals) {
        ivals.sort(Comparator.comparingLong(LongInterval::min));
        List<LongInterval> result = new ArrayList<>();
        LongInterval curr = ivals.get(0);
        for (int i = 1; i < ivals.size(); i++) {
            LongInterval next = ivals.get(i);
            if (next.min() <= curr.max() + 1) {
                curr = new LongInterval(curr.min(), Math.max(curr.max(), next.max()));
            } else {
                result.add(curr);
                curr = next;
            }
        }
        result.add(curr);
        return result;
    }

    @Override
    public Integer part1() {
        return Fn.count(vals, this::contains);
    }

    private boolean contains(Long val) {
        int i = Fn.bisectRight(ivals, val, LongInterval::min, Comparator.naturalOrder()) - 1;
        return i >= 0 && val <= ivals.get(i).max();
    }

    public Long part2() {
        return Fn.sumLong(ivals, LongInterval::size);
    }
}
