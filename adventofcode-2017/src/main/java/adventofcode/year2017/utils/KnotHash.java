package adventofcode.year2017.utils;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

public class KnotHash {
    private static final String suffix = new String(new byte[] { 17, 31, 73, 47, 23 });

    public static KnotHash standard(String input) {
        KnotHash hash = new KnotHash((input + suffix).getBytes(StandardCharsets.UTF_8));
        for (int i = 0; i < 64; i++)
            hash.round();
        return hash;
    }

    private final byte[] lengths;
    public final byte[] hash;
    private int pos = 0;
    private int skipSize = 0;

    public KnotHash(byte[] lengths) {
        this.lengths = lengths;
        this.hash = new byte[256];
        for (int i = 0; i < 256; i++)
            hash[i] = (byte) i;
    }

    public void round() {
        for (int i = 0; i < lengths.length; i++) {
            int length = Byte.toUnsignedInt(lengths[i]);
            reverse(length);
            pos = (pos + length + skipSize) % hash.length;
            skipSize++;
        }
    }

    private void reverse(int length) {
        for (int i = 0; i < length / 2; i++) {
            swap((pos + i) % hash.length, (pos + length - 1 - i) % hash.length);
        }
    }

    private void swap(int i, int j) {
        byte t = hash[i];
        hash[i] = hash[j];
        hash[j] = t;
    }

    public String toHexString() {
        return HexFormat.of().formatHex(toByteArray());
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[16];
        for (int i = 0; i < hash.length; i++)
            bytes[i >> 4] ^= hash[i];
        return bytes;
    }
}
