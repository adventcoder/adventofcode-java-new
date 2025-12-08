package adventofcode.utils.iter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class PipedSupplier<T> implements Supplier<T>, AutoCloseable {
    private final PipedConsumer out = new PipedConsumer();
    private final LinkedBlockingQueue<T> queue;
    private final Thread inThread;
    private final Thread outThread;
    private volatile boolean inClosed = false;
    private volatile boolean outClosed = false;

    public PipedSupplier(Consumer<? super Consumer<? super T>> proc) {
        this(proc, 1);
    }

    public PipedSupplier(Consumer<? super Consumer<? super T>> proc, int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
        inThread = Thread.currentThread();
        outThread = new Thread(() -> {
            try {
                proc.accept(out);
            } finally {
                out.close();
            }
        });
        outThread.start();
    }

    private class PipedConsumer implements Consumer<T> {
        public void close() {
            outClosed = true;
            inThread.interrupt();
        }

        public void accept(T val) {
            if (outClosed)
                throw new IllegalStateException("closed");
            try {
                queue.put(val);
            } catch (InterruptedException e) {
                if (!inClosed) throw new AssertionError("unexpected interrupt");
                throw new IllegalStateException("pipe dead");
            }
        }
    }

    @Override
    public void close() {
        inClosed = true;
        outThread.interrupt();
    }

    @Override
    public T get() {
        if (inClosed)
            throw new IllegalStateException("closed");
        try {
            return queue.take();
        } catch (InterruptedException e) {
            if (!outClosed) throw new AssertionError("unexpected interrupt");
            throw new IllegalStateException("pipe dead");
        }
    }
}
