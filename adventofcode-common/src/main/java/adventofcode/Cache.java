package adventofcode;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Cache {
    private final Client client = new Client();

    public String getInput(int year, int day, String session) throws IOException {
        File file = getInputFile(year, day);
        if (file.exists()) {
            return TextIO.read(file, StandardCharsets.UTF_8);
        }

        String input = client.getInput(year, day, session);

        File parentFile = file.getParentFile();
        if (parentFile.exists() || parentFile.mkdirs()) {
            try {
                TextIO.write(file, input, StandardCharsets.UTF_8);
            } catch (IOException ioe) {
                if (file.exists() && !file.delete()) {
                    file.deleteOnExit();
                }
            }
        }

        return input;
    }

    public File getInputFile(int year, int day) {
        File file = new File("inputs");
        file = new File(file, Integer.toString(year));
        file = new File(file, day + ".txt");
        return file;
    }
}
