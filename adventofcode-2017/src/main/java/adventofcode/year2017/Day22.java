package adventofcode.year2017;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.math.IntVec2;

@Puzzle(day = 22, name = "Sporifica Virus")
public class Day22 extends AbstractDay {
    private static IntVec2[] dirs = { IntVec2.NORTH, IntVec2.EAST, IntVec2.SOUTH, IntVec2.WEST };

    private Set<IntVec2> initialInfected;
    private IntVec2 start;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");

        initialInfected = new HashSet<>();
        for (int y = 0; y < lines.length; y++)
            for (int x = 0; x < lines[y].length(); x++)
                if (lines[y].charAt(x) == '#')
                    initialInfected.add(new IntVec2(x, y));

        start = new IntVec2((lines[0].length() - 1) / 2, (lines.length - 1) / 2);
    }

    @Override
    public Integer part1() {
        Set<IntVec2> infected = new HashSet<>(initialInfected);
        int infections = 0;

        IntVec2 pos = start;
        IntVec2 dir = IntVec2.NORTH;
        for (int i = 0; i < 10000; i++) {
            if (infected.contains(pos)) {
                infected.remove(pos);
                dir = dir.rotateRight();
            } else {
                infected.add(pos);
                dir = dir.rotateLeft();
                infections++;
            }
            pos = pos.add(dir);
        }

        return infections;
    }

    @Override
    public Integer part2() {
        Map<IntVec2, State> states = new HashMap<>();
        for (IntVec2 pos : initialInfected)
            states.put(pos, State.INFECTED);
        int infections = 0;

        IntVec2 pos = start;
        int dirIndex = 0;
        for (int i = 0; i < 10000000; i++) {
            State state = states.getOrDefault(pos, State.CLEAN);
            State newState = State.values()[(state.ordinal() + 1) % 4];

            states.put(pos, newState);
            if (newState == State.INFECTED)
                infections++;

            dirIndex = (dirIndex + state.ordinal() + 3) % 4;
            pos = pos.add(dirs[dirIndex]);
        }

        return infections;
    }

    private static enum State { CLEAN, WEAKENED, INFECTED, FLAGGED }
}
