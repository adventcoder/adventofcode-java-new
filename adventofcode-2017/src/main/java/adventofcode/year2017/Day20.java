package adventofcode.year2017;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.ToIntFunction;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.collect.Range;
import adventofcode.utils.geom.Vector3;
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

    private static final List<ToIntFunction<Vector3>> axes = List.of(Vector3::x, Vector3::y, Vector3::z);

    private List<Particle> particles;

    @Override
    public void parse(String input) {
        particles = new ArrayList<>();
        for (String line : input.split("\n"))
            particles.add(Particle.parse(line));
    }

    @Override
    public Integer part1() {
        return Fn.argMin(new Range(particles.size()), i -> particles.get(i).distanceToOriginAtInfinity());
    }

    @Override
    public Integer part2() {
        Map<Integer, List<IntIntPair>> collisions = new HashMap<>();
        for (int i = 0; i < particles.size(); i++) {
            for (int j = i + 1; j < particles.size(); j++) {
                for (int t : particles.get(i).collide(particles.get(j))) {
                    if (t >= 0)
                        collisions.computeIfAbsent(t, k -> new ArrayList<>()).add(IntIntPair.of(i, j));
                }
            }
        }

        IntSet alive = new IntOpenHashSet(new Range(particles.size()));
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
    private static class Particle {
        public Vector3 pos;
        public Vector3 vel;
        public Vector3 acc;

        public static Particle parse(String line) {
            Map<String, Vector3> vals = new HashMap<>();
            for (String s : line.split(", ")) {
                String[] parts = s.split("=");
                vals.put(parts[0].trim(), parseVector(parts[1]));
            }
            return new Particle(vals.get("p"), vals.get("v"), vals.get("a"));
        }

        private static Vector3 parseVector(String s) {
            String[] vals = Fn.strip(s.trim(), "<", ">").split(",");
            return new Vector3(Integer.parseInt(vals[0]), Integer.parseInt(vals[1]), Integer.parseInt(vals[2]));
        }

        public IntList distanceToOriginAtInfinity() {
            return IntList.of(acc.abs(), acc.addMul(vel, 2).abs(), pos.multiply(2).abs());
        }

        public IntList collide(Particle other) {
            Vector3 relPos = pos.subtract(other.pos);
            Vector3 relVel = vel.subtract(other.vel);
            Vector3 relAcc = acc.subtract(other.acc);
            return solveQuadratic(relAcc, relAcc.addMul(relVel, 2), relPos.multiply(2));
        }
   }

    private static IntList solveQuadratic(Vector3 a, Vector3 b, Vector3 c) {
        IntList roots = null;
        for (var axis : axes) {
            IntList axisRoots = solveQuadratic(axis.applyAsInt(a), axis.applyAsInt(b), axis.applyAsInt(c));
            if (axisRoots == null) continue;
            if (roots == null) {
                roots = axisRoots;
            } else {
                roots.retainAll(axisRoots);
            }
            if (roots.isEmpty()) break;
        }
        return roots;
    }

    private static IntList solveQuadratic(int a, int b, int c) {
        if (a == 0)
            return solveLinear(b, c);
        IntList roots = new IntArrayList();
        int disc = b*b - 4*a*c;
        int denom = 2*a;
        if (disc == 0) {
            if (b % denom == 0)
                roots.add(-b / denom);
        } else if (disc > 0) {
            int k = (int) Math.sqrt(disc);
            if (k*k == disc) {
                if ((b + k) % denom == 0)
                    roots.add((-b - k) / denom);
                if ((b - k) % denom == 0)
                    roots.add((-b + k) / denom);
            }
        }
        return roots;
    }

    private static IntList solveLinear(int a, int b) {
        if (a == 0)
            return solveConstant(b);
        IntList roots = new IntArrayList();
        if (b % a == 0)
            roots.add(-b / a);
        return roots;
    }

    private static IntList solveConstant(int a) {
        if (a == 0) return null;
        return new IntArrayList();
    }
}
