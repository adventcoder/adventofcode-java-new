package adventofcode.utils.geom;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Dir4 {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    public final int x;
    public final int y;

    static final Dir4[] cachedValues = values();

    public Dir4 right90(int n) {
        return cachedValues[(ordinal() + n) & 3];
    }

    public Dir4 left90(int n) {
        return right90(-n);
    }

    public Dir4 right90() {
        return right90(1);
    }

    public Dir4 opposite() {
        return right90(2);
    }

    public Dir4 left90() {
        return right90(3);
    }

    public Dir4 reflect(Dir4 a, Dir4 b) {
        return cachedValues[(a.ordinal() + b.ordinal() - ordinal()) & 3];
    }

    public Dir4 reflect(Dir4 a) {
        return reflect(a, a);
    }
}