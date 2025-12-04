package adventofcode.year2025;

import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 3, name = "Lobby")
public class Day3 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day3.class, args);
    }

    private List<String> banks;

    @Override
    public void parse(String input) {
        banks = List.of(input.split("\n"));
    }

    @Override
    public Long part1() {
        return Fn.sumLong(banks, bank -> maxJoltage(bank, 2));
    }

    @Override
    public Long part2() {
        return Fn.sumLong(banks, bank -> maxJoltage(bank, 12));
    }

    private long maxJoltage(String bank, int n) {
        char[] stack = new char[bank.length()];
        int size = 0;
        for (int i = 0; i < bank.length(); i++) {
            char d = bank.charAt(i);
            int remaining = bank.length() - i;
            while (size > 0 && d > stack[size - 1] && size + remaining > n)
                size--;
            stack[size++] = d;
        }
        return Long.parseLong(new String(stack, 0, n));
    }
}
