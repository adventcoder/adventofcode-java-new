package adventofcode;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TimedResult<T> {
    T value;
    long time;
}
