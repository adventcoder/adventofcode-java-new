package adventofcode.year2017;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Vec2;

@Puzzle(day = 22, name = "Sporifica Virus")
public class Day22 extends AbstractDay {
    private static Vec2[] dirs = { Vec2.NORTH, Vec2.EAST, Vec2.SOUTH, Vec2.WEST };

    private Set<Vec2> initialInfected;
    private Vec2 start;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");

        initialInfected = new HashSet<>();
        for (int y = 0; y < lines.length; y++)
            for (int x = 0; x < lines[y].length(); x++)
                if (lines[y].charAt(x) == '#')
                    initialInfected.add(new Vec2(x, y));

        start = new Vec2((lines[0].length() - 1) / 2, (lines.length - 1) / 2);
    }

    @Override
    public Integer part1() {
        Set<Vec2> infected = new HashSet<>(initialInfected);
        int infections = 0;

        Vec2 pos = start;
        Vec2 dir = Vec2.NORTH;
        for (int i = 0; i < 10000; i++) {
            if (infected.contains(pos)) {
                infected.remove(pos);
                dir = dir.perpRight();
            } else {
                infected.add(pos);
                dir = dir.perpLeft();
                infections++;
            }
            pos = pos.add(dir);
        }

        return infections;
    }

    @Override
    public Integer part2() {
        Map<Vec2, State> states = new HashMap<>();
        for (Vec2 pos : initialInfected)
            states.put(pos, State.INFECTED);
        int infections = 0;

        Vec2 pos = start;
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
