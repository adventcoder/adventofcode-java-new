package adventofcode.year2017;

import java.util.HashMap;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.IntArray;

@Puzzle(day = 6, name = "Memory Reallocation")
public class Day6 extends AbstractDay {
    private Map<IntArray, Integer> seen = new HashMap<>();
    private IntArray curr;

    @Override
    public void parse(String input) {
        curr = new IntArray(Fn.parseInts(input, "\\s+"));
    }

    @Override
    public Integer part1() {
        while (!seen.containsKey(curr)) {
            seen.put(curr.clone(), seen.size());
            step(curr);
        }
        return seen.size();
    }

    @Override
    public Integer part2() {
        return seen.size() - seen.get(curr);
    }

    private void step(IntArray banks) {
        int amount = banks.max();
        int i = banks.index(amount);
        banks.set(i, 0);
        banks.incAll(amount / banks.length());
        amount %= banks.length();
        while (amount-- > 0) {
            i = (i + 1) % banks.length();
            banks.inc(i, 1);
        }
    }
}
