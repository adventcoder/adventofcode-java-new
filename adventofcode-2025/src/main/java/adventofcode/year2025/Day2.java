package adventofcode.year2025;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.DefaultHashMap;
import it.unimi.dsi.fastutil.Pair;

@Puzzle(day = 2, name = "Gift Shop")
public class Day2 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day2.class, args);
    }

    private Map<Integer, List<Pair<BigInteger, BigInteger>>> ranges;
    private BigInteger[] pow10;
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
        for (String[] pair : pairs) {
            int aCount = pair[0].length(), bCount = pair[1].length();
            BigInteger a = new BigInteger(pair[0]), b = new BigInteger(pair[1]);
            if (aCount == bCount) {
                ranges.get(aCount).add(Pair.of(a, b));
            } else {
                ranges.get(aCount).add(Pair.of(a, pow10[aCount].subtract(BigInteger.ONE)));
                for (int n = aCount + 1; n < bCount; n++)
                    ranges.get(n).add(Pair.of(pow10[n - 1], pow10[n].subtract(BigInteger.ONE))); 
                ranges.get(bCount).add(Pair.of(pow10[bCount - 1], b));
            }
        }
    }

    private void precomputePowers(int max) {
        pow10 = new BigInteger[max + 1];
        pow10[0] = BigInteger.ONE;
        for (int n = 1; n <= max; n++)
            pow10[n] = pow10[n - 1].multiply(BigInteger.TEN);
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
    public BigInteger part1() {
        BigInteger total = BigInteger.ZERO;
        for (int n : ranges.keySet()) {
            if (n % 2 != 0) continue;
            total = total.add(sumInvalid(n, n / 2));
        }
        return total;
    }

    @Override
    public BigInteger part2() {
        BigInteger total = BigInteger.ZERO;
        for (int n : ranges.keySet())
            total = total.add(sumInvalid(n, n).subtract(sumInvalidAll(n))); // subtract out the groups that are just the entire number
        return total;
    }

    private BigInteger sumInvalidAll(int n) {
        BigInteger total = BigInteger.ZERO;
        for (int d = 1; d <= n; d++) {
            if (n % d != 0) continue; // could precomputate spf for faster divisors?
            BigInteger sign = BigInteger.valueOf(mu[n / d]); // mobius inversion handles inclusion/exclusion
            if (sign.equals(BigInteger.ZERO)) continue;
            total = total.add(sumInvalid(n, d).multiply(sign));
        }
        return total;
    }

    private BigInteger sumInvalid(int n, int d) {
        BigInteger total = BigInteger.ZERO;
        BigInteger m = pow10[n].subtract(BigInteger.ONE).divide(pow10[d].subtract(BigInteger.ONE)); // (10^n-1) / (10^d-1)
        for (var range : ranges.get(n))
            total = total.add(sumMultiples(range.left(), range.right(), m));
        return total;
    }

    private BigInteger sumMultiples(BigInteger a, BigInteger b, BigInteger m) {
        BigInteger i = a.subtract(BigInteger.ONE).divide(m); // (a-1)/m
        BigInteger j = b.divide(m); // b/m
        BigInteger iSum = i.multiply(i.add(BigInteger.ONE)).divide(BigInteger.TWO); // i(i+1)/2
        BigInteger jSum = j.multiply(j.add(BigInteger.ONE)).divide(BigInteger.TWO); // j(j+1)/2
        return m.multiply(jSum.subtract(iSum));
    }
}
