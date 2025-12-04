package adventofcode.utils.geom;

import java.util.AbstractList;
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

    public Point neighbour(Dir4 dir) {
        return new Point(x + dir.x, y + dir.y);
    }

    public Point neighbour(Dir8 dir) {
        return new Point(x + dir.x, y + dir.y);
    }

    public int distanceToOrigin() {
        return Math.abs(x) + Math.abs(y);
    }

    public int distanceTo(Point other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public List<Point> neighbours4() {
        return new AbstractList<>() {
            @Override
            public int size() {
                return Dir4.values.size();
            }

            @Override
            public Point get(int i) {
                return neighbour(Dir4.values.get(i));
            }
        };
    }

    public List<Point> neighbours8() {
        return new AbstractList<>() {
            @Override
            public int size() {
                return Dir8.values.size();
            }

            @Override
            public Point get(int i) {
                return neighbour(Dir8.values.get(i));
            }
        };
    }
}
