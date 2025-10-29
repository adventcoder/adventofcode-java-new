package adventofcode.year2017;

import java.util.ArrayList;
import java.util.Collections;
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
    public static void main(String[] args) throws Exception {
        main(Day20.class, args);
    }

    private List<Particle> particles;

    @Override
    public void parse(String input) {
        particles = new ArrayList<>();
        for (String line : input.split("\n"))
            particles.add(Particle.parse(line));
    }

    @Override
    public Integer part1() {
        return IntStream.range(0, particles.size())
            .boxed()
            .min(Comparator.comparing(i -> particles.get(i)))
            .orElse(null);
    }

    @Override
    public Integer part2() {
        //TODO: the time steps are tiny, in hindsight I should have just ran the simulation
        Map<Integer, List<Collision>> collisions = new HashMap<>();
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                for (int t : particles.get(i).collide(particles.get(j))) {
                    if (t >= 0) {
                        collisions.computeIfAbsent(t, k -> new ArrayList<>())
                            .add(new Collision(i, j));
                    }
                }
            }
        }

        Set<Integer> alive = new HashSet<>();
        for (int i = 0; i < particles.size(); i++)
            alive.add(i);

        for (int t : Fn.sorted(collisions.keySet())) {
            Set<Integer> toRemove = new HashSet<>();
            for (Collision coll : collisions.get(t)) {
                if (alive.contains(coll.i) && alive.contains(coll.j)) {
                    toRemove.add(coll.i);
                    toRemove.add(coll.j);
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

        public List<Integer> collide(Particle other) {
            // Solve for P1(t) - P2(t) = dA + dB t + dC t^2 = 0
            Vec3 dA = pos.subtract(other.pos).multiply(2);
            Vec3 dC = acc.subtract(other.acc);
            Vec3 dB = vel.subtract(other.vel).multiply(2).add(dC);
            return solveQuadratic(dA, dB, dC);
        }

        private static List<Integer> solveQuadratic(Vec3 A, Vec3 B, Vec3 C) {
            // Solve A + B t + C t^2 = 0
            if (C.abs() == 0)
                return solveLinear(A, B);
            //
            // Crossing by C cancels the quadratic component giving:
            //
            // U + V t = 0, U=AxC, V=BxC
            //
            // Normally there is only one or no solution since the system is overspecified.
            //
            // Note however that if both U=0 and V=0 then A,B,C are all colinear and there could be two solutions, but we don't have to handle that case here.
            //
            return solveLinear(A.cross(C), B.cross(C));
        }

        private static List<Integer> solveLinear(Vec3 A, Vec3 B) {
            // Solve A + B t = 0
            //
            // This has a solution when A and B are colinear, A x B = 0
            //
            // The solution is:
            //
            // A.B + B.B t = 0
            //           t = -A.B / B.B
            //
            if (A.cross(B).abs() == 0) {
                // careful these dot products were overflowing!
                long n = -A.dot(B);
                long d = B.dot(B);
                // we have discrete integer time steps only
                if (n % d == 0)
                    return List.of((int) (n / d));
            }
            return Collections.emptyList();
        }
    }

    @AllArgsConstructor
    public static class Collision {
        public final int i;
        public final int j;
    }
}
