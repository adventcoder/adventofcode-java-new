package adventofcode.utils.geom;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Dir8 {
    NORTH(0, -1),
    NORTHEAST(1, -1),
    EAST(1, 0),
    SOUTHEAST(1, 1),
    SOUTH(0, 1),
    SOUTHWEST(-1, 1),
    WEST(-1, 0),
    NORTHWEST(-1, -1);

    public final int x;
    public final int y;

    static final Dir8[] cachedValues = values();

    public Dir8 right45(int n) {
        return cachedValues[(ordinal() + n) & 7];
    }

    public Dir8 left45(int n) {
        return right45(-n);
    }

    public Dir8 right45() {
        return right45(1);
    }

    public Dir8 right90() {
        return right45(2);
    }

    public Dir8 right135() {
        return right45(3);
    }

    public Dir8 opposite() {
        return right45(4);
    }

    public Dir8 left135() {
        return right45(5);
    }

    public Dir8 left90() {
        return right45(6);
    }

    public Dir8 left45() {
        return right45(7);
    }

    public Dir8 reflect(Dir8 a, Dir8 b) {
        return cachedValues[(a.ordinal() + b.ordinal() - ordinal()) & 7];
    }

    public Dir8 reflect(Dir8 a) {
        return reflect(a, a);
    }
}
