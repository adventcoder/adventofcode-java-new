package adventofcode.year2018;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.geom.Dir4;
import adventofcode.utils.geom.Point;
import lombok.AllArgsConstructor;

@Puzzle(day = 13, name = "Mine Cart Madness")
public class Day13 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day13.class, args);
    }

    private static final Map<Character, Dir4> dirMap = Map.of('^', Dir4.NORTH, '>', Dir4.EAST, 'v', Dir4.SOUTH, '<', Dir4.WEST);

    private String[] track;
    private List<Cart> carts = new ArrayList<>();
    private List<Point> collisions = new ArrayList<>();

    @Override
    public void parse(String input) {
        track = input.split("\n");
        for (int y = 0; y < track.length; y++) {
            for (int x = 0; x < track[y].length(); x++) {
                Dir4 dir = dirMap.get(track[y].charAt(x));
                if (dir != null)
                    carts.add(new Cart(x, y, dir, 0, false));
            }
        }
    }

    @Override
    public String part1() {
        while (collisions.isEmpty())
            tick();
        Point coll = collisions.get(0);
        return coll.x + "," + coll.y;
    }

    @Override
    public String part2() {
        while (carts.size() > 1)
            tick();
        Cart cart = carts.get(0);
        return cart.x + "," + cart.y;
    }

    private void tick() {
        carts.sort(Cart::comparePosition);
        for (Cart cart : carts) {
            if (cart.dead) continue;
            cart.tick(track);
            // Just do the quadratic loop, we won't bother keeping track of pos -> cart
            for (Cart other : carts) {
                if (cart != other && cart.comparePosition(other) == 0 && !other.dead) {
                    cart.dead = true;
                    other.dead = true;
                    collisions.add(new Point(cart.x, cart.y));
                }
            }
        }
        carts.removeIf(c -> c.dead);
    }

    @AllArgsConstructor
    private static class Cart {
        private int x;
        private int y;
        private Dir4 dir;
        private int state;
        private boolean dead;

        public void tick(String[] track) {
            x += dir.x;
            y += dir.y;
            switch (track[y].charAt(x)) {
                case '/' -> dir = dir.reflect(Dir4.NORTH, Dir4.EAST);
                case '\\' -> dir = dir.reflect(Dir4.SOUTH, Dir4.EAST);
                case '+' -> {
                    dir = dir.right90(state - 1);
                    state = (state + 1) % 3;
                }
            }
        }

        public int comparePosition(Cart other) {
            return y == other.y ? x - other.x : y - other.y;
        }
    }
}
