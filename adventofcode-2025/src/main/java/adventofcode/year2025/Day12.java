package adventofcode.year2025;

import java.util.ArrayList;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;

@Puzzle(day = 12)
public class Day12 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day12.class, args);
    }

    private int[] areas;
    private List<Region> regions;

    @Override
    public void parse(String input) {
        String[] chunks = input.split("\n\n");

        areas = new int[chunks.length - 1];
        for (int i = 0; i < chunks.length - 1; i++) {
            String[] lines = chunks[i].split("\n");
            for (int y = 1; y < lines.length; y++)
                for (int x = 0; x < lines[y].length(); x++)
                    if (lines[y].charAt(x) == '#')
                        areas[i]++;
        }

        regions = new ArrayList<>();
        for (String line : chunks[chunks.length - 1].split("\n")) {
            String[] pair = line.split(":", 2);
            int[] dims = Fn.parseInts(pair[0].trim(), "x");
            int[] counts = Fn.parseInts(pair[1].trim(), "\\s+");
            regions.add(new Region(dims[0], dims[1], counts));
        }
    }

    @Override
    public Integer part1() {
        int count = 0;
        for (Region r : regions) {
            if (r.neverFits(areas)) continue;
            if (!r.alwaysFits(3, 3)) throw new AssertionError("don't tell me I have to actually solve the problem!");
            count++;
        }
        return count;
    }

    public record Region(int width, int height, int[] counts) {
        public boolean neverFits(int[] areas) {
            int minArea = 0;
            for (int i = 0; i < counts.length; i++)
                minArea += areas[i]*counts[i];
            return width * height <= minArea;
        }

        public boolean alwaysFits(int shapeWidth, int shapeHeight) {
            int maxWidth = shapeWidth * counts.length;
            int maxHeight = shapeHeight * counts.length;
            return width >= maxWidth && height >= maxHeight;
        }
    }
}
