package util;

public class TimeInterval {
    public static void sleep(long deltaTime) {
        long preTime = System.currentTimeMillis();
        while (true) {
            if (System.currentTimeMillis() - preTime >= deltaTime) {
                return;
            }
        }
    }
}
