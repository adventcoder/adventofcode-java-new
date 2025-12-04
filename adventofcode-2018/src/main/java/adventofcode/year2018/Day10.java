package adventofcode.year2018;

import java.util.ArrayList;
import java.util.List;

import adventofcode.AbstractDay;
import adventofcode.Puzzle;
import adventofcode.utils.Fn;
import adventofcode.utils.geom.Grid;
import adventofcode.utils.geom.Point;
import adventofcode.utils.geom.Rect;

@Puzzle(day = 10, name = "The Stars Align")
public class Day10 extends AbstractDay {
    public static void main(String[] args) throws Exception {
        main(Day10.class, args);
    }

    private int[][] pos;
    private int[][] vel;

    @Override
    public void parse(String input) {
        String[] lines = input.split("\\n");
        pos = new int[2][lines.length];
        vel = new int[2][lines.length];
        for (int i = 0; i < lines.length; i++) {
            int[] vals = Fn.findAll(lines[i], "-?\\d+")
                .mapToInt(m -> Integer.parseInt(m.group()))
                .toIntArray();
            pos[0][i] = vals[0]; pos[1][i] = vals[1];
            vel[0][i] = vals[2]; vel[1][i] = vals[3];
        }
    }

    @Override
    public Grid part1() {
        int t = part2();

        List<Point> points = new ArrayList<>();
        Rect bounds = Rect.empty();
        for (int i = 0; i < pos[0].length; i++) {
            Point p = new Point(pos[0][i] + vel[0][i]*t, pos[1][i] + vel[1][i]*t);
            points.add(p);
            bounds = bounds.or(p);
        }

        Grid grid = new Grid(bounds.width(), bounds.height(), ' ');
        for (Point p : points)
            grid.set(p.x - bounds.xMin, p.y - bounds.yMin, '#');
        return grid;
    }

    @Override
    public Integer part2() {
        double tApprox = minimizeTotalVariance();
        int tLower = (int) Math.floor(tApprox);
        int tUpper = (int) Math.ceil(tApprox);
        if (tLower == tUpper) return tLower;
        return totalVariance(tLower) < totalVariance(tUpper) ? tLower : tUpper;
    }

    private double totalVariance(int t) {
        double total = 0.0;
        for (int d = 0; d < 2; d++)
            // var(p + t v) = var(p) + 2 t cov(p,v) + t^2 var(v)
            total += variance(pos[d]) + 2*t*covariance(pos[d], vel[d]) + t*t*variance(vel[d]);
        return total;
    }

    private double minimizeTotalVariance() {
        double num = 0.0;
        double den = 0.0;
        for (int d = 0; d < 2; d++) {
            // var(p+t v)' = 2 cov(p,v) + 2 t var(v)
            num += covariance(pos[d], vel[d]);
            den += variance(vel[d]);
        }
        return -num / den;
    }

    private static double variance(int[] xs) {
        double xxBar = 0.0;
        double xBar = 0.0;
        for (int i = 0; i < xs.length; i++) {
            xxBar += (xs[i]*xs[i] - xxBar) / (i + 1);
            xBar += (xs[i] - xBar) / (i + 1);
        }
        return xxBar - xBar*xBar;
    }

    private static double covariance(int[] xs, int[] ys) {
        double xyBar = 0.0;
        double xBar = 0.0;
        double yBar = 0.0;
        for (int i = 0; i < xs.length; i++) {
            xyBar += (xs[i]*ys[i] - xyBar) / (i + 1);
            xBar += (xs[i] - xBar) / (i + 1);
            yBar += (ys[i] - yBar) / (i + 1);
        }
        return xyBar - xBar*yBar;
    }
}

