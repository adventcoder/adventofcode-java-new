package adventofcode.utils.collect;

import java.util.HashMap;
import java.util.function.Function;

public class ReentrantHashMap<K, V> extends HashMap<K, V> {
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> func) {
        V val = get(key);
        if (val == null && !containsKey(key)) {
            val = func.apply(key);
            put(key, val);
        }
        return val;
    }
}
