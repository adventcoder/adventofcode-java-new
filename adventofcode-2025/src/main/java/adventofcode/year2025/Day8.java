package adventofcode.year2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.IntMath;
import adventofcode.utils.collect.DisjointSet;
import adventofcode.utils.geom.Vector3;
import it.unimi.dsi.fastutil.ints.IntIntPair;

@Puzzle(day = 8, name = "Playground")
public class Day8 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day8.class, args);
    }

    private List<Vector3> junctions;
    private PriorityQueue<IntIntPair> connections;
    private DisjointSet circuits;

    @Override
    public void parse(String input) {
        junctions = new ArrayList<>();
        for (String line : input.split("\n"))
            junctions.add(Fn.parseIntTriple(line, ",", Vector3::new));
    }

    @Override
    public void common() {
        connections = new PriorityQueue<>(Comparator.comparingLong(this::distance));
        for (int i = 0; i < junctions.size(); i++)
            for (int j = i + 1; j < junctions.size(); j++)
                connections.add(IntIntPair.of(i, j));
        circuits = new DisjointSet(junctions.size());
    }

    @Override
    public Integer part1() {
        for (int i = 0; i < 1000; i++) {
            IntIntPair conn = connections.poll();
            circuits.merge(conn.leftInt(), conn.rightInt());
        }
        return circuits.ranks()
            .sorted()
            .skip(circuits.size() - 3)
            .reduce(1, IntMath::product);
    }

    @Override
    public Integer part2() {
        while (!connections.isEmpty()) {
            IntIntPair conn = connections.poll();
            circuits.merge(conn.leftInt(), conn.rightInt());
            if (circuits.size() == 1)
                return junctions.get(conn.leftInt()).x() * junctions.get(conn.rightInt()).x();
        }
        return null;
    }

    private long distance(IntIntPair conn) {
        return junctions.get(conn.rightInt()).subtract(junctions.get(conn.leftInt())).magnitudeSquared();
    }
}
