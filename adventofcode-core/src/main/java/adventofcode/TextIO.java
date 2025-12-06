package adventofcode;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TextIO {
    public static String read(URL url, Charset encoding) throws IOException {
        try (InputStream is = url.openStream()) {
            return read(is, encoding);
        }
    }

    public static String read(File file, Charset encoding) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            return read(is, encoding);
        }
    }

    public static String read(InputStream is, Charset encoding) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, encoding));
        List<String> lines = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            lines.add(line);
            line = reader.readLine();
        }
        return String.join("\n", lines);
    }

    public static void write(File file, String text, Charset encoding) throws IOException {
        try (FileOutputStream os = new FileOutputStream(file)) {
            write(os, text, encoding);
        }
    }

    public static void write(OutputStream os, String text, Charset encoding) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8));
        for (String line : text.split("\n")) {
            writer.write(line);
            writer.newLine();
        }
    }
}
