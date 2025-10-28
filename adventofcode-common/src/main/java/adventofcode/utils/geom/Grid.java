package adventofcode.utils.geom;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Grid {
    private final Map<Vec2, Integer> vals = new HashMap<>();
    private final int fill;

    public Grid(int fill) {
        this.fill = fill;
    }

    public Grid(String input, char fill) {
        this(fill);
        String[] lines = input.split("\n");
        for (int y = 0; y < lines.length; y++) {
            for (int x = 0; x < lines[y].length(); x++) {
                char c = lines[y].charAt(x);
                if (c != fill)
                    vals.put(new Vec2(x, y), (int) c);
            }
        }
    }

    public int get(Vec2 pos) {
        return vals.getOrDefault(pos, fill);
    }

    public void set(Vec2 pos, int val) {
        if (val == fill) {
            vals.remove(pos);
        } else {
            vals.put(pos, val);
        }
    }

    public Stream<Vec2> findAll(int val) {
        return vals.keySet().stream().filter(p -> vals.get(p) == val);
    }

    public Vec2 findFirst(int val) {
        return findAll(val).min(Comparator.naturalOrder()).orElseThrow();
    }
}
