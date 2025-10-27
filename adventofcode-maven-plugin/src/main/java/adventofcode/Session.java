package adventofcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Session {
    private final String token;

    public void downloadInput(int year, int day, OutputStream out) throws IOException {
        URL url = new URL(String.format("https://adventofcode.com/%d/day/%d/input", year, day));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Cookie", "session=" + token);
        conn.setInstanceFollowRedirects(false);

        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException(appendErrorResponse("Failed to download input for day " + day, conn));
        }

        try (InputStream in = conn.getInputStream()) {
            in.transferTo(out);
        }
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