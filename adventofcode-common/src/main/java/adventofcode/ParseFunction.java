package adventofcode;

@FunctionalInterface
public interface ParseFunction {
    void call(String input) throws Exception;
}
