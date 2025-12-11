package adventofcode.utils.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Predicate;

import adventofcode.utils.iter.Generator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Pipe<T> {
    @FunctionalInterface
    public static interface ProducerProc<T> {
        void call(Predicate<? super T> out);
    }

    @FunctionalInterface
    public static interface ConsumerProc<T> {
        void call(Generator<? extends T> in);
    }

    private final BlockingQueue<T> queue;
    private final In in = new In();
    private final Out out = new Out();

    public Pipe(int capacity) {
        queue = new LinkedBlockingQueue<>(capacity);
    }

    public static <T> Pipe<T>.In openProducer(ProducerProc<T> proc) {
        return openProducer(Integer.MAX_VALUE, proc);
    }

    public static <T> Pipe<T>.In openProducer(int capacity, ProducerProc<T> proc) {
        Pipe<T> pipe = new Pipe<>(capacity);
        pipe.in.thread = Thread.currentThread();
        pipe.out.thread = new Thread(() -> {
            try {
                proc.call(pipe.out);
            } finally {
                pipe.out.closed = true;
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
                proc.call(pipe.in);
            } finally {
                pipe.in.closed = true;
            }
        });
        pipe.in.thread.start();
        return pipe.out;
    }

    public class Out implements Predicate<T>, AutoCloseable {
        private volatile boolean closed = false;
        private Thread thread;

        public void close() {
            closed = true;
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
        @Override
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

    public class In implements Generator<T>, AutoCloseable {
        private volatile boolean closed = false;
        private Thread thread;

        @Override
        public void close() {
            closed = true;
            out.thread.interrupt();
            try {
                out.thread.join();
            } catch (InterruptedException e) {
                throw new UncheckedInterruptedException();
            }
        }

        // Get the next non-null from the producer, or null if all values are consumed.
        // Waits for the producer to produce some elements if there are none available. 
        //
        // @return null if the producer is closed and there are no more elements.
        //
        // @throws IllegalStateException this is closed.
        // @throws UncheckedInterruptedException interrutped while waiting for the producer.
        //
        @Override
        public T next() {
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
