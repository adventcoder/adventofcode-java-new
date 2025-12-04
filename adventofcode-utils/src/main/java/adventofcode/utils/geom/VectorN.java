package adventofcode.utils.geom;

import it.unimi.dsi.fastutil.ints.IntImmutableList;

//TODO: maybe just make a mutable IntMatrix and IntVector class that includes vector operations
public class VectorN extends IntImmutableList {
    public VectorN(int... vals) {
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

    public VectorN add(VectorN v) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] += v.getInt(i);
        return new VectorN(vals);
    }

    public VectorN addMul(VectorN v, int n) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] += v.getInt(i)*n;
        return new VectorN(vals);
    }

    public VectorN subtract(VectorN v) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] -= v.getInt(i);
        return new VectorN(vals);
    }

    public VectorN subtractMul(VectorN v, int n) {
        int[] vals = toArray(new int[Math.max(size(), v.size())]);
        for (int i = 0; i < v.size(); i++)
            vals[i] -= v.getInt(i)*n;
        return new VectorN(vals);
    }

    public VectorN multiply(int n) {
        int[] vals = new int[size()];
        for (int i = 0; i < vals.length; i++)
            vals[i] = getInt(i) * n;
        return new VectorN(vals);
    }

    public int dot(VectorN v) {
        int total = 0;
        int n = Math.min(size(), v.size());
        for (int i = 0; i < n; i++)
            total += getInt(i) * v.getInt(i);
        return total;
    }
}
