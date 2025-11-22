package adventofcode.year2018;

import java.util.OptionalLong;
import java.util.stream.LongStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 9, name = "Marble Mania")
public class Day9 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day9.class, args);
    }

    private int players;
    private int lastMarble;

    @Override
    public void parse(String input) {
        String[] tokens = input.split("\\s+");
        players = Integer.parseInt(tokens[0]);
        lastMarble = Integer.parseInt(tokens[6]);
    }

    @Override
    public OptionalLong part1() {
        return LongStream.of(finalScores()).max();
    }

    @Override
    public OptionalLong part2() {
        lastMarble *= 100;
        return LongStream.of(finalScores()).max();
    }

    public long[] finalScores() {
        long[] scores = new long[players];
        int[] left = new int[lastMarble + 1];
        int[] right = new int[lastMarble + 1];
        int curr = 0;
        int player = 0;
        for (int n = 1; n <= lastMarble; n++) {
            if (n % 23 == 0) {
                for (int i = 0; i < 7; i++)
                    curr = left[curr];
                int prev = left[curr];
                int next = right[curr];
                right[prev] = next;
                left[next] = prev;
                scores[player] += n + curr;
                curr = next;
            } else {
                curr = right[curr];
                int next = right[curr];
                left[n] = curr;
                right[n] = next;
                right[curr] = n;
                left[next] = n;
                curr = n;
            }
            player = (player + 1) % players;
        }
        return scores;
    }
}
