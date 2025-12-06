package adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Objects;

public class Client {
    public String getInput(int year, int day, String session) throws IOException {
        URI uri = URI.create(String.format("https://adventofcode.com/%d/day/%d/input", year, day));

        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        if (session != null)
            conn.setRequestProperty("Cookie", "session=" + session);
        conn.setInstanceFollowRedirects(false);

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(appendErrorResponse("Failed to download input for year " + year + " day " + day, conn));
        }

        Charset charset = Objects.requireNonNullElse(getResponseCharset(conn), StandardCharsets.UTF_8);
        try (InputStream in = conn.getInputStream()) {
            return TextIO.read(in, charset);
        }
    }

    private Charset getResponseCharset(HttpURLConnection conn) throws IOException {
        String contentType = conn.getContentType();
        if (contentType == null) return null;
        String[] parts = contentType.split(";");
        for (String part : parts) {
            if (part.trim().toLowerCase().startsWith("charset=")) {
                String csName = part.trim().substring("charset=".length());
                try {
                    return Charset.forName(csName);
                } catch (UnsupportedCharsetException e) {
                    throw new UnsupportedEncodingException(csName);
                }
            }
        }
        return null;
    }

    private String appendErrorResponse(String message, HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(message).append(": HTTP ").append(conn.getResponseCode());
        try (InputStream err = conn.getErrorStream()) {
            if (err != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(err, StandardCharsets.UTF_8));
                for (String line; (line = reader.readLine()) != null; )
                    sb.append("\n").append(line);
            }
        }
        return sb.toString();
    }
}