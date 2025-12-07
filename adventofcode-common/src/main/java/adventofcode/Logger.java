package adventofcode;

import adventofcode.utils.ObjectsEx;

public interface Logger {
    void debug(Object... args);

    static String format(String prefix, Object... args) {
        StringBuilder msg = new StringBuilder(prefix);
        for (int i = 0; i < args.length; i++) {
            msg.append(" ");
            msg.append(ObjectsEx.deepToString(args[i]));
        }
        return msg.toString();
    }
}
