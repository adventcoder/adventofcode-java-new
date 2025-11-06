package adventofcode.utils.math;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class IntVec3 {
    public static final IntVec3 ZERO = new IntVec3(0, 0, 0);

    public final int x;
    public final int y;
    public final int z;

    public static IntVec3 parse(String s, String sep) {
        String[] tokens = s.split(sep);
        int x = Integer.parseInt(tokens[0].trim());
        int y = Integer.parseInt(tokens[1].trim());
        int z = Integer.parseInt(tokens[2].trim());
        return new IntVec3(x, y, z);
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public IntVec3 negate() {
        return new IntVec3(-x, -y, -z);
    }

    public IntVec3 add(IntVec3 other) {
        return new IntVec3(x + other.x, y + other.y, z + other.z);
    }

    public IntVec3 subtract(IntVec3 other) {
        return new IntVec3(x - other.x, y - other.y, z - other.z);
    }

    public IntVec3 multiply(int n) {
        return new IntVec3(x * n, y * n, z * n);
    }

    public IntVec3 pairMultiply(IntRot3 r) {
        // ¬r v r = (a^2 - b.b) v + 2 a bxv + 2 v.b b
        IntVec3 par = multiply(r.a*r.a - r.B.dot(r.B)); // component along v
        IntVec3 proj = r.B.multiply(2*dot(r.B)); // component along normal b
        IntVec3 perp = r.B.cross(multiply(2*r.a)); // component perpendicular to both v and normal b
        return par.add(perp).add(proj);
    }

    public IntRot3 multiply(IntVec3 other) {
        return new IntRot3(dot(other), cross(other));
    }

    public int dot(IntVec3 other) {
        return x*other.x + y*other.y + z*other.z;
    }

    public IntVec3 cross(IntVec3 other) {
        return new IntVec3(y*other.z - z*other.y, z*other.x - x*other.z, x*other.y - y*other.x);
    }
}
