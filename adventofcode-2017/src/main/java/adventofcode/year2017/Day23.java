package adventofcode.year2017;

import java.util.Arrays;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;

@Puzzle(day = 23, name = "Coprocessor Conflagration")
public class Day23 extends AbstractDay {
    private int bInitial;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");
        String[] tokens = lines[0].split("\\s+");
        bInitial = Integer.parseInt(tokens[2]);
    }

    @Override
    public Integer part1() {
        // Number of multiplies in the prime check roughly equivalent to:
        //
        // f = 1
        // for (d = 2; d != b; d++)
        //     for (e = 2; e != b; e++)
        //         if (d*e == b)
        //             f = 0
        //
        return (bInitial - 2) * (bInitial - 2);
    }

    @Override
    public Integer part2() {
        int lower  = bInitial * 100 + 100000;
        int upper = lower + 17000;
        boolean[] isPrime = sieve(upper);
        int h = 0;
        for (int b = lower; b <= upper; b += 17)
            if (!isPrime[b])
                h++;
        return h;
    }

    private static boolean[] sieve(int upper) {
        boolean[] isPrime = new boolean[upper + 1];
        Arrays.fill(isPrime, true);
        for (int d = 2; d * d <= upper; d++) {
            if (isPrime[d]) {
                for (int n = d * d; n <= upper; n += d)
                    isPrime[n] = false;
            }
        }
        return isPrime;
    }
}
