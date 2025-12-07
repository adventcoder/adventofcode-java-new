package adventofcode.utils.iter;

import java.util.concurrent.SynchronousQueue;
import java.util.function.Function;

import lombok.SneakyThrows;

public class Fiber<I, O> {
    private final SynchronousQueue<I> in = new SynchronousQueue<>();
    private final SynchronousQueue<O> out = new SynchronousQueue<>();
    private final Thread thread;
 
    public Fiber(Function<? super Fiber<I, O>, ? extends Function<I, O>> generator) {
        Function<I, O> func = generator.apply(this);
        thread = new Thread(() -> {
            try {
                out.put(func.apply(in.take()));
            } catch (InterruptedException ignored) {
            }
        });
        thread.start();
    }

    @SneakyThrows
    public I yield(O val) {
        out.put(val);
        return in.take();
    }

    @SneakyThrows
    public O resume(I val) {
        in.put(val);
        return out.take();
    }

    public static void main(String[] args) {
        Fiber<Integer, String> fiber = new Fiber<>(self -> a -> {
            int b = self.yield("Thanks for " + a);
            int c = self.yield("And thanks for " + b);
            return "Done and thanks for " + c;
        });
        String response1 = fiber.resume(1);
        System.out.println(response1);
        String response2 = fiber.resume(2);
        System.out.println(response2);
        String response3 = fiber.resume(3);
        System.out.println(response3);
    }
}
