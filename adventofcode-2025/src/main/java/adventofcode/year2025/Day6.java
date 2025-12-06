package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongFunction;
import java.util.stream.LongStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

@Puzzle(day = 6, name = "Trash Compactor")
public class Day6 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day6.class, args);
    }

    private static Map<String, ToLongFunction<long[]>> opMap = Map.of(
        "+", operands -> LongStream.of(operands).sum(),
        "*", operands -> LongStream.of(operands).reduce(1L, (x, y) -> x * y)
    );

    private List<ToLongFunction<long[]>> operators;
    private List<long[]> rowOperands;
    private List<long[]> colOperands;

    @Override
    public void parse(String input) {
        String[] rows = input.split("\n");
        parseRows(rows);
        parseColumns(rows);
    }

    private void parseRows(String[] rows) {
        operators = new ArrayList<>();
        rowOperands = new ArrayList<>();
        for (String token : rows[rows.length - 1].trim().split("\\s+")) {
            operators.add(opMap.get(token));
            rowOperands.add(new long[rows.length - 1]);
        }
        for (int y = 0; y < rows.length - 1; y++) {
            String[] tokens = rows[y].trim().split("\\s+");
            for (int i = 0; i < tokens.length; i++)
                rowOperands.get(i)[y] = Integer.parseInt(tokens[i]);
        }
    }

    private void parseColumns(String[] rows) {
        int maxLength = 0;
        for (String row : rows)
            maxLength = Math.max(maxLength, row.length());
        colOperands = new ArrayList<>();
        LongList curr = new LongArrayList();
        for (int x = 0; x < maxLength; x++) {
            String token = getColumn(rows, x).trim();
            if (token.isEmpty()) {
                colOperands.add(curr.toLongArray());
                curr.clear();
            } else {
                curr.add(Long.parseLong(token));
            }
        }
        colOperands.add(curr.toLongArray());
    }

    private String getColumn(String[] rows, int x) {
        char[] arr = new char[rows.length - 1];
        for (int y = 0; y < rows.length - 1; y++)
            arr[y] = x < rows[y].length() ? rows[y].charAt(x) : ' ';
        return new String(arr);
    }

    @Override
    public Long part1() {
        long total = 0;
        for (int i = 0; i < operators.size(); i++)
            total += operators.get(i).applyAsLong(rowOperands.get(i));
        return total;
    }

    @Override
    public Long part2() {
        long total = 0;
        for (int i = 0; i < operators.size(); i++)
            total += operators.get(i).applyAsLong(colOperands.get(i));
        return total;
    }
}
