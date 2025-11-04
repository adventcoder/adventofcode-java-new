package adventofcode.year2018;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 2, name = "Inventory Management System")
public class Day2 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day2.class, args);
    }

    private String[] boxIds;

    @Override
    public void parse(String input) {
        boxIds = input.split("\n");
    }

    @Override
    public Integer part1() {
        int total2 = 0;
        int total3 = 0;
        for (String id : boxIds) {
            int[] counts = new int[26];
            for (int i = 0; i < id.length(); i++)
                counts[id.charAt(i) - 'a']++;

            if (IntStream.of(counts).anyMatch(n -> n == 2)) total2++;
            if (IntStream.of(counts).anyMatch(n -> n == 3)) total3++;
        }

        return total2 * total3;
    }

    @Override
    public String part2() {
        Set<String> seen = new HashSet<>();
        for (String id : boxIds) {
            for (int i = 0; i < id.length(); i++) {
                String l = id.substring(0, i);
                String r = id.substring(i + 1);
                if (!seen.add(l + "_" + r))
                    return l + r;
            }
        }
        return null;
    }
}
