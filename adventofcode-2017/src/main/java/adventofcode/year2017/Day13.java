package adventofcode.year2017;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Function;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.collect.DefaultHashMap;
import lombok.AllArgsConstructor;

@Puzzle(day = 13, name = "Packet Scanners")
public class Day13 extends AbstractDay {
    private DefaultHashMap<Integer, Set<Integer>> scanners = DefaultHashMap.ofSupplier(HashSet::new);

    @Override
    public void parse(String input) {
        for (String line : input.split("\n")) {
            int depth = Integer.parseInt(Fn.before(line, ":").trim());
            int range = Integer.parseInt(Fn.after(line, ":").trim());
            scanners.computeIfAbsent(range).add(depth);
        }
    }

    @Override
    public Integer part1() {
        int totalSeverity = 0;
        for (int range : scanners.keySet()) {
            int period = (range - 1) * 2;
            for (int depth : scanners.get(range)) {
                if (depth % period == 0)
                    totalSeverity += depth * range;
            }
        }
        return totalSeverity;
    }

    @Override
    public Integer part2() {
        PriorityQueue<Solution> heap = new PriorityQueue<>(Comparator.comparingInt(s -> s.delays.size()));
        for (int range : scanners.keySet())
            heap.add(Solution.forRange(range, scanners.get(range)));
        while (heap.size() > 1)
            heap.add(heap.poll().and(heap.poll()));
        Solution solution = heap.poll();
        return Fn.min(solution.delays, Function.identity());
    }

    @AllArgsConstructor
    public static class Solution {
        public final Set<Integer> delays;
        public final int period;

        public static Solution forRange(int range, Set<Integer> depths) {
            int period = (range - 1) * 2;
            HashSet<Integer> delays = new HashSet<>();
            for (int delay = 0; delay < period; delay++)
                delays.add(delay);
            // Constraint: delay + depth != 0 mod period
            for (int depth : depths)
                delays.remove(Math.floorMod(-depth, period));
            return new Solution(delays, period);
        }

        public Solution and(Solution other) {
            var sol = IntMath.gcdExtended(period, other.period);
            Set<Integer> newStarts = new HashSet<>();
            int newPeriod = (period / sol.gcd) * other.period;
            for (int a1 : delays) {
                for (int a2 : other.delays) {
                    if ((a2 - a1) % sol.gcd == 0) {
                        int A = (a2 - a1) / sol.gcd;
                        int k1 = Math.floorMod(A * sol.x, other.period);
                        newStarts.add(Math.floorMod(a1 + k1*period, newPeriod));
                    }
                }
            }
            return new Solution(newStarts, newPeriod);
        }
    }
}
