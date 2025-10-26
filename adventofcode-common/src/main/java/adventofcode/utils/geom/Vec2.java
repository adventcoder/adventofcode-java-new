package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vec2 {
    public final int x;
    public final int y;

    public int abs() {
        return Math.abs(x) + Math.abs(y);
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 subtract(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public int dot(Vec2 other) {
        return x*other.x + y*other.y;
    }

    public int cross(Vec2 other) {
        return x*other.y - y*other.x;
    }

    public Vec2 perpLeft() {
        return new Vec2(y, -x);
    }

    public Vec2 perpRight() {
        return new Vec2(-y, x);
    }
}
