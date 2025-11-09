package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.math.IntVec2;
import picocli.CommandLine.Option;

@Puzzle(day = 6, name = "Chronal Coordinates")
public class Day6 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day6.class, args);
    }

    @Option(names = "--radius", defaultValue = "10000")
    private int radius;

    private List<IntVec2> seeds;

    @Override
    public void parse(String input) {
        seeds = new ArrayList<>();
        for (String line : input.split("\n"))
            seeds.add(IntVec2.parse(line, ","));
    }

    @Override
    public Object part1() {
        return null;
    }

    @Override
    public Integer part2() {
        List<IntVec2> corners = findCorners();
        int perim = 0;
        int area = 0;
        IntVec2 a = corners.get(corners.size() - 1);
        for (IntVec2 b : corners) {
            perim += a.distance(b);
            area += a.cross(b);
            a = b;
        }
        return (area + perim) / 2 + 1;
    }

    private List<IntVec2> findCorners() {
        List<IntVec2> corners = new ArrayList<>();
        IntVec2 start = findStartPoint();
        System.out.println("start: " + start);

        return corners;
    }

    private IntVec2 findStartPoint() {
        IntVec2 center = findCentroid();
        int xMax = seeds.stream().mapToInt(s -> s.x).max().orElseThrow();
        int startX = Fn.bsearchLast(center.x, xMax, x -> sdf(new IntVec2(x, center.y)) >= 0 ? 0 : 1);
        return new IntVec2(startX, center.y);
    }

    private IntVec2 findCentroid() {
        int i = seeds.size() / 2;
        seeds.sort(Comparator.comparingInt(s -> s.x));
        int cx = seeds.size() % 2 == 0 ? (seeds.get(i - 1).x + seeds.get(i).x) / 2 : seeds.get(i).x;
        seeds.sort(Comparator.comparingInt(s -> s.y));
        int cy = seeds.size() % 2 == 0 ? (seeds.get(i - 1).y + seeds.get(i).y) / 2 : seeds.get(i).y;
        return new IntVec2(cx, cy);
    }

    private int sdf(IntVec2 p) {
        return radius - 1 - Fn.sum(seeds, p::distance);
    }
}
