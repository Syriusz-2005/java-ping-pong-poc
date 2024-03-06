package Utils;

import java.util.function.Predicate;
import java.util.random.RandomGeneratorFactory;

public class RandomCode {
    private static int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    public static String generateRandomCode(int length, Predicate<String> isColliding) {
        String chars = "abcdefghijklmnoprstuwxyz1234567890";
        var builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int charIndex = randomInt(0, chars.length());
            char c = chars.charAt(charIndex);
            builder.append(c);
        }
        String result = builder.toString();
        if (isColliding.test(result)) {
            return generateRandomCode(length, isColliding);
        }
        return result;
    }
}
