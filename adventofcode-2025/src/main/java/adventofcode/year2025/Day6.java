package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongBinaryOperator;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Grid;

@Puzzle(day = 6, name = "Trash Compactor")
public class Day6 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day6.class, args);
    }

    private static Map<String, LongBinaryOperator> opMap = Map.of("+", Long::sum, "*", (x, y) -> x * y);

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
        return Fn.sumLong(problems, this::evalRows);
    }

    @Override
    public Long part2() {
        return Fn.sumLong(problems, this::evalColumns);
    }

    private long evalRows(Grid problem) {
        return IntStream.range(0, problem.height - 1)
            .mapToLong(y -> Long.parseLong(problem.getRow(y).trim()))
            .reduce(opMap.get(problem.getRow(problem.height - 1).trim()))
            .orElseThrow();
    }
    
    private long evalColumns(Grid problem) {
        return IntStream.range(0, problem.width)
            .mapToLong(x -> Long.parseLong(problem.getColumn(x, 0, problem.height - 1).trim()))
            .reduce(opMap.get(problem.getRow(problem.height - 1).trim()))
            .orElseThrow();
    }
}
