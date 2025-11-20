package adventofcode.decorators;

import lombok.experimental.UtilityClass;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

@UtilityClass
public class Decorators {
    public static <T> Class<? extends T> decorate(Class<T> clazz) {
        return new ByteBuddy()
            .subclass(clazz)
            .method(ElementMatchers.isAnnotatedWith(Memoized.class))
            .intercept(MethodDelegation.to(new Memoized.Interceptor()))
            // add other interceptors here
            .make()
            .load(clazz.getClassLoader())
            .getLoaded();
    }

    public static <T> Class<? super T> undecorate(Class<T> clazz) {
        return clazz.getName().contains("ByteBuddy") ? clazz.getSuperclass() : clazz;
    }
}
