package adventofcode.year2017;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Grid;
import adventofcode.utils.geom.Vec2;

@Puzzle(day = 19, name = "A Series of Tubes")
public class Day19 extends AbstractDay {
    private Grid grid;

    @Override
    public void parse(String input) {
        grid = new Grid(input, ' ');
    }

    @Override
    public String part1() {
        StringBuilder seq = new StringBuilder();
        traverse(pos -> {
            char c = (char) grid.get(pos);
            if (Character.isLetter(c))
                seq.append(c);
        });
        return seq.toString();
    }

    @Override
    public Integer part2() {
        List<Vec2> path = new ArrayList<>();
        traverse(path::add);
        return path.size();
    }

    private void traverse(Consumer<Vec2> action) {
        Vec2 pos = grid.findFirst('|');
        Vec2 dir = Vec2.SOUTH;
        while (grid.get(pos) != ' ') {
            action.accept(pos);
            if (grid.get(pos) == '+')
                dir = grid.get(pos.add(dir.perpLeft())) != ' ' ? dir.perpLeft() : dir.perpRight();
            pos = pos.add(dir);
        }
    };
}
