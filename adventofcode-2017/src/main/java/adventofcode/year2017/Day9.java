package adventofcode.year2017;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

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
        int garbageCount = 0;
        Matcher matcher = Pattern.compile("<([^>]*)>").matcher(stream);
        while (matcher.find())
            garbageCount += matcher.group(1).length();
        return garbageCount;
    }
}
