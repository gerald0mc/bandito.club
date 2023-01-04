package club.bandito.client.util;

public class Timer {
    private long lastMs;

    public Timer() {
        lastMs = System.currentTimeMillis();
    }

    public boolean passed(long ms) {
        return System.currentTimeMillis() - lastMs > ms;
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public long getLastMs() {
        return lastMs;
    }
}
