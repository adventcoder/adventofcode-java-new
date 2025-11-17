package adventofcode.year2017;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Dir4;
import adventofcode.utils.geom.Point;

@Puzzle(day = 22, name = "Sporifica Virus")
public class Day22 extends AbstractDay {
    private Set<Point> initialInfected;
    private Point start;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\n");

        initialInfected = new HashSet<>();
        for (int y = 0; y < lines.length; y++)
            for (int x = 0; x < lines[y].length(); x++)
                if (lines[y].charAt(x) == '#')
                    initialInfected.add(new Point(x, y));

        start = new Point((lines[0].length() - 1) / 2, (lines.length - 1) / 2);
    }

    @Override
    public Integer part1() {
        Set<Point> infected = new HashSet<>(initialInfected);
        int infections = 0;

        Point pos = start;
        Dir4 dir = Dir4.NORTH;
        for (int i = 0; i < 10000; i++) {
            if (infected.contains(pos)) {
                infected.remove(pos);
                dir = dir.right();
            } else {
                infected.add(pos);
                dir = dir.left();
                infections++;
            }
            pos = pos.neighbour(dir);
        }

        return infections;
    }

    @Override
    public Integer part2() {
        Map<Point, State> states = new HashMap<>();
        for (Point pos : initialInfected)
            states.put(pos, State.INFECTED);
        int infections = 0;

        Point pos = start;
        Dir4 dir = Dir4.NORTH;
        for (int i = 0; i < 10000000; i++) {
            State state = states.getOrDefault(pos, State.CLEAN);
            State newState = State.values()[(state.ordinal() + 1) % 4];

            states.put(pos, newState);
            if (newState == State.INFECTED)
                infections++;

            dir = dir.right(state.ordinal() - 1);
            pos = pos.neighbour(dir);
        }

        return infections;
    }

    private static enum State { CLEAN, WEAKENED, INFECTED, FLAGGED }
}
