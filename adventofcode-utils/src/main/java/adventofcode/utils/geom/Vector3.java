package adventofcode.utils.geom;

import it.unimi.dsi.fastutil.ints.AbstractIntList;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class Vector3 extends AbstractIntList {
    public final int x;
    public final int y;
    public final int z;

    @Override
    public int size() {
        return 3;
    }

    @Override
    public int getInt(int i) {
        return switch (i) {
            case 0 -> x;
            case 1 -> y;
            case 2 -> z;
            default -> throw new IndexOutOfBoundsException();
        };
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public Vector3 add(Vector3 v) {
        return new Vector3(x + v.x, y + v.y, z + v.z);
    }

    public Vector3 addMul(Vector3 v, int n) {
        return new Vector3(x + v.x*n, y + v.y*n, z + v.z*n);
    }

    public Vector3 subtract(Vector3 v) {
        return new Vector3(x - v.x, y - v.y, z - v.z);
    }

    public Vector3 subtractMul(Vector3 v, int n) {
        return new Vector3(x - v.x*n, y - v.y*n, z - v.z*n);
    }

    public Vector3 multiply(int n) {
        return new Vector3(x * n, y * n, z * n);
    }

    public int dot(Vector3 v) {
        return x*v.x + y*v.y + z*v.z;
    }

    public Vector3 cross(Vector3 v) {
        return new Vector3(y*v.z - z*v.y, z*v.x - x*v.z, x*v.y - y*v.x);
    }
}
