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
import adventofcode.utils.geom.Vec3;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Puzzle(day = 20, name = "Particle Swarm")
public class Day20 extends AbstractDay {
    private List<Particle> particles;

    @Override
    public void parse(String input) {
        particles = new ArrayList<>();
        for (String line : input.split("\n"))
            particles.add(Particle.parse(line));
    }

    @Override
    public Integer part1() {
        return IntStream.range(0, particles.size()).boxed()
            .min(Comparator.comparing(i -> particles.get(i))).orElse(null);
    }

    @Override
    public Integer part2() {
        Map<Integer, List<int[]>> collisions = new HashMap<>();
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                for (int t : particles.get(i).collide(particles.get(j))) {
                    if (t >= 0) {
                        collisions.computeIfAbsent(t, k -> new ArrayList<>())
                            .add(new int[] { i, j });
                    }
                }
            }
        }

        Set<Integer> alive = new HashSet<>();
        for (int i = 0; i < particles.size(); i++)
            alive.add(i);

        for (int t : Fn.sorted(collisions.keySet())) {
            Set<Integer> toRemove = new HashSet<>();
            for (int[] coll : collisions.get(t)) {
                int i = coll[0];
                int j = coll[1];
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
    public static class Particle implements Comparable<Particle> {
        public Vec3 pos;
        public Vec3 vel;
        public Vec3 acc;

        public static Particle parse(String s) {
            Map<String, Vec3> vals = new HashMap<>();
            for (String subs : s.split(", ")) {
                String[] pair = subs.split("=");
                vals.put(pair[0].trim(), parseVec(pair[1].trim()));
            }
            return new Particle(vals.get("p"), vals.get("v"), vals.get("a"));
        }

        private static Vec3 parseVec(String s) {
            s = s.substring(1, s.length() - 1);
            String[] tokens = s.split(",");
            return new Vec3(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), Integer.parseInt(tokens[2]));
        }

        @Override
        public int compareTo(Particle other) {
            // compare by closeness to the origin as t tends to infinity
            //
            // The position at time t is P(t) = pos + (vel + acc/2) t + acc/2 t^2
            // 
            // The t^2 component will dominate in the long term, but in the case where there are multiple particles with the same acc then the t component needs to be considered and so forth.
            //
            int cmp = Integer.compare(acc.abs(), other.acc.abs());
            if (cmp == 0) {
                cmp = Integer.compare(vel.multiply(2).add(acc).abs(), other.vel.multiply(2).add(other.acc).abs());
                if (cmp == 0)
                    cmp = Integer.compare(pos.abs(), other.pos.abs());
            }
            return cmp;
        }

        public Set<Integer> collide(Particle other) {
            // Solve P1(t) - P2(t) = 0
            //
            // Where P(t) = acc/2 t^2 + (vel + acc/2) t + pos
            //
            return solveQuadratic(
                acc.subtract(other.acc),
                vel.subtract(other.vel).multiply(2).add(acc.subtract(other.acc)),
                pos.subtract(other.pos).multiply(2)
            );
        }

        private static Set<Integer> solveQuadratic(Vec3 A, Vec3 B, Vec3 C) {
            // A t^2 + B t + C = 0
            if (A.abs() == 0) {
                return solveLinear(B, C);
            } else {
                return solveLinear(A.cross(B), A.cross(C));
            }
        }

        private static Set<Integer> solveLinear(Vec3 A, Vec3 B) {
            // A t + B = 0
            if (A.abs() == 0) {
                if (B.abs() == 0)
                    return null;
            } else {
                if (A.cross(B).abs() == 0) {
                    long n = -A.dot(B);
                    long d = A.dot(A);
                    if (n % d == 0)
                        return Set.of((int) (n / d));
                }
            }
            return Set.of();
        }
    }
}
