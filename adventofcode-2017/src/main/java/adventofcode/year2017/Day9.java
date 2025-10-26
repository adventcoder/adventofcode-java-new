package adventofcode.year2017;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.iter.Matches;

@Puzzle(day = 9, name = "Stream Processing")
public class Day9 extends AbstractDay {
    String stream;

    @Override
    public void parse(String input) {
        stream = input.trim().replaceAll("!.", "");
    }

    @Override
    public Integer part1() {
        String cleaned = stream.replaceAll("<[^>]*>", "");
        int score = 0;
        int depth = 0;
        for (int i = 0; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                score += depth--;
            }
        }
        return score;
    }

    @Override
    public Integer part2() {
        return Fn.sum(new Matches("<([^>]*)>", stream, 1), String::length);
    }
}
