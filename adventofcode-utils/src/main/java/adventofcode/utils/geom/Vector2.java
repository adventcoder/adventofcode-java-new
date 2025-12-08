package adventofcode.utils.geom;

public record Vector2(int x, int y) {
    public boolean isZero() {
        return x == 0 && y == 0;
    }

    public int abs() {
        return Math.abs(x) + Math.abs(y);
    }

    public long magnitudeSquared() {
        return ((long) x)*x + ((long) y)*y;
    }

    public Vector2 add(Vector2 v) {
        return new Vector2(x + v.x, y + v.y);
    }

    public Vector2 addMul(Vector2 v, int n) {
        return new Vector2(x + v.x*n, y + v.y*n);
    }

    public Vector2 subtract(Vector2 v) {
        return new Vector2(x - v.x, y - v.y);
    }

    public Vector2 subtractMul(Vector2 v, int n) {
        return new Vector2(x - v.x*n, y - v.y*n);
    }

    public Vector2 multiply(int n) {
        return new Vector2(x * n, y * n);
    }

    public long dot(Vector2 v) {
        return ((long) x)*v.x + ((long) y)*v.y;
    }

    public int cross(Vector2 v) {
        return x*v.y - y*v.x;
    }
}
