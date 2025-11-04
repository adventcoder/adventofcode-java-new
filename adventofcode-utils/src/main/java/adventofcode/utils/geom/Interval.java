package adventofcode.utils.geom;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Interval {
    public int min;
    public int max;

    public boolean overlaps(Interval other) {
        return min <= other.max && other.min <= max;
    }

    public Interval or(Interval other) {
        return new Interval(Math.min(min, other.min), Math.max(max, other.max));
    }

    public Interval and(Interval other) {
        return new Interval(Math.max(min, other.min),  Math.min(max, other.max));
    }
}