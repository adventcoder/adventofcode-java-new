package adventofcode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Inputs {
    public static String read(Class<?> root, String path) throws IOException {
        try (InputStream is = root.getResourceAsStream(path)) {
            if (is == null)
                throw new FileNotFoundException(path);
            return read(is);
        }
    }

    public static String read(File file) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            return read(is);
        }
    }

    public static String read(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        List<String> lines = new ArrayList<>();
        for (String line; (line = reader.readLine()) != null; )
            lines.add(line);
        return String.join("\n", lines);
    }
}
