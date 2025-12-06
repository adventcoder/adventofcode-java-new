package adventofcode.utils.geom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Grid {
    private final char[] data;
    private final int start;
    private final int stride;
    public final int width;
    public final int height;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        data = new char[width * height];
        start = 0;
        stride = width;
    }

    public Grid(int width, int height, char c) {
        this(width, height);
        Arrays.fill(data, c);
    }

    public Grid(String... rows) {
        this(rows[0].length(), rows.length);
        for (int y = 0; y < rows.length; y++) {
            if (rows[y].length() != width)
                throw new IllegalArgumentException();
            setRow(y, rows[y]);
        }
    }

    public static Grid parse(String input) {
        return new Grid(input.split("\n"));
    }

    @Override
    public String toString() {
        List<String> rows = new ArrayList<>();
        for (int y = 0; y < height; y++)
            rows.add(getRow(y));
        return String.join("\n", rows);
    }

    public boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    private int index(int x, int y) {
        return start + x + stride * y;
    }

    public char get(int x, int y) {
        return data[index(x, y)];
    }

    public char getOrDefault(int x, int y, char c) {
        return inBounds(x, y) ? get(x, y) : c;
    }

    public void set(int x, int y, char c) {
        data[index(x, y)] = c;
    }

    public String getRow(int y) {
        return new String(data, start + y*stride, width);
    }

    public void setRow(int y, String row) {
        row.getChars(0, width, data, start + y*stride);
    }

    public Grid slice(int x, int y, int sliceWidth, int sliceHeight) {
        return new Grid(data, index(x, y), stride, sliceWidth, sliceHeight);
    }

    public void setSlice(int x, int y, Grid slice) {
        if (width == slice.width && stride == width && slice.stride == slice.width) {
            System.arraycopy(slice.data, slice.start, data, index(x, y), slice.width*slice.height);
        } else {
            for (int sliceY = 0; sliceY < slice.height; sliceY++)
                System.arraycopy(slice.data, slice.start + sliceY*slice.stride, data, index(x, y + sliceY), slice.width);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Grid other)) return false;
        if (other.height != height) return false;
        if (stride == width && other.stride == other.width) {
            return Arrays.equals(data, start, start + width*height, other.data, other.start, other.start + other.width*other.height);
        } else {
            for (int y = 0; y < height; y++) {
                int offset = start + y*stride;
                int otherOffset = other.start + y*other.stride;
                if (!Arrays.equals(data, offset, offset + width, other.data, otherOffset, otherOffset + other.width))
                    return false;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int y = 0; y < height; y++) {
            int offset = start + y*stride;
            for (int x = 0; x < width; x++)
                result = result*31 + data[offset + x];
        }
        return result;
    }

    public int count(char c) {
        int count = 0;
        for (int y = 0; y < height; y++) {
            int offset = start + y*stride;
            for (int x = 0; x < width; x++) {
                if (data[offset + x] == c) count++;
            }
        }
        return count;
    }
}
