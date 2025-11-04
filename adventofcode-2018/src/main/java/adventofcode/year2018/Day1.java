package adventofcode.year2018;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 1, name = "Chronal Calibration")
public class Day1 extends AbstractDay {
    private int[] diffs;
    private int[] freqs;

    @Override
    public void parse(String input) {
        diffs = Fn.parseInts(input, "\n");
    }

    @Override
    public Integer part1() {
        freqs = new int[diffs.length + 1];
        for (int i = 0; i < diffs.length; i++)
            freqs[i + 1] = freqs[i] + diffs[i];
        return freqs[diffs.length];
    }

    @Override
    public Integer part2() {
        int step = freqs[diffs.length];

        // Every value increases by step every cycle.
        // Assuming there are no duplicate values in the first cycle, then our job is to find the first pair of frequencies such that freq1 + n*step = freq2, which can only happen if freq1 = freq2 mod step.

        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int freq : freqs)
            groups.computeIfAbsent(Math.floorMod(freq, step), k -> new ArrayList<>()).add(freq);

        int bestN = Integer.MAX_VALUE;
        Integer bestFreq = null;
        for (int freq1 : freqs) {
            for (int freq2 : groups.get(Math.floorMod(freq1, step))) {
                if (step > 0 ? freq2 > freq1 : freq2 < freq1) {
                    int n = (freq2 - freq1) / step;
                    if (n < bestN) {
                        bestN = n;
                        bestFreq = freq2; // the answer is the value that will eventually be reached
                    }
                }
            }
        }

        return bestFreq;
    }
}
