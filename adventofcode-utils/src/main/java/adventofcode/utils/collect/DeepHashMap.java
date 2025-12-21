package adventofcode.utils.collect;

import adventofcode.utils.lang.ObjectsEx;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

public class DeepHashMap<K, V> extends Object2ObjectOpenCustomHashMap<K, V> {
    public DeepHashMap() {
        super(ObjectsEx.DEEP_HASH_STRATEGY);
    }
}
