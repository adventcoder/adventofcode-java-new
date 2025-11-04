package adventofcode.utils.collect;

import java.util.HashMap;
import java.util.function.Function;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultHashMap<K, V> extends HashMap<K, V> implements DefaultMap<K, V> {
    private final Function<? super K, ? extends V> defaultFunction;

    @Override
    public V getOrCompute(K k) {
        return computeIfAbsent(k, defaultFunction);
    }
}
