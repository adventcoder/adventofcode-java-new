package adventofcode.utils.geom;

public record Vector3(int x, int y, int z) {
    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y) + Math.abs(z);
    }

    public int magnitudeSquared() {
        return x*x + y*y + z*z;
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
