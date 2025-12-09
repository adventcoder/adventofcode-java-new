package adventofcode.year2025;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Point;
import picocli.CommandLine.Option;

@Puzzle(day = 9, name = "Movie Theater")
public class Day9 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day9.class, args);
    }

    @Option(names = "--image-name")
    private String imageName;

    private List<Point> points;

    @Override
    public void parse(String input) {
        points = new ArrayList<>();
        for (String line : input.split("\n"))
            points.add(Fn.parseIntPair(line, ",", Point::new));
        writeImage();
    }

    @Override
    public Long part1() {
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point a = points.get(i), b = points.get(j);
                maxArea = Math.max(maxArea, area(a, b));
            }
        }
        return maxArea;
    }

    @Override
    public Long part2() {
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point a = points.get(i), b = points.get(j);
                if (contains(new Point(a.x, b.y)) && contains(new Point(b.x, a.y)))
                    maxArea = Math.max(maxArea, area(a, b));
            }
        }
        return maxArea;
    }

    private boolean contains(Point p) {
        boolean inside = false;
        Point a = points.get(points.size() - 1);
        for (Point b : points) {
            if (a.y == b.y) {
                // Horizontal edge Intersection
                if (p.y == a.y) {
                    // Check p is on the edge
                    if (p.x >= Math.min(a.x, b.x) && p.x <= Math.max(a.x, b.x))
                        return true;
                }
            }
            else {
                // Vertical edge intersection
                if (p.y >= Math.min(a.y, b.y) && p.y <= Math.max(a.y, b.y)) {
                    // Check p is on the edge
                    if (p.x == a.x) return true;
                    // Ray cast: vertical edge intersects ray iff its x is > p.x
                    if (a.x > p.x) inside = !inside;
                }
            }
            a = b;
        }
        return inside;
    }

    private static long area(Point a, Point b) {
        int width = Math.abs(b.x - a.x) + 1;
        int height = Math.abs(b.y - a.y) + 1;
        return ((long) width) * height;
    }

    private void writeImage() {
        if (imageName == null) return;
        int factor = 100;
        BufferedImage img = new BufferedImage(100_000 / factor, 100_000 / factor, BufferedImage.TYPE_INT_RGB);
        Point a = points.get(points.size() - 1);
        for (Point b : points) {
            img.setRGB(b.x / factor, b.y / factor, 0xFF0000);
            if (a.x == b.x) {
                int x = a.x / factor;
                int y0 = Math.min(a.y, b.y) / factor, y1 = Math.max(a.y, b.y) / factor;
                for (int y = y0 + 1; y < y1; y++) img.setRGB(x, y, 0x00FF00);
            } else {
                int x0 = Math.min(a.x, b.x) / factor, x1 = Math.max(a.x, b.x) / factor;
                int y = a.y / factor;
                for (int x = x0 + 1; x < x1; x++) img.setRGB(x, y, 0x00Ff00);
            }
            a = b;
        }
        try {
            ImageIO.write(img, "png", new File(imageName + ".png"));
        } catch (IOException ioe) {
            warn(ioe.getMessage());
        }
    }
}
