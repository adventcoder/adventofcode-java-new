package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import lombok.AllArgsConstructor;

@Puzzle(day = 2, name = "Gift Shop")
public class Day2 extends AbstractDay {
    private static final int[] mu = { 0, 1, -1, -1, 0, -1, 1, -1, 0, 0, 1, -1, 0, -1, 1, 1, 0, -1, 0, -1 };
    private static final long[] pow10 = { 1L, 10L, 100L, 1_000L, 10_000L, 100_000L, 1_000_000L, 10_000_000L, 100_000_000L, 1_000_000_000L, 10_000_000_000L, 100_000_000_000L, 1_000_000_000_000L, 10_000_000_000_000L, 100_000_000_000_000L, 1_000_000_000_000_000L, 10_000_000_000_000_000L, 100_000_000_000_000_000L, 1_000_000_000_000_000_000L };

    public static void main(String[] args) throws Exception {
        main(Day2.class, args);
    }

    private List<IdRange> ranges;

    @Override
    public void parse(String input) {
        ranges = new ArrayList<>();
        for (String s : input.split(",")) {
            String[] pair = s.trim().split("-");
            long a = Long.parseLong(pair[0]);
            long b = Long.parseLong(pair[1]);

            // split the range by digit count
            int n1 = digitCount(a);
            int n2 = digitCount(b);
            if (n1 == n2) {
                ranges.add(new IdRange(a, b, n1));
            } else {
                ranges.add(new IdRange(a, pow10[n1] - 1, n1));
                for (int n = n1 + 1; n < n2; n++)
                    ranges.add(new IdRange(pow10[n - 1], pow10[n] - 1, n));
                ranges.add(new IdRange(pow10[n2 - 1], b, n2));
            }
        }
    }

    @Override
    public Long part1() {
        return ranges.stream().mapToLong(r -> sumInvalid1(r.a, r.b, r.n)).sum();
    }

    @Override
    public Object part2() {
        return ranges.stream().mapToLong(r -> sumInvalid2(r.a, r.b, r.n)).sum();
    }

    private long sumInvalid1(long a, long b, int n) {
        if (n % 2 != 0) return 0;
        long m = (pow10[n] - 1) / (pow10[n / 2] - 1);
        return sumMultiples(a, b, m);
    }

    private long sumInvalid2(long a, long b, int n) {
        long total = 0;
        for (int k = 1; k <= n / 2; k++) {
            if (n % k != 0) continue;
            long m = (pow10[n] - 1) / (pow10[k] - 1);
            total -= mu[n / k] * sumMultiples(a, b, m);
        }
        return total;
    }

    private long sumMultiples(long a, long b, long m) {
        long i = Math.floorDiv(a - 1, m);
        long j = Math.floorDiv(b, m);
        return m * (j*(j+1)/2 - i*(i+1)/2);
    }

    private static int digitCount(long x) {
        int i = Arrays.binarySearch(pow10, x);
        return i >= 0 ? i + 1 : -i - 1;
    }

    @AllArgsConstructor
    public static class IdRange {
        public final long a;
        public final long b;
        public final int n;
    }
}
