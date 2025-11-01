package adventofcode.year2017;

import java.util.HashMap;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Puzzle(day = 6, name = "Memory Reallocation")
public class Day6 extends AbstractDay {
    private Map<Banks, Integer> seen;
    private Banks curr;

    @Override
    public void parse(String input) {
        seen = new HashMap<>();
        curr = new Banks(input);
    }

    @Override
    public Integer part1() {
        while (!seen.containsKey(curr)) {
            seen.put(new Banks(curr), seen.size());
            curr.redistribute();
        }
        return seen.size();
    }

    @Override
    public Integer part2() {
        return seen.size() - seen.get(curr);
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Banks {
        private final int[] banks;

        public Banks(String input) {
            this(Fn.parseInts(input, "\\s+"));
        }

        public Banks(Banks other) {
            this(other.banks.clone());
        }

        public void redistribute() {
            int i = findBankToRedistribute();

            int remaining = banks[i];
            banks[i] = 0;

            addAll(remaining / banks.length);
            remaining %= banks.length;

            while (remaining > 0) {
                i = (i + 1) % banks.length;
                banks[i]++;
                remaining--;
            }
        }

        private int findBankToRedistribute() {
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

        private void addAll(int amount) {
            for (int i = 0; i < banks.length; i++)
                banks[i] += amount;
        }
    }
}
