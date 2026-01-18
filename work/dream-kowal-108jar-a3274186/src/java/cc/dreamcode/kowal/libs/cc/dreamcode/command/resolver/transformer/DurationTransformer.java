package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer;

import java.util.regex.Matcher;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Optional;
import lombok.NonNull;
import java.util.regex.Pattern;
import java.time.Duration;

public class DurationTransformer implements ObjectTransformer<Duration>
{
    private static final Pattern SIMPLE_DURATION_PATTERN;
    private static final Pattern JBOD_FULL_DURATION_PATTERN;
    
    @Override
    public Class<?> getGeneric() {
        return Duration.class;
    }
    
    @Override
    public Optional<Duration> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return readJbodPattern(input);
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
        final Matcher fullMatcher = DurationTransformer.JBOD_FULL_DURATION_PATTERN.matcher((CharSequence)text);
        if (!fullMatcher.matches()) {
            return (Optional<Duration>)Optional.empty();
        }
        final Matcher matcher = DurationTransformer.SIMPLE_DURATION_PATTERN.matcher((CharSequence)text);
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
    
    static {
        SIMPLE_DURATION_PATTERN = Pattern.compile("(?<value>-?[0-9]+)(?<unit>ms|ns|d|h|m|s)");
        JBOD_FULL_DURATION_PATTERN = Pattern.compile("((-?[0-9]+)(ms|ns|d|h|m|s))+");
    }
}
