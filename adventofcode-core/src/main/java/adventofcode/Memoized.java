package adventofcode;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.matcher.ElementMatchers;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Memoized {

    public static class Interceptor {
        public static <T> Class<? extends T> subclass(Class<T> clazz) {
            return new ByteBuddy()
                .subclass(clazz)
                .method(ElementMatchers.isAnnotatedWith(Memoized.class))
                .intercept(MethodDelegation.to(new Memoized.Interceptor()))
                .make()
                .load(clazz.getClassLoader())
                .getLoaded();
        }

        private final Map<Method, Map<Object, Object>> methodMemos = new HashMap<>();

        @RuntimeType
        public Object intercept(@Origin Method method, @AllArguments Object[] args, @SuperCall Callable<Object> superCall) throws Exception {
            Map<Object, Object> memo = methodMemos.computeIfAbsent(method, k -> new HashMap<>());
            Object key = args.length == 1 ? args[0] : Arrays.asList(args);
            Object val = memo.get(key);
            if (val == null) {
                val = superCall.call();
                memo.put(key, val);
            }
            return val;
        }
    }
}
