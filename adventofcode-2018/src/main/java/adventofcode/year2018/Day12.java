package adventofcode.year2018;

import java.util.Arrays;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 12, name = "Subterranean Sustainability")
public class Day12 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day12.class, args);
    }

    private State initialState;
    private byte[] rules;

    @Override
    public void parse(String input) {
        String[] chunks = input.split("\n\n");
        initialState = State.parse(chunks[0].substring("initial state:".length()).trim());
        rules = new byte[32];
        for (String line : chunks[1].split("\n")) {
            String[] pair = line.split("=>");
            rules[parseRule(pair[0].trim())] = (byte) parseRule(pair[1].trim());
        }
    }

    private int parseRule(String s) {
        int r = 0;
        for (int i = 0; i < s.length(); i++)
            r = (r << 1) | (s.charAt(i) == '#' ? 1 : 0);
        return r;
    }

    @Override
    public Integer part1() {
        State state = initialState;
        for (int i = 0; i < 20; i++)
            state = state.tick(rules);
        return state.sum();
    }

    @Override
    public Object part2() {
        return null;
    }

    public static class State {
        private int offset;
        private byte[] state;

        public State(int offset, byte[] state) {
            int start = 0;
            while (state[start] == 0) start++;
            int end = state.length;
            while (state[end - 1] == 0) end--;
            this.state = Arrays.copyOfRange(state, start, end);
            this.offset = offset + start;
        }

        public static State parse(String s) {
            byte[] state = new byte[s.length()];
            for (int i = 0; i < s.length(); i++)
                state[i] = (byte) (s.charAt(i) == '#' ? 1 : 0);
            return new State(0, state);
        }

        public int sum() {
            int total = 0;
            for (int i = 0; i < state.length; i++)
                total += state[i] * (offset + i);
            return total;
        }

        public State tick(byte[] rules) {
            byte[] newState = new byte[state.length + 4];
            int r = 0;
            for (int i = 0; i < newState.length; i++) {
                r = ((r << 1) | (i < state.length ? state[i] : 0)) & 0x1F;
                newState[i] = rules[r];
            }
            return new State(offset - 2, newState);
        }
    }
}
