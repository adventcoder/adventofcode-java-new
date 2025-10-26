package adventofcode.year2017;

import java.util.List;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import lombok.AllArgsConstructor;

@Puzzle(day = 11, name = "Hex Ed")
public class Day11 extends AbstractDay {
    private static Map<String, HexVec> dirMap = Map.of(
        "nw", new HexVec(1, 0),
        "n", new HexVec(1, 1),
        "ne", new HexVec(0, 1),
        "sw", new HexVec(0, -1),
        "s", new HexVec(-1, -1),
        "se", new HexVec(-1, 0)
    );

    private List<HexVec> dirs;
    private int maxDistance = 0;

    @Override
    public void parse(String input) {
        dirs = Fn.parseVals(input.replaceAll("\\s+", ""), ",", dirMap::get).toList();
    }

    @Override
    public Integer part1() {
        HexVec pos = new HexVec(0, 0);
        for (HexVec dir : dirs) {
            pos = pos.add(dir);
            maxDistance = Math.max(maxDistance, pos.abs());
        }
        return pos.abs();
    }

    @Override
    public Integer part2() {
        return maxDistance;
    }

    @AllArgsConstructor
    public static class HexVec {
        public final int nw;
        public final int ne;

        public HexVec add(HexVec other) {
            return new HexVec(nw + other.nw, ne + other.ne);
        }

        public int abs() {
            return Fn.max(Math.abs(2*nw), Math.abs(2*ne), Math.abs(nw + ne)) / 2;
        }
    }
}
