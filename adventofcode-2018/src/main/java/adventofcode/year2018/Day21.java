package adventofcode.year2018;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.year2018.utils.Program;

@Puzzle(day = 21, name = "Chronal Conversion")
public class Day21 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day21.class, args);
    }

    private Program program;

    @Override
    public void parse(String input) {
        program = Program.parse(input);
        program.debug(this);
    }
}
