package adventofcode.utils.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;
import java.util.function.Supplier;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Pipe<T> {
    @FunctionalInterface
    public static interface SupplierProc<T> {
        void run(Predicate<? super T> out);
    }

    @FunctionalInterface
    public static interface ConsumerProc<T> {
        void run(Supplier<? extends T> in);
    }

    private final BlockingQueue<T> queue;
    private final In in = new In();
    private final Out out = new Out();

    public Pipe(int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    public static <T> Pipe<T>.In openSupplier(SupplierProc<T> proc) {
        return openSupplier(Integer.MAX_VALUE, proc);
    }

    public static <T> Pipe<T>.In openSupplier(int capacity, SupplierProc<T> proc) {
        Pipe<T> pipe = new Pipe<>(capacity);
        pipe.in.thread = Thread.currentThread();
        pipe.out.thread = new Thread(() -> {
            try {
                proc.run(pipe.out);
            } finally {
                pipe.out.closeFromChild();
            }
        });
        pipe.out.thread.start();
        return pipe.in;
    }

    public static <T> Pipe<T>.Out openConsumer(ConsumerProc<T> proc) {
        return openConsumer(Integer.MAX_VALUE, proc);
    }

    public static <T> Pipe<T>.Out openConsumer(int capacity, ConsumerProc<T> proc) {
        Pipe<T> pipe = new Pipe<>(capacity);
        pipe.out.thread = Thread.currentThread();
        pipe.in.thread = new Thread(() -> {
            try {
                proc.run(pipe.in);
            } finally {
                pipe.in.closeFromChild();
            }
        });
        pipe.in.thread.start();
        return pipe.out;
    }

    public class Out implements Predicate<T>, AutoCloseable {
        private volatile boolean closed = false;
        private Thread thread;

        private void closeFromChild() {
            closed = true;
        }

        public void close() {
            closeFromChild();
            in.thread.interrupt();
            try {
                in.thread.join();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }

        // Send a non-null value to the consumer.
        // Waits for the producer to process some elements if there is insufficient capacity on the queue, 
        //
        // @return false if the consumer is closed and no more elements can be sent. The producer should abort as soon as poss
        //
        // @throws IllegalStateException this is closed.
        // @throws UncheckedInterruptedException interrutped while waiting for the consumer.
        //
        public boolean test(T val) {
            if (closed)
                throw new IllegalStateException("closed");
            if (in.closed)
                return false;
            try {
                queue.put(val);
                return true;
            } catch (InterruptedException e) {
                if (in.closed)
                    return false;
                throw new UncheckedInterruptedException();
            }
        }
    }

    public class In implements Supplier<T>, AutoCloseable {
        private volatile boolean closed = false;
        private Thread thread;

        private void closeFromChild() {
            closed = true;
        }

        @Override
        public void close() {
            closeFromChild();
            out.thread.interrupt();
            try {
                out.thread.join();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }

        // Get a non-null from the producer, or null if all values are consumed.
        // Waits for the producer to produce some elements if there are none available. 
        //
        // @return false if the producer is closed and there are no more elements.
        //
        // @throws IllegalStateException this is closed.
        // @throws UncheckedInterruptedException interrutped while waiting for the producer.
        //
        @Override
        public T get() {
            if (closed)
                throw new IllegalStateException("closed");
            if (out.closed && queue.isEmpty())
                return null;
            try {
                return queue.take();
            } catch (InterruptedException e) {
                if (out.closed)
                    return queue.poll();
                throw new UncheckedInterruptedException();
            }
        }
    }
}
