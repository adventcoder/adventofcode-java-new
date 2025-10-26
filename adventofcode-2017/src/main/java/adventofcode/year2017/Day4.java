package adventofcode.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 4, name = "High-Entropy Passphrases")
public class Day4 extends AbstractDay {
    List<List<String>> phrases = new ArrayList<>();

    @Override
    public void parse(String input) {
        for (String line : input.split("\n"))
            phrases.add(Arrays.asList(line.split("\\s+")));
    }

    @Override
    public Integer part1() {
        return Fn.count(phrases, this::valid1);
    }

    @Override
    public Integer part2() {
        return Fn.count(phrases, this::valid2);
    }

    private boolean valid1(List<String> phrase) {
        return new HashSet<>(phrase).size() == phrase.size();
    }

    private boolean valid2(List<String> phrase) {
        return valid1(phrase.stream().map(this::sortWord).toList());
    }

    private String sortWord(String word) {
        char[] cs = word.toCharArray();
        Arrays.sort(cs);
        return new String(cs);
    }
}
