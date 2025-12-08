package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.DefaultHashMap;
import it.unimi.dsi.fastutil.longs.LongLongPair;

@Puzzle(day = 2, name = "Gift Shop")
public class Day2 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day2.class, args);
    }

    private Map<Integer, List<LongLongPair>> ranges;
    private long[] pow10;
    private int[] mu;

    @Override
    public void parse(String input) {
        String[][] pairs = Fn.parseVals(input, ",", s -> s.split("-")).toArray(String[][]::new);

        int maxCount = 0;
        for (String[] pair : pairs) {
            int aCount = pair[0].length(), bCount = pair[1].length();
            maxCount = Math.max(maxCount, Math.max(aCount, bCount));
        }

        precomputePowers(maxCount);
        precomputeMu(maxCount);

        // now actually parse the ranges, splitting and grouping by digit count
        ranges = new DefaultHashMap<>(ArrayList::new);
        for (String[] pair : pairs)
            addRange(Long.parseLong(pair[0]), pair[0].length(), Long.parseLong(pair[1]), pair[1].length());
    }

    private void addRange(long a, int aCount, long b, int bCount) {
        if (aCount == bCount) {
            ranges.get(aCount).add(LongLongPair.of(a, b));
        } else {
            ranges.get(aCount).add(LongLongPair.of(a, pow10[aCount] - 1));
            for (int n = aCount + 1; n < bCount; n++)
                ranges.get(n).add(LongLongPair.of(pow10[n - 1], pow10[n] - 1)); 
            ranges.get(bCount).add(LongLongPair.of(pow10[bCount - 1], b));
        }
    }

    private void precomputePowers(int max) {
        pow10 = new long[max + 1];
        pow10[0] = 1;
        for (int n = 1; n <= max; n++)
            pow10[n] = pow10[n - 1] * 10;
    }

    private void precomputeMu(int max) {
        mu = new int[max + 1];
        for (int n = 1; n <= max; n++)
            mu[n] = 1;
        boolean[] seen = new boolean[max + 1];
        for (int p = 2; p <= max; p++) {
            if (!seen[p]) {
                for (int n = p; n <= max; n += p) {
                    mu[n] *= -1;
                    seen[n] = true;
                }
                for (int n = p*p; n <= max; n += p*p)
                    mu[n] = 0;
            }
        }
    }

    @Override
    public Long part1() {
        long total = 0;
        for (int n : ranges.keySet())
            if (n % 2 == 0)
                total += sumInvalid(n, n / 2);
        return total;
    }

    @Override
    public Long part2() {
        long total = 0;
        for (int n : ranges.keySet())
            total += sumInvalid(n, n) - sumInvalidAll(n); // add back in the the full number group which isn't actually invalid
        return total;
    }

    private long sumInvalidAll(int n) {
        // The moebius function handles inclusion/exclusion for cases like 1111 that would count toward all of 1,2,4 groups.
        long total = 0;
        for (int d1 = 1; d1*d1 <= n; d1++) {
            if (n % d1 != 0) continue;
            int d2 = n / d1;
            if (mu[d1] != 0)
                total += mu[d1] * sumInvalid(n, d2);
            if (mu[d2] != 0 && d1 != d2)
                total += mu[d2] * sumInvalid(n, d1);
        }
        return total;
    }

    private long sumInvalid(int n, int d) {
        long total = 0;
        long m = (pow10[n] - 1) / (pow10[d] - 1);
        for (var range : ranges.get(n))
            total += sumMultiples(range.leftLong(), range.rightLong(), m);
        return total;
    }

    private long sumMultiples(long a, long b, long m) {
        long i = Math.floorDiv(a - 1, m);
        long j = Math.floorDiv(b, m);
        return m*(j*(j+1)/2 - i*(i+1)/2);
    }
}
