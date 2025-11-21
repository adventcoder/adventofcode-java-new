package adventofcode.year2018;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Dir4;
import adventofcode.utils.geom.Point;
import picocli.CommandLine.Option;

@Puzzle(day = 6, name = "Chronal Coordinates")
public class Day6 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day6.class, args);
    }

    @Option(names = "--radius", defaultValue = "10000")
    private int radius;

    private List<Point> seeds;

    @Override
    public void parse(String input) {
        seeds = new ArrayList<>();
        for (String line : input.split("\n")) {
            String[] strs = line.split(",");
            int x = Integer.parseInt(strs[0].trim());
            int y = Integer.parseInt(strs[1].trim());
            seeds.add(new Point(x, y));
        }
    }

    @Override
    public Object part1() {
        return null;
    }

    @Override
    public Integer part2() {
        List<Point> corners = findCorners();
        int perim = 0;
        int area = 0;
        Point a = corners.get(corners.size() - 1);
        for (Point b : corners) {
            perim += a.distanceTo(b);
            area += a.x*b.y - a.y*b.x;
            a = b;
        }
        return (area + perim) / 2 + 1;
    }

    private List<Point> findCorners() {
        // Since the centroid minimizes the total distance, it's guaranteed to be inside the shape for any possible valid radius.
        Point center = findCentroid();

        // Find a point on the left edge
        Point start = center;
        while (contains(start.neighbour(Dir4.WEST)))
            start = start.neighbour(Dir4.WEST);

        // Trace the boundary
        List<Point> corners = new ArrayList<>();
        Point curr = start;
        Dir4 dir = Dir4.NORTH;
        do {
            if (contains(curr.neighbour(dir.left()))) {
                corners.add(curr);
                dir = dir.left();
            } else if (!contains(curr.neighbour(dir))) {
                corners.add(curr);
                dir = dir.right();
            }
            curr = curr.neighbour(dir);
        } while (!curr.equals(start));

        return corners;
    }

    private Point findCentroid() {
        int i = seeds.size() / 2;
        seeds.sort(Comparator.comparingInt(s -> s.x));
        int cx = seeds.size() % 2 == 0 ? (seeds.get(i - 1).x + seeds.get(i).x) / 2 : seeds.get(i).x;
        seeds.sort(Comparator.comparingInt(s -> s.y));
        int cy = seeds.size() % 2 == 0 ? (seeds.get(i - 1).y + seeds.get(i).y) / 2 : seeds.get(i).y;
        return new Point(cx, cy);
    }

    private boolean contains(Point p) {
        return Fn.sum(seeds, p::distanceTo) < radius;
    }
}
