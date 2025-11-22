package adventofcode.year2017;

import java.util.HashMap;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.IntArrays;
import it.unimi.dsi.fastutil.ints.IntList;

@Puzzle(day = 6, name = "Memory Reallocation")
public class Day6 extends AbstractDay {
    private Map<IntList, Integer> seen;
    private int[] curr;

    @Override
    public void parse(String input) {
        seen = new HashMap<>();
        curr = Fn.parseInts(input, "\\s+");
    }

    @Override
    public Integer part1() {
        while (!seen.containsKey(IntArrays.asList(curr))) {
            seen.put(IntArrays.asList(curr.clone()), seen.size());
            redistribute(curr);
        }
        return seen.size();
    }

    @Override
    public Integer part2() {
        return seen.size() - seen.get(IntArrays.asList(curr));
    }

    private static void redistribute(int[] banks) {
        int i = findBankToRedistribute(banks);

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

    private static int findBankToRedistribute(int[] banks) {
        int maxBlocks = banks[0];
        int targetIndex = 0;
        for (int i = 1; i < banks.length; i++) {
            if (banks[i] > maxBlocks) {
                maxBlocks = banks[i];
                targetIndex = i;
            }
        }
        return targetIndex;
    }

    private static void addAll(int[] banks, int amount) {
        for (int i = 0; i < banks.length; i++)
            banks[i] += amount;
    }
}
