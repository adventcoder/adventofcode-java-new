package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 3, name = "Lobby")
public class Day3 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day3.class, args);
    }

    List<int[]> banks;

    @Override
    public void parse(String input) {
        banks = new ArrayList<>();
        for (String line : input.split("\n"))
            banks.add(line.chars().map(c -> c - '0').toArray());
    }

    @Override
    public Long part1() {
        return Fn.sumLong(banks, bank -> maxJoltage(bank, 2));
    }

    @Override
    public Long part2() {
        return Fn.sumLong(banks, bank -> maxJoltage(bank, 12));
    }

    private static long maxJoltage(int[] bank, int n) {
        int start = 0;
        long joltage = 0;
        while (n > 0) {
            int i = firstMaxIndex(bank, start, bank.length - n + 1);
            joltage = joltage*10 + bank[i];
            start = i + 1;
            n--;
        }
        return joltage;
    }

    private static int firstMaxIndex(int[] bank, int start, int end) {
        int max = bank[start];
        int maxIndex = start;
        for (int i = start + 1; i < end && max < 9; i++) {
            if (bank[i] > max) {
                max = bank[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
