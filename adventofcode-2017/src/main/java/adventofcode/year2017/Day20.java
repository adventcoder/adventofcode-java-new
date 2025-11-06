package adventofcode.year2017;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.Range;
import adventofcode.utils.math.IntMath;
import adventofcode.utils.math.IntVec3;
import lombok.AllArgsConstructor;

@Puzzle(day = 20, name = "Particle Swarm")
public class Day20 extends AbstractDay {
    private List<Path> paths;

    @Override
    public void parse(String input) {
        paths = new ArrayList<>();
        for (String line : input.split("\n")) {
            IntVec3 p = IntVec3.parse(Fn.between(line, "p=<", ">"), ",");
            IntVec3 v = IntVec3.parse(Fn.between(line, "v=<", ">"), ",");
            IntVec3 a = IntVec3.parse(Fn.between(line, "a=<", ">"), ",");
            paths.add(Path.ofParticle(p, v, a));
        }
    }

    @Override
    public Integer part1() {
        return IntStream.range(0, paths.size()).boxed()
            .min(Comparator.comparing(i -> paths.get(i))).orElse(null);
    }

    @Override
    public Integer part2() {
        Map<Integer, List<List<Integer>>> collisions = new HashMap<>();
        for (int i = 0; i < paths.size(); i++) {
            for (int j = i + 1; j < paths.size(); j++) {
                Path relativePath = paths.get(i).subtract(paths.get(j));
                for (int t : relativePath.findPotentialRoots()) {
                    if (t >= 0 && relativePath.isRoot(t)) {
                        collisions.computeIfAbsent(t, k -> new ArrayList<>())
                            .add(List.of(i, j));
                    }
                }
            }
        }

        Set<Integer> alive = new HashSet<>(new Range(paths.size()));
        for (int t : Fn.sorted(collisions.keySet())) {
            Set<Integer> toRemove = new HashSet<>();
            for (List<Integer> coll : collisions.get(t)) {
                if (alive.containsAll(coll))
                    toRemove.addAll(coll);
            }
            alive.removeAll(toRemove);
        }

        return alive.size();
    }

    @AllArgsConstructor
    public static class Path implements Comparable<Path> {
        private final IntVec3 A;
        private final IntVec3 B;
        private final IntVec3 C;

        public static Path ofParticle(IntVec3 initialPos, IntVec3 initialVel, IntVec3 acc) {
            // V(t) = V(t-1) + A
            //      = ...
            //      = V0 + A t
            //
            // P(t) = P(T-1) + V(t)
            //      = P(T-1) + V0 + A t
            //      = ...
            //      = P0 + V0 t + A t(t+1)/2
            //      = P0 + (V0+A/2) t + A/2 t^2
            //
            return new Path(acc, initialVel.multiply(2).add(acc), initialPos.multiply(2));
        }

        @Override
        public int compareTo(Path other) {
            // Compare by closeness to the origin as t tends to infinity.
            //
            // The t^2 term will dominate in the long term.
            // But in the case where there are multiple paths with the same t^2 term, then the t term needs to be considered and so forth.
            //
            int cmp = Integer.compare(A.abs(), other.A.abs());
            if (cmp == 0) {
                cmp = Integer.compare(B.abs(), other.B.abs());
                if (cmp == 0)
                    cmp = Integer.compare(C.abs(), other.C.abs());
            }
            return cmp;
        }

        public Path subtract(Path other) {
            return new Path(A.subtract(other.A), B.subtract(other.B), C.subtract(other.C));
        }

        public int[] findPotentialRoots() {
            if (!A.isZero()) {
                return IntMath.findRoots(A.dot(A), B.dot(A), C.dot(A));
            } else if (!B.isZero()) {
                return IntMath.findRoots(B.dot(B), C.dot(B));
            } else {
                return IntMath.findRoots(C.dot(C));
            }
        }

        public boolean isRoot(int t) {
            return A.multiply(t*t).add(B.multiply(t)).add(C).isZero();
        }
    }
}
