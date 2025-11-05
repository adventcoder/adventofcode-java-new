package adventofcode.year2017;

import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
import lombok.AllArgsConstructor;

@Puzzle(day = 25, name = "The Halting Problem")
public class Day25 extends AbstractDay {
    private int initialState;
    private int targetSteps;
    private Rule[][] rules;

    @AllArgsConstructor
    private static class Rule {
        public final int val;
        public final int dir;
        public final int nextState;
    }

    @Override
    public void parse(String input) {
        String[] chunks = input.split("\n\n");

        int numStates = chunks.length - 1;
        rules = new Rule[numStates][2];

        parsePreamble(chunks[0]);
        for (int i = 1; i < chunks.length; i++)
            parseStateRules(chunks[i]);
    }

    private void parsePreamble(String chunk) {
        String[] lines = chunk.split("\n");
        initialState = findState(lines[0]);
        targetSteps = Integer.parseInt(find(lines[1], "\\d+"));
    }

    private void parseStateRules(String chunk) {
        String[] lines = chunk.split("\n");
        int state = findState(lines[0]);
        rules[state][findVal(lines[1])] = new Rule(findVal(lines[2]), findDir(lines[3]), findState(lines[4]));
        rules[state][findVal(lines[5])] = new Rule(findVal(lines[6]), findDir(lines[7]), findState(lines[8]));
    }

    private String find(String s, String regex) {
        Matcher matcher = Pattern.compile(regex).matcher(s);
        if (matcher.find()) return matcher.group(0);
        throw new InputMismatchException("'" + regex + "' not found in '" + s + "'");
    }

    private int findVal(String s) { return Integer.parseInt(find(s, "[0-1]")); }
    private int findDir(String s) { return find(s, "\\b(left|right)\\b").equals("left") ? -1 : 1; }
    private int findState(String s) { return find(s, "\\b[A-Z]\\b").charAt(0) - 'A'; }

    @Override
    public Integer part1() {
        Int2IntMap tape = new Int2IntOpenHashMap();
        int pos = 0;
        int state = initialState;
        for (int i = 0; i < targetSteps; i++) {
            Rule rule = rules[state][tape.getOrDefault(pos, 0)];
            tape.put(pos, rule.val);
            pos += rule.dir;
            state = rule.nextState;
        }
        return tape.values().intStream().sum();
    }
}
