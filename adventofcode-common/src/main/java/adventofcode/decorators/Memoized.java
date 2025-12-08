package adventofcode.decorators;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Memoized {

    public static class Interceptor {
        private final Map<Method, Map<Object, Object>> memos = new HashMap<>();

        @RuntimeType
        public Object intercept(@Origin Method method, @AllArguments Object[] args, @SuperCall Callable<Object> superCall) throws Exception {
            Object key = args.length == 1 ? args[0] : Arrays.asList(args);
            return getValue(getMemo(method), key, superCall);
        }

        private Map<Object, Object> getMemo(Method method) {
            return memos.computeIfAbsent(method, k -> new HashMap<>());
        }

        private static Object getValue(Map<Object, Object> memo, Object key, Callable<Object> superCall) throws Exception {
            Object val = memo.get(key);
            if (val == null && !memo.containsKey(key)) {
                val = superCall.call();
                memo.put(key, val);
            }
            return val;
        }
    }
}
