package adventofcode.utils.geom;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

//TODO: decide how to handle long/int consistently
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Interval {
    public long min;
    public long max;

    public static Interval empty() {
        return new Interval(Long.MAX_VALUE, Long.MIN_VALUE);
    }

    public boolean isEmpty() {
        return min > max;
    }

    public long size() {
        return max - min + 1;
    }

    public boolean overlaps(Interval other) {
        return min <= other.max && other.min <= max;
    }

    public boolean contains(Interval other) {
        return min <= other.min && max >= other.max;
    }

    public boolean contains(long val) {
        return min <= val && max >= val;
    }

    public Interval or(Interval other) {
        return new Interval(Math.min(min, other.min), Math.max(max, other.max));
    }

    public Interval or(int n) {
        return new Interval(Math.min(min, n), Math.max(max, n));
    }

    public Interval and(Interval other) {
        return new Interval(Math.max(min, other.min),  Math.min(max, other.max));
    }

    public static List<Interval> union(List<Interval> ivs) {
        List<Interval> result = new ArrayList<>();
        ivs.sort(Comparator.comparingLong(iv -> iv.min));
        Interval curr = ivs.get(0);
        for (int i = 1; i < ivs.size(); i++) {
            Interval next = ivs.get(i);
            if (next.min <= curr.max + 1) {
                curr = new Interval(curr.min, Math.max(curr.max, next.max));
            } else {
                result.add(curr);
                curr = next;
            }
        }
        result.add(curr);
        return result;
    }
}