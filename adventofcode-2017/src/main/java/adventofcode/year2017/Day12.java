package adventofcode.year2017;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.DisjointSet;

@Puzzle(day = 12, name = "Digital Plumber")
public class Day12 extends AbstractDay {
    private DisjointSet groups;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");
        groups = new DisjointSet(lines.length);
        for (String line : lines) {
            String[] pair = line.split("<->");
            int a = Integer.parseInt(pair[0].trim());
            for (int b : Fn.parseInts(pair[1], ","))
                groups.merge(a, b);
        }
    }

    @Override
    public Integer part1() {
        return groups.rank(0);
    }

    @Override
    public Integer part2() {
        return groups.size();
    }
}
