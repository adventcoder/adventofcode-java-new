package adventofcode.year2017;

import java.util.InputMismatchException;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import lombok.AllArgsConstructor;

@Puzzle(day = 15, name = "Dueling Generators")
public class Day15 extends AbstractDay {
    private static final int modulus = 2147483647;
    private static final int aFactor = 16807;
    private static final int bFactor = 48271;

    private int aSeed;
    private int bSeed;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");
        aSeed = parseSeed(lines[0], "A");
        bSeed = parseSeed(lines[1], "B");
    }

    private int parseSeed(String line, String name) {
        String[] tokens = line.trim().split("\\s+");
        if (!tokens[1].equals(name))
            throw new InputMismatchException();
        return Integer.parseInt(tokens[4]);
    }

    @Override
    public Integer part1() {
        return judge(A(), B(), 40_000_000);
    }

    @Override
    public Integer part2() {
        return judge(A().filter(x -> x % 4 == 0), B().filter(x -> x % 8 == 0), 5_000_000);
    }

    private int judge(IntSupplier A, IntSupplier B, int trials) {
        int count = 0;
        for (int i = 0; i < trials; i++)
            if ((A.getAsInt() & 0xFFFF) == (B.getAsInt() & 0xFFFF))
                count++;
        return count;
    }

    private Generator A() {
        return new Generator(aFactor, aSeed);
    }

    private Generator B() {
        return new Generator(bFactor, bSeed);
    }

    @AllArgsConstructor
    private static class Generator implements IntSupplier {
        private final int factor;
        private int value;

        @Override
        public int getAsInt() {
            value = (int) ((long) value * factor % modulus);
            return value;
        }

        public IntSupplier filter(IntPredicate pred) {
            return () -> {
                int value = getAsInt();
                while (!pred.test(value))
                    value = getAsInt();
                return value;
            };
        }
    }
}
