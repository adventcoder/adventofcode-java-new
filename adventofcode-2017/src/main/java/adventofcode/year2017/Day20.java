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
import adventofcode.utils.IntMath;
import adventofcode.utils.collect.IntArray;
import adventofcode.utils.collect.Range;
import adventofcode.utils.geom.Vec3;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Puzzle(day = 20, name = "Particle Swarm")
public class Day20 extends AbstractDay {
    private List<Path> paths;

    @Override
    public void parse(String input) {
        paths = new ArrayList<>();
        for (String line : input.split("\n")) {
            Vec3 p = parseVec3(line, "p");
            Vec3 v = parseVec3(line, "v");
            Vec3 a = parseVec3(line, "a");
            paths.add(Path.ofParticle(p, v, a));
        }
    }

    private Vec3 parseVec3(String line, String name) {
        String s = Fn.between(line, name + "=<", ">");
        String tokens[] = s.split(",");
        int x = Integer.parseInt(tokens[0].trim());
        int y = Integer.parseInt(tokens[1].trim());
        int z = Integer.parseInt(tokens[2].trim());
        return new Vec3(x, y, z);
    }

    @Override
    public Integer part1() {
        return IntStream.range(0, paths.size()).boxed()
            .min(Comparator.comparing(i -> paths.get(i))).orElse(null);
    }

    @Override
    public Integer part2() {
        Map<Integer, List<IntArray>> collisions = new HashMap<>();
        for (int i = 0; i < paths.size(); i++) {
            for (int j = i + 1; j < paths.size(); j++) {
                Path relativePath = paths.get(i).subtract(paths.get(j));
                for (int t : relativePath.findCandidateRoots()) {
                    if (t >= 0 && relativePath.isRoot(t)) {
                        collisions.computeIfAbsent(t, k -> new ArrayList<>())
                            .add(IntArray.of(i, j));
                    }
                }
            }
        }

        Set<Integer> alive = new HashSet<>(new Range(paths.size()));
        for (int t : Fn.sorted(collisions.keySet())) {
            Set<Integer> toRemove = new HashSet<>();
            for (IntArray coll : collisions.get(t)) {
                int i = coll.get(0), j = coll.get(1);
                if (alive.contains(i) && alive.contains(j)) {
                    toRemove.add(i);
                    toRemove.add(j);
                }
            }
            alive.removeAll(toRemove);
        }

        return alive.size();
    }

    @AllArgsConstructor
    @ToString
    public static class Path implements Comparable<Path> {
        private final Vec3 A;
        private final Vec3 B;
        private final Vec3 C;

        public static Path ofParticle(Vec3 initialPos, Vec3 initialVel, Vec3 acc) {
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

        public IntArray findCandidateRoots() {
            if (!A.isZero()) {
                return IntMath.solveQuadratic(A.dot(A), B.dot(A), C.dot(A));
            } else if (!B.isZero()) {
                return IntMath.solveLinear(B.dot(B), C.dot(B));
            } else {
                return IntMath.solveConstant(C.dot(C));
            }
        }

        public boolean isRoot(int t) {
            return A.multiply(t*t).add(B.multiply(t)).add(C).isZero();
        }
    }
}
