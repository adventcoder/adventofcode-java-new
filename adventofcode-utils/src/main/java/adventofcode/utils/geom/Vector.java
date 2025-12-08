package adventofcode.utils.geom;

import it.unimi.dsi.fastutil.ints.IntImmutableList;

//TODO: maybe just make a mutable IntMatrix and IntVector class that includes linalg ops
public class Vector extends IntImmutableList {
    public Vector(int... vals) {
        super(vals);
    }

    public int x() {
        return getInt(0);
    }

    public int y() {
        return getInt(1);
    }

    public int z() {
        return getInt(2);
    }

    public boolean isZero() {
        for (int i = 0; i < size(); i++)
            if (getInt(i) != 0) return false;
        return true;
    }

    public int abs() {
        int total = 0;
        for (int i = 0; i < size(); i++)
            total += Math.abs(getInt(i));
        return total;
    }

    public int magnitudeSquared() {
        int total = 0;
        for (int i = 0; i < size(); i++)
            total += getInt(i)*getInt(i);
        return total;
    }

    public Vector add(Vector v) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] += v.getInt(i);
        return new Vector(vals);
    }

    public Vector addMul(Vector v, int n) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] += v.getInt(i)*n;
        return new Vector(vals);
    }

    public Vector subtract(Vector v) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] -= v.getInt(i);
        return new Vector(vals);
    }

    public Vector subtractMul(Vector v, int n) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] -= v.getInt(i)*n;
        return new Vector(vals);
    }

    public Vector multiply(int n) {
        int[] vals = new int[size()];
        for (int i = 0; i < vals.length; i++)
            vals[i] = getInt(i) * n;
        return new Vector(vals);
    }

    public int dot(Vector v) {
        int total = 0;
        int n = Math.min(size(), v.size());
        for (int i = 0; i < n; i++)
            total += getInt(i) * v.getInt(i);
        return total;
    }
}
