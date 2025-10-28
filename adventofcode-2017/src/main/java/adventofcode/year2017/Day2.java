package adventofcode.year2017;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 2, name = "Corruption Checksum")
public class Day2 extends AbstractDay {
    private List<int[]> rows = new ArrayList<>();

    @Override
    public void parse(String input) {
        for (String line : input.split("\n"))
            rows.add(Fn.parseInts(line, "\\s+"));
    }

    @Override
    public Integer part1() {
        return rows.stream().mapToInt(this::checksum1).sum();
    }

    @Override
    public Integer part2() {
        return rows.stream().mapToInt(this::checksum2).sum();
    }

    private int checksum1(int[] row) {
        int min = IntStream.of(row).min().orElseThrow();
        int max = IntStream.of(row).max().orElseThrow();
        return max - min;
    }

    private int checksum2(int[] row) {
        Arrays.sort(row);
        for (int i = 0; i < row.length; i++)
            for (int j = i + 1; j < row.length; j++)
                if (row[j] % row[i] == 0)
                    return row[j] / row[i];
        throw new NoSuchElementException();
    }
}
