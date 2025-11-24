package adventofcode.year2017;

import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntArrays;
import adventofcode.utils.collect.DeepHashMap;

@Puzzle(day = 6, name = "Memory Reallocation")
public class Day6 extends AbstractDay {
    private Map<int[], Integer> seen;
    private int[] curr;

    @Override
    public void parse(String input) {
        seen = new DeepHashMap<>();
        curr = Fn.parseInts(input, "\\s+");
    }

    @Override
    public Integer part1() {
        while (!seen.containsKey(curr)) {
            seen.put(curr.clone(), seen.size());
            redistribute(curr);
        }
        return seen.size();
    }

    @Override
    public Integer part2() {
        return seen.size() - seen.get(curr);
    }

    private static void redistribute(int[] banks) {
        int i = IntArrays.maxIndex(banks);

        int remaining = banks[i];
        banks[i] = 0;

        addAll(banks, remaining / banks.length);
        remaining %= banks.length;

        while (remaining > 0) {
            i = (i + 1) % banks.length;
            banks[i]++;
            remaining--;
        }
    }

    private static void addAll(int[] banks, int amount) {
        for (int i = 0; i < banks.length; i++)
            banks[i] += amount;
    }
}
