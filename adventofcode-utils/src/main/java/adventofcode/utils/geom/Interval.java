package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Interval {
    public int min;
    public int max;

    public boolean isEmpty() {
        return min > max;
    }

    public int size() {
        return max - min + 1;
    }

    public boolean overlaps(Interval other) {
        return min <= other.max && other.min <= max;
    }

    public boolean contains(Interval other) {
        return min <= other.min && max >= other.max;
    }

    public Interval or(Interval other) {
        return new Interval(Math.min(min, other.min), Math.max(max, other.max));
    }

    public Interval and(Interval other) {
        return new Interval(Math.max(min, other.min),  Math.min(max, other.max));
    }
}