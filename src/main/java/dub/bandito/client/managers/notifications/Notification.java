package club.bandito.client.managers.notifications;

public class Notification {
    private final String message;
    private final long startTime;

    public Notification(String message) {
        this.message = message;
        this.startTime = System.currentTimeMillis();
    }

    public String getMessage() {
        return message;
    }

    public long getStartTime() {
        return startTime;
    }
}
