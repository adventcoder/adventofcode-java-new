package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Grid;

@Puzzle(day = 6, name = "Trash Compactor")
public class Day6 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day6.class, args);
    }

    private List<Grid> problems = new ArrayList<>();

    @Override
    public void parse(String input) {
        Grid sheet = Grid.parse(input);
        int start = 0;
        for (int x = 0; x < sheet.width; x++) {
            if (sheet.getColumn(x).isBlank()) {
                problems.add(sheet.slice(start, 0, x - start, sheet.height));
                start = x + 1;
            }
        }
        problems.add(sheet.slice(start, 0, sheet.width - start, sheet.height));
    }

    @Override
    public Long part1() {
        return Fn.sumLong(problems, p -> eval(p, this::operandsByRow));
    }

    @Override
    public Long part2() {
        return Fn.sumLong(problems, p -> eval(p, this::operandsByColumn));
    }

    private long eval(Grid problem, Function<Grid, LongStream> operands) {
        String op = problem.getRow(problem.height - 1).trim();
        return switch (op) {
            case "+" -> operands.apply(problem).sum();
            case "*" -> operands.apply(problem).reduce(1L, (x, y) -> x * y);
            default -> throw new UnsupportedOperationException();
        };
    }

    private LongStream operandsByRow(Grid problem) {
        return IntStream.range(0, problem.height - 1)
            .mapToLong(y -> Long.parseLong(problem.getRow(0, y, problem.width).trim()));
    }

    private LongStream operandsByColumn(Grid problem) {
        return IntStream.range(0, problem.width)
            .mapToLong(x -> Long.parseLong(problem.getColumn(x, 0, problem.height - 1).trim()));
    }

}
