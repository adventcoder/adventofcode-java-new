package adventofcode.utils.collect;

import java.util.Map;

public interface DefaultMap<K, V> extends Map<K, V> {

    public V getOrCompute(K k);
}
