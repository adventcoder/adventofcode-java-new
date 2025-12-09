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

    @Option(names = "--image-file")
    private File imageFile;

    private List<Point> points;

    @Override
    public void parse(String input) {
        points = new ArrayList<>();
        for (String line : input.split("\n"))
            points.add(Fn.parseIntPair(line, ",", Point::new));
        writeImage(1000, 1000, 100);
    }

    @Override
    public Long part1() {
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Point a = points.get(i), b = points.get(j);
                maxArea = Math.max(maxArea, rectArea(a, b));
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
                if (containsRect(a, b))
                    maxArea = Math.max(maxArea, rectArea(a, b));
            }
        }
        return maxArea;
    }

    private boolean containsRect(Point a, Point b) {
        int xMin = Math.min(a.x, b.x), xMax = Math.max(a.x, b.x);
        int yMin = Math.min(a.y, b.y), yMax = Math.max(a.y, b.y);
        Point c = points.get(points.size() - 1);
        for (Point d : points) {
            int xMinEdge = Math.min(c.x, d.x), xMaxEdge = Math.max(c.x, d.x);
            int yMinEdge = Math.min(c.y, d.y), yMaxEdge = Math.max(c.y, d.y);

            // found an edge that overlaps the rect interior
            // this means the rect is not fully inside the polygon
            boolean xOverlaps = xMaxEdge > xMin && xMinEdge < xMax;
            boolean yoverlaps = yMaxEdge > yMin && yMinEdge < yMax;
            if (xOverlaps && yoverlaps)
                return false;

            c = d;
        }
        return true;
    }

    private static long rectArea(Point a, Point b) {
        int width = Math.abs(b.x - a.x) + 1;
        int height = Math.abs(b.y - a.y) + 1;
        return ((long) width) * height;
    }

    private void writeImage(int width, int height, int factor) {
        if (imageFile == null) return;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Point a = points.get(points.size() - 1);
        for (Point b : points) {
            int xMin = Math.min(a.x, b.x) / factor, xMax = Math.max(a.x, b.x) / factor;
            int yMin = Math.min(a.y, b.y) / factor, yMax = Math.max(a.y, b.y) / factor;
            for (int y = yMin; y <= yMax; y++)
                for (int x = xMin; x <= xMax; x++)
                    img.setRGB(x, y, 0x00FF00);
            a = b;
        }
        for (Point p : points)
            img.setRGB(p.x / factor, p.y / factor, 0xFF0000);
        try {
            ImageIO.write(img, "png", imageFile);
        } catch (IOException ioe) {
            warn(ioe.getMessage());
        }
    }
}
