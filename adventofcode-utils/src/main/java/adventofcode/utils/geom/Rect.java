package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Rect {
    public final int xMin;
    public final int yMin;
    public final int xMax;
    public final int yMax;

    public static Rect empty() {
        return new Rect(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public boolean isEmpty() {
        return xMin > xMax || yMin > yMax;
    }

    public int width() {
        return xMax - xMin + 1;
    }

    public int height() {
        return yMax - yMin + 1;
    }

    public boolean overlaps(Rect r) {
        return (xMin <= r.xMax && xMax >= r.xMin) && (yMin <= r.yMax && yMax >= r.yMin);
    }

    public boolean contains(Rect r) {
        return (xMin <= r.xMin && xMax >= r.xMax) && (yMin <= r.yMin && yMax >= r.yMax);
    }

    public boolean contains(Point p) {
        return (xMin <= p.x && xMax >= p.x) && (yMin <= p.y && yMax >= p.y);
    }

    public Rect or(Rect r) {
        return new Rect(Math.min(xMin, r.xMin), Math.min(yMin, r.yMin), Math.max(xMax, r.xMax), Math.max(yMax, r.yMax));
    }

    public Rect or(Point p) {
        return new Rect(Math.min(xMin, p.x), Math.min(yMin, p.y), Math.max(xMax, p.x), Math.max(yMax, p.y));
    }

    public Rect and(Rect r) {
        return new Rect(Math.max(xMin, r.xMin),  Math.max(yMin, r.yMin), Math.min(xMax, r.xMax), Math.min(yMax, r.yMax));
    }
}
