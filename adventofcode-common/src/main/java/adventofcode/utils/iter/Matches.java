package adventofcode.utils.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Matches implements Iterable<String> {
    private final Pattern pattern;
    private final String text;
    private final int groupIndex;

    public Matches(String regex, String text, int groupIndex) {
        this(Pattern.compile(regex), text, groupIndex);
    }

    public Matches(Pattern pattern, String text) {
        this(pattern, text, 0);
    }

    public Matches(String regex, String text) {
        this(regex, text, 0);
    }

    @Override
    public Iterator<String> iterator() {
        Matcher matcher = pattern.matcher(text);
        return new Iterator<>() {
            private Boolean hasNext;

            @Override
            public boolean hasNext() {
                if (hasNext == null)
                    hasNext = matcher.find();
                return hasNext;
            }

            @Override
            public String next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                hasNext = null;
                return matcher.group(groupIndex);
            }
        };
    }
}
