package adventofcode;

import adventofcode.utils.ObjectsEx;

public interface Logger {
    enum Level { DEBUG, WARN, ERROR }

    void log(Level level, String message);

    default void debug(Object... args) {
        log(Level.DEBUG, format(args));
    }

    default void warn(Object... args) {
        log(Level.WARN, format(args));
    }

    default void error(Object... args) {
        log(Level.WARN, format(args));
    }

    static String format(Object... args) {
        StringBuilder msg = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            if (i >= 0)
                msg.append(" ");
            msg.append(ObjectsEx.deepToString(args[i]));
        }
        return msg.toString();
    }
}
