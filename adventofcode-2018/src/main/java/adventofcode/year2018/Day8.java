package adventofcode.year2018;

import java.util.PrimitiveIterator;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.array.IntArrays;

@Puzzle(day = 8, name = "Memory Maneuver")
public class Day8 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day8.class, args);
    }

    private int[] data;

    @Override
    public void parse(String input) {
        data = Fn.parseInts(input, "\\s+");
    }

    @Override
    public Integer part1() {
        return sumEntries(IntArrays.iterator(data));
    }

    private int sumEntries(PrimitiveIterator.OfInt it) {
        int numChildren = it.nextInt();
        int numEntries = it.nextInt();
        int total = 0;
        for (int i = 0; i < numChildren; i++)
            total += sumEntries(it);
        for (int i = 0; i < numEntries; i++)
            total += it.nextInt();
        return total;
    }

    @Override
    public Object part2() {
        return value(IntArrays.iterator(data));
    }

    private int value(PrimitiveIterator.OfInt it) {
        int numChildren = it.nextInt();
        int numEntries = it.nextInt();
        int total = 0;
        if (numChildren == 0) {
            for (int i = 0; i < numEntries; i++)
                total += it.nextInt();
        } else {
            int[] childValues = new int[numChildren];
            for (int i = 0; i < numChildren; i++)
                childValues[i] = value(it);
            for (int i = 0; i < numEntries; i++) {
                int n = it.nextInt();
                if (n >= 1 && n <= childValues.length)
                    total += childValues[n - 1];
            }
        }
        return total;
    }
}
