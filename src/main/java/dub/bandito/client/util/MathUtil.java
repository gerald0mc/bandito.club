package club.bandito.client.util;

public class MathUtil {

    public static float normalize(float value, float min, float max) {
        return (value - min) / (max - min);
    }

    public static double lerp(double then, double now, float time) {
        return then + time * (now - then);
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}