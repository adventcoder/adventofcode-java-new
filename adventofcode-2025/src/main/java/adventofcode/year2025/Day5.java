package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Puzzle(day = 5, name = "Cafeteria")
public class Day5 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day5.class, args);
    }

    private List<Interval> ivs;
    private List<Long> vals;

    @Override
    public void parse(String input) {
        String[] chunks = input.split("\n\n");

        ivs = new ArrayList<>();
        for (String line : chunks[0].split("\n"))
            ivs.add(Interval.parse(line.trim()));
        ivs = merge(ivs);

        vals = new ArrayList<>();
        for (String line : chunks[1].split("\n"))
            vals.add(Long.parseLong(line.trim()));
    }

    @Override
    public Integer part1() {
        // could binary search here...
        return Fn.count(vals, val -> Fn.any(ivs, iv -> iv.contains(val)));
    }

    public Long part2() {
        return Fn.sumLong(ivs, Interval::size);
    }

    @AllArgsConstructor
    @ToString
    public static class Interval {
        public final long min;
        public final long max;

        public static Interval parse(String s) {
            String[] pair = s.split("-");
            return new Interval(Long.parseLong(pair[0]), Long.parseLong(pair[1]));
        }

        public long size() {
            return max - min + 1;
        }

        public boolean contains(long val) {
            return val >= min && val <= max;
        }
    }

    private static List<Interval> merge(List<Interval> ivs) {
        List<Interval> merged = new ArrayList<>();
        ivs.sort(Comparator.comparingLong(iv -> iv.min));
        Interval curr = ivs.get(0);
        for (int i = 1; i < ivs.size(); i++) {
            Interval next = ivs.get(i);
            if (next.min <= curr.max + 1) {
                curr = new Interval(curr.min, Math.max(curr.max, next.max));
            } else {
                merged.add(curr);
                curr = next;
            }
        }
        merged.add(curr);
        return merged;
    }
}
