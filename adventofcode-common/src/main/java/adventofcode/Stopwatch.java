package adventofcode;

import java.util.concurrent.Callable;

import it.unimi.dsi.fastutil.objects.ObjectLongPair;

public class Stopwatch {
    private long lastStartTime = -1;
    private long time = 0L; 

    public boolean running() {
        return lastStartTime >= 0;
    }

    public long time() {
        return time + (running() ? System.nanoTime() - lastStartTime : 0);
    }

    public void reset() {
        time = 0L;
        if (running())
            lastStartTime = System.nanoTime();
    }

    public long pause() {
        if (!running())
            throw new IllegalStateException("Timer not running");
        time += System.nanoTime() - lastStartTime;
        lastStartTime = -1;
        return time;
    }

    public long resume() {
        if (running())
            throw new IllegalStateException("Timer already running");
        lastStartTime = System.nanoTime();
        return time;
    }

    public void pauseFor(Runnable runnable) {
        if (running()) {
            pause();
            try {
                runnable.run();
            } finally {
                resume();
            }
        } else {
            runnable.run();
        }
    }

    public long resumeFor(Runnable runnable) {
        long startTime = resume();
        runnable.run();
        long endTime = pause();
        return endTime - startTime;
    }

    public <T> ObjectLongPair<T> resumeFor(Callable<T> callable) throws Exception {
        long startTime = resume();
        T result = callable.call();
        long endTime = pause();
        return ObjectLongPair.of(result, endTime - startTime);
    }
}
