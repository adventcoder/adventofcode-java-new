package adventofcode.utils.collect;

import adventofcode.utils.ObjectsEx;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;

public class DeepHashSet<K> extends ObjectOpenCustomHashSet<K> {
    public DeepHashSet() {
        super(ObjectsEx.DEEP_HASH_STRATEGY);
    }
}
