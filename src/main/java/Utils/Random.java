package Utils;

public class Random {
    public static float random(float min, float max) {
        return (float) Math.random() * (max - min) + min;
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static float random(float[][] bounds) {
        int setIndex = random(0, bounds.length);
        float[] bound = bounds[setIndex];
        return random(bound[0], bound[1]);
    }
}
