package adventofcode;

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

    public long resumeFor(ParseFunction fn, String input) throws Exception {
        long startTime = resume();
        fn.call(input);
        long endTime = pause();
        return endTime - startTime;
    }

    public PartResult resumeFor(PartFunction fn) throws Exception {
        long startTime = resume();
        Object answer = fn.call();
        long endTime = pause();
        return new PartResult(answer, endTime - startTime);
    }
}
