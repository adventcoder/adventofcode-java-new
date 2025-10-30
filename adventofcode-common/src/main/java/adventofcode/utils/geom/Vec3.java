package adventofcode.utils.geom;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Vec3 {
    public final int x;
    public final int y;
    public final int z;

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public Vec3 negate() {
        return new Vec3(-x, -y, -z);
    }

    public Vec3 add(Vec3 other) {
        return new Vec3(x + other.x, y + other.y, z + other.z);
    }

    public Vec3 subtract(Vec3 other) {
        return new Vec3(x - other.x, y - other.y, z - other.z);
    }

    public Vec3 multiply(int n) {
        return new Vec3(x * n, y * n, z * n);
    }

    public int dot(Vec3 other) {
        return x*other.x + y*other.y + z*other.z;
    }

    public Vec3 cross(Vec3 other) {
        return new Vec3(y*other.z - z*other.y, z*other.x - x*other.z, x*other.y - y*other.x);
    }
}
