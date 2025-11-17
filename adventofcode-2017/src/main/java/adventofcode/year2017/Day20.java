package adventofcode.year2017;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.collect.DefaultHashMap;
import adventofcode.utils.collect.DefaultMap;
import adventofcode.utils.collect.IntArrays;
import adventofcode.utils.collect.Range;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntIntPair;
import it.unimi.dsi.fastutil.ints.IntList;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;
import lombok.AllArgsConstructor;

@Puzzle(day = 20, name = "Particle Swarm")
public class Day20 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day20.class, args);
    }
    private List<Path> paths;

    @Override
    public void parse(String input) {
        paths = new ArrayList<>();
        for (String line : input.split("\n"))
            paths.add(Path.parse(line));
    }

    @Override
    public Integer part1() {
        return IntStream.range(0, paths.size()).boxed()
            .min(Comparator.comparing(i -> paths.get(i)))
            .orElse(null);
    }

    @Override
    public Integer part2() {
        DefaultMap<Integer, List<IntIntPair>> collisions = new DefaultHashMap<>(k -> new ArrayList<>());
        for (int i = 0; i < paths.size(); i++) {
            for (int j = i + 1; j < paths.size(); j++) {
                Path relPath = paths.get(i).subtract(paths.get(j));
                for (int t : relPath.findPotentialRoots()) {
                    if (t >= 0 && relPath.isRoot(t)) {
                        collisions.getOrCompute(t).add(IntIntPair.of(i, j));
                    }
                }
            }
        }

        IntSet alive = new IntOpenHashSet(new Range(paths.size()));
        for (Integer t : Fn.sorted(collisions.keySet())) {
            IntList toRemove = new IntArrayList();
            for (IntIntPair coll : collisions.get(t)) {
                if (alive.contains(coll.leftInt()) && alive.contains(coll.rightInt())) {
                    toRemove.add(coll.leftInt());
                    toRemove.add(coll.rightInt());
                }
            }
            alive.removeAll(toRemove);
        }

        return alive.size();
    }

    @AllArgsConstructor
    private static class Path implements Comparable<Path> {
        public Vector pos;
        public Vector vel;
        public Vector acc;

        public static Path parse(String line) {
            Map<String, Vector> vals = new HashMap<>();
            for (String s : line.split(", ")) {
                String[] parts = s.split("=");
                vals.put(parts[0].trim(), Vector.parse(parts[1]));
            }
            return newton(vals.get("p"), vals.get("v"), vals.get("a"));
        }

        public static Path newton(Vector pos, Vector vel, Vector acc) {
            // P(t) = P + V t + A t(t+1)/2
            return new Path(pos.mul(2), acc.addMul(vel, 2), acc);
        }

        @Override
        public int compareTo(Path other) {
            int cmp = Integer.compare(acc.abs(), other.acc.abs());
            if (cmp == 0) {
                cmp = Integer.compare(vel.abs(), other.vel.abs());
                if (cmp == 0)
                    cmp = Integer.compare(pos.abs(), other.pos.abs());
            }
            return cmp;
        }

        public Path subtract(Path other) {
            return new Path(pos.subtract(other.pos), vel.subtract(other.vel), acc.subtract(other.acc));
        }

        public boolean isRoot(int t) {
            return pos.addMul(vel, t).addMul(acc, t*t).abs() == 0;
        }

        public int[] findPotentialRoots() {
            // it's much faster to solve the projection and filter out invalid roots afterwards
            if (acc.abs() != 0) {
                return solveQuadratic(acc.dot(acc), vel.dot(acc), pos.dot(acc));
            } else if (vel.abs() != 0) {
                return solveLinear(vel.dot(vel), pos.dot(vel));
            } else {
                return solveConstant(pos.dot(pos));
            }
        }

        private static int[] solveQuadratic(int a, int b, int c) {
            int disc = b*b - 4*a*c;
            if (disc == 0) {
                return solveLinear(2*a, b);
            } else if (disc > 0) {
                int k = IntMath.floorSqrt(disc);
                if (k * k == disc)
                    return IntArrays.concat(solveLinear(2*a, b + k), solveLinear(2*a, b - k));
            }
            return new int[0];
        }

        private static int[] solveLinear(int a, int b) {
            if (-b % a == 0)
                return new int[] { -b / a };
            return new int[0];
        }

        private static int[] solveConstant(int a) {
            if (a == 0)
                throw new IllegalArgumentException("zero polynomial");
            return new int[0];
        }
    }

    //TODO: move into utils math package?
    @AllArgsConstructor
    private static class Vector {
        public final int x;
        public final int y;
        public final int z;

        public static Vector parse(String s) {
            String[] tokens = Fn.strip(s.trim(), "<", ">").split(",");
            int x = Integer.parseInt(tokens[0]);
            int y = Integer.parseInt(tokens[1]);
            int z = Integer.parseInt(tokens[2]);
            return new Vector(x, y, z);
        }

        public int abs() {
            return Math.abs(x) + Math.abs(y) + Math.abs(z);
        } 

        public Vector subtract(Vector o) {
            return new Vector(x - o.x, y - o.y, z - o.z);
        }

        public Vector addMul(Vector v, int n) {
            return new Vector(x + v.x*n, y + v.y*n, z + v.z*n);
        }

        public Vector mul(int n) {
            return new Vector(x * n, y * n, z * n);
        }

        public int dot(Vector v) {
            return x*v.x + y*v.y + z*v.z;
        }
    }
}
