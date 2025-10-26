package adventofcode.year2017;

import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 1, name = "Inverse Captcha")
public class Day1 extends AbstractDay {
    private int[] digits;

    @Override
    public void parse(String input) {
        digits = input.chars()
            .filter(Character::isDigit)
            .map(c -> Character.digit(c, 10))
            .toArray();
    }

    @Override
    public Integer part1() {
        return captcha(1);
    }

    @Override
    public Integer part2() {
        return captcha(digits.length / 2);
    }

    private int captcha(int step) {
        return IntStream.range(0, digits.length)
            .filter(i -> digits[i] == digits[(i + step) % digits.length])
            .sum();
    }
}
