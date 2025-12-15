package adventofcode.utils.geom;

public record LongInterval(long min, long max) {
    public static LongInterval empty() {
        return new LongInterval(Long.MAX_VALUE, Long.MIN_VALUE);
    }

    public boolean isEmpty() {
        return min > max;
    }

    public long size() {
        return max - min + 1;
    }

    public boolean overlaps(LongInterval other) {
        return min <= other.max && other.min <= max;
    }

    public boolean contains(LongInterval other) {
        return min <= other.min && max >= other.max;
    }

    public boolean contains(long val) {
        return min <= val && max >= val;
    }

    public LongInterval or(LongInterval other) {
        return new LongInterval(Math.min(min, other.min), Math.max(max, other.max));
    }

    public LongInterval or(int n) {
        return new LongInterval(Math.min(min, n), Math.max(max, n));
    }

    public LongInterval and(LongInterval other) {
        return new LongInterval(Math.max(min, other.min),  Math.min(max, other.max));
    }
}