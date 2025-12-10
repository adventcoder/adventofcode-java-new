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
import adventofcode.utils.geom.Rect;
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
                Rect rect = Rect.of(points.get(i), points.get(j));
                maxArea = Math.max(maxArea, rect.area());
            }
        }
        return maxArea;
    }

    @Override
    public Long part2() {
        long maxArea = 0;
        for (int i = 0; i < points.size(); i++) {
            for (int j = i + 1; j < points.size(); j++) {
                Rect rect = Rect.of(points.get(i), points.get(j));
                if (containsRect(rect))
                    maxArea = Math.max(maxArea, rect.area());
            }
        }
        return maxArea;
    }

    private boolean containsRect(Rect rect) {
        Point a = points.get(points.size() - 1);
        for (Point b : points) {
            Rect edge = Rect.of(a, b);
            // found an edge that overlaps the interior of the rect (excluding the boundary)
            // in otherwords the rect is partly on either side of the edge, and therefore not fully contained within the polygon
            if (edge.overlapsExclusive(rect)) return false;
            a = b;
        }
        return true;
    }

    private void writeImage(int width, int height, int factor) {
        if (imageFile == null) return;
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Point a = points.get(points.size() - 1);
        for (Point b : points) {
            Rect edge = Rect.of(a, b);
            for (int y = edge.yMin / factor; y <= edge.yMax / factor; y++)
                for (int x = edge.xMin / factor; x <= edge.xMax / factor; x++)
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
