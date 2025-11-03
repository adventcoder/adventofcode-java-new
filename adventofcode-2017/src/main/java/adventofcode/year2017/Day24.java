package adventofcode.year2017;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import lombok.AllArgsConstructor;

//TODO: slow brute force dfs, ideas: find biconnected components
@Puzzle(day = 24, name = "Electromagnetic Moat")
public class Day24 extends AbstractDay {
    private List<Piece> pieces;
    private Map<Integer, Set<Integer>> cands;

    @Override
    public void parse(String input) {
        pieces = Fn.parseVals(input, "\n", Piece::parse).toList();
        cands = new HashMap<>();
        for (int i = 0; i < pieces.size(); i++) {
            cands.computeIfAbsent(pieces.get(i).portA, k -> new HashSet<>()).add(i);
            cands.computeIfAbsent(pieces.get(i).portB, k -> new HashSet<>()).add(i);
        }
    }

    @Override
    public Integer part1() {
        Comparator<Bridge> cmp = Comparator.comparingInt(bridge -> bridge.strength);
        return findBestBridge(0, 0L, cmp).strength;
    }

    @Override
    public Integer part2() {
        Comparator<Bridge> cmp = Comparator.comparingInt((Bridge bridge) -> bridge.length)
                                           .thenComparingInt(bridge -> bridge.strength);
        return findBestBridge(0, 0L, cmp).strength;
    }

    public Bridge findBestBridge(int a, long used, Comparator<Bridge> cmp) {
        Bridge best = new Bridge(0, 0);
        for (int i : cands.get(a)) {
            if ((used & (1L << i)) == 0) {
                Piece piece = pieces.get(i);
                int b = piece.portA + piece.portB - a;
                Bridge bridge = piece.cons(findBestBridge(b, used | (1L << i), cmp));
                if (cmp.compare(bridge, best) > 0)
                    best = bridge;
            }
        }
        return best;
    }

    @AllArgsConstructor
    public static class Piece {
        public final int portA;
        public final int portB;

        public static Piece parse(String s) {
            String[] pair = s.split("/");
            int a = Integer.parseInt(pair[0].trim());
            int b = Integer.parseInt(pair[1].trim());
            return new Piece(a, b);
        }

        public Bridge cons(Bridge rest) {
            return new Bridge(rest.strength + portA + portB, rest.length + 1);
        }
    }

    @AllArgsConstructor
    public static class Bridge {
        public final int strength;
        public final int length;
    }
}
