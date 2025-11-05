package adventofcode.year2018;

import java.util.OptionalInt;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 5, name = "Alchemical Reduction")
public class Day5 extends AbstractDay {
    private String polymer;

    @Override
    public void parse(String input) {
        polymer = input.trim();
    }

    @Override
    public Integer part1() {
        StringBuilder result = new StringBuilder(polymer);
        reduce(result);
        return result.length();
    }

    @Override
    public OptionalInt part2() {
        return IntStream.range(0, 26)
            .map(type -> {
                StringBuilder result = new StringBuilder(polymer);
                remove(result, type);
                reduce(result);
                return result.length();
            })
            .min();
    }

    private void remove(StringBuilder poly, int type) {
        int newSize = 0;
        for (int i = 0; i < poly.length(); i++) {
            char c = poly.charAt(i);
            if (Character.toLowerCase(c) == type + 'a') continue;
            poly.setCharAt(newSize++, poly.charAt(i));
        }
        poly.setLength(newSize);
    }

    private void reduce(StringBuilder poly) {
        int newSize = 0;
        for (int i = 0; i < poly.length(); i++) {
            char c = poly.charAt(i);
            if (newSize > 0 && reacts(poly.charAt(newSize - 1), c)) {
                newSize--;
            } else {
                poly.setCharAt(newSize++, c);
            }
        }
        poly.setLength(newSize);
    }

    private boolean reacts(char c1, char c2) {
        return (Character.toLowerCase(c1) == Character.toLowerCase(c2)) &&
            (Character.isLowerCase(c1) != Character.isLowerCase(c2));
    }
}
