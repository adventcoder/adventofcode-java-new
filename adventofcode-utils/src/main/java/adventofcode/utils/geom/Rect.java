package adventofcode.utils.geom;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Rect {
    public final int xMin;
    public final int yMin;
    public final int xMax;
    public final int yMax;

    public int width() {
        return yMax - yMin + 1;
    }

    public int height() {
        return xMax - xMin + 1;
    }

    public boolean overlaps(Rect r) {
        return (xMin <= r.xMax && xMax >= r.xMin) && (yMin <= r.yMax && yMax >= r.yMin);
    }

    public Rect or(Rect r) {
        return new Rect(Math.min(xMin, r.xMin), Math.min(yMin, r.yMin), Math.max(xMax, r.xMax), Math.max(yMax, r.yMax));
    }

    public Rect and(Rect r) {
        return new Rect(Math.max(xMin, r.xMin),  Math.max(yMin, r.yMin), Math.min(xMax, r.xMax), Math.min(yMax, r.yMax));
    }
}
