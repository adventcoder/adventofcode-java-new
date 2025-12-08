package adventofcode.utils.geom;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Point {
    public final int x;
    public final int y;

    public Point north() {
        return new Point(x, y - 1);
    }

    public Point south() {
        return new Point(x, y + 1);
    }

    public Point west() {
        return new Point(x - 1, y);
    }

    public Point east() {
        return new Point(x + 1, y);
    }

    public Point neighbour(Dir4 dir) {
        return new Point(x + dir.x, y + dir.y);
    }

    public Point neighbour(Dir8 dir) {
        return new Point(x + dir.x, y + dir.y);
    }

    public List<Point> neighbours4() {
        return neighbours4(x, y);
    }

    public static List<Point> neighbours4(int x, int y) {
        return List.of(
            new Point(x - 1, y),
            new Point(x + 1, y),
            new Point(x, y - 1),
            new Point(x, y + 1)
        );
    }

    public List<Point> neighbours8() {
        return neighbours8(x, y);
    }

    public static List<Point> neighbours8(int x, int y) {
        return List.of(
            new Point(x - 1, y - 1),
            new Point(x, y - 1),
            new Point(x + 1, y - 1),
            new Point(x - 1, y),
            new Point(x + 1, y),
            new Point(x - 1, y + 1),
            new Point(x, y + 1),
            new Point(x + 1, y + 1)
        );
    }

    public int distanceToOrigin() {
        return Math.abs(x) + Math.abs(y);
    }

    public int distanceTo(Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }
}
