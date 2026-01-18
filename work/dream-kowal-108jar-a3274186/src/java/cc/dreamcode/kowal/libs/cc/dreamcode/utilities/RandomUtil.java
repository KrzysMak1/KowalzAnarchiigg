package cc.dreamcode.kowal.libs.cc.dreamcode.utilities;

import lombok.Generated;
import java.util.Random;

public final class RandomUtil
{
    public static int nextInteger(final int min, final int max) {
        if (max < min) {
            throw new IllegalArgumentException("Max value cannot be smaller than min.");
        }
        return new Random().ints(min, max).findAny().orElse(min);
    }
    
    public static int nextInteger(final int max) {
        return nextInteger(0, max);
    }
    
    public static long nextLong(final long min, final long max) {
        if (max < min) {
            throw new IllegalArgumentException("Max value cannot be smaller than min.");
        }
        return new Random().longs(min, max).findAny().orElse(min);
    }
    
    public static long nextLong(final long max) {
        if (max < 0L) {
            throw new IllegalArgumentException("Max value cannot be smaller than 0.");
        }
        return nextLong(0L, max);
    }
    
    public static double nextDouble(final double min, final double max) {
        if (max < min) {
            throw new IllegalArgumentException("Max value cannot be smaller than min.");
        }
        return new Random().doubles(min, max).findAny().orElse(min);
    }
    
    public static double nextDouble(final double max) {
        if (max < 0.0) {
            throw new IllegalArgumentException("Max value cannot be smaller than 0.");
        }
        return nextDouble(0.0, max);
    }
    
    public static boolean nextBoolean() {
        return nextInteger(1) >= 1;
    }
    
    public static boolean chance(final double percent) {
        return percent >= 100.0 || percent >= nextDouble(0.0, 100.0);
    }
    
    public static String alphabetic(final int length) {
        final Random random = new Random();
        final int leftLimit = 97;
        final int rightLimit = 122;
        return ((StringBuilder)random.ints(97, 123).limit((long)length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)).toString();
    }
    
    public static String alphanumeric(final int length) {
        final Random random = new Random();
        final int leftLimit = 48;
        final int rightLimit = 122;
        return ((StringBuilder)random.ints(48, 123).filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit((long)length).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)).toString();
    }
    
    @Generated
    private RandomUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
