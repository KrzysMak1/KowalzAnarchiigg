package cc.dreamcode.kowal.libs.cc.dreamcode.utilities;

import lombok.Generated;
import java.util.regex.Matcher;
import java.math.BigInteger;
import java.util.Locale;
import java.time.Duration;
import java.util.Optional;
import lombok.NonNull;
import java.util.regex.Pattern;

public final class ParseUtil
{
    private static final Pattern SIMPLE_DURATION_PATTERN;
    private static final Pattern JBOD_FULL_DURATION_PATTERN;
    
    public static Optional<Integer> parseInteger(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final int i = Integer.parseInt(arg);
            return (Optional<Integer>)Optional.of((Object)i);
        }
        catch (final NumberFormatException e) {
            return (Optional<Integer>)Optional.empty();
        }
    }
    
    public static Optional<Long> parseLong(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final long l = Long.parseLong(arg);
            return (Optional<Long>)Optional.of((Object)l);
        }
        catch (final NumberFormatException e) {
            return (Optional<Long>)Optional.empty();
        }
    }
    
    public static Optional<Short> parseShort(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final short s = Short.parseShort(arg);
            return (Optional<Short>)Optional.of((Object)s);
        }
        catch (final NumberFormatException e) {
            return (Optional<Short>)Optional.empty();
        }
    }
    
    public static Optional<Byte> parseByte(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final byte b = Byte.parseByte(arg);
            return (Optional<Byte>)Optional.of((Object)b);
        }
        catch (final NumberFormatException e) {
            return (Optional<Byte>)Optional.empty();
        }
    }
    
    public static Optional<Double> parseDouble(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final double d = Double.parseDouble(arg);
            return (Optional<Double>)Optional.of((Object)d);
        }
        catch (final NumberFormatException e) {
            return (Optional<Double>)Optional.empty();
        }
    }
    
    public static Optional<Float> parseFloat(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final float f = Float.parseFloat(arg);
            return (Optional<Float>)Optional.of((Object)f);
        }
        catch (final NumberFormatException e) {
            return (Optional<Float>)Optional.empty();
        }
    }
    
    public static boolean parseBoolean(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        return Boolean.parseBoolean(arg);
    }
    
    public static Optional<Character> parseChar(@NonNull final String arg) {
        if (arg == null) {
            throw new NullPointerException("arg is marked non-null but is null");
        }
        try {
            final char c = arg.charAt(0);
            return (Optional<Character>)Optional.of((Object)c);
        }
        catch (final IndexOutOfBoundsException e) {
            return (Optional<Character>)Optional.empty();
        }
    }
    
    public static Optional<Duration> parsePeriod(@NonNull final String period) {
        if (period == null) {
            throw new NullPointerException("period is marked non-null but is null");
        }
        return readJbodPattern(period);
    }
    
    private static Duration timeToDuration(final long longValue, final String unit) {
        int n = -1;
        switch (unit.hashCode()) {
            case 100: {
                if (unit.equals((Object)"d")) {
                    n = 0;
                    break;
                }
                break;
            }
            case 104: {
                if (unit.equals((Object)"h")) {
                    n = 1;
                    break;
                }
                break;
            }
            case 109: {
                if (unit.equals((Object)"m")) {
                    n = 2;
                    break;
                }
                break;
            }
            case 115: {
                if (unit.equals((Object)"s")) {
                    n = 3;
                    break;
                }
                break;
            }
            case 3494: {
                if (unit.equals((Object)"ms")) {
                    n = 4;
                    break;
                }
                break;
            }
            case 3525: {
                if (unit.equals((Object)"ns")) {
                    n = 5;
                    break;
                }
                break;
            }
        }
        switch (n) {
            case 0: {
                return Duration.ofDays(longValue);
            }
            case 1: {
                return Duration.ofHours(longValue);
            }
            case 2: {
                return Duration.ofMinutes(longValue);
            }
            case 3: {
                return Duration.ofSeconds(longValue);
            }
            case 4: {
                return Duration.ofMillis(longValue);
            }
            case 5: {
                return Duration.ofNanos(longValue);
            }
            default: {
                throw new IllegalArgumentException("Really, this one should not be possible: " + unit);
            }
        }
    }
    
    private static Optional<Duration> readJbodPattern(String text) {
        text = text.toLowerCase(Locale.ROOT);
        text = text.replace((CharSequence)" ", (CharSequence)"");
        final Matcher fullMatcher = ParseUtil.JBOD_FULL_DURATION_PATTERN.matcher((CharSequence)text);
        if (!fullMatcher.matches()) {
            return (Optional<Duration>)Optional.empty();
        }
        final Matcher matcher = ParseUtil.SIMPLE_DURATION_PATTERN.matcher((CharSequence)text);
        boolean matched = false;
        BigInteger currentValue = BigInteger.valueOf(0L);
        while (matcher.find()) {
            matched = true;
            final long longValue = Long.parseLong(matcher.group("value"));
            final String unit = matcher.group("unit");
            currentValue = currentValue.add(BigInteger.valueOf(timeToDuration(longValue, unit).toNanos()));
        }
        return (Optional<Duration>)(matched ? Optional.of((Object)Duration.ofNanos(currentValue.longValueExact())) : Optional.empty());
    }
    
    @Generated
    private ParseUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        SIMPLE_DURATION_PATTERN = Pattern.compile("(?<value>-?[0-9]+)(?<unit>ms|ns|d|h|m|s)");
        JBOD_FULL_DURATION_PATTERN = Pattern.compile("((-?[0-9]+)(ms|ns|d|h|m|s))+");
    }
}
