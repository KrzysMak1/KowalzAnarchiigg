package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.duration;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.util.regex.Matcher;
import java.math.BigInteger;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import java.time.Duration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class DurationTransformer extends BidirectionalTransformer<String, Duration>
{
    private static final Pattern SIMPLE_ISO_DURATION_PATTERN;
    private static final Pattern SUBSEC_ISO_DURATION_PATTERN;
    private static final Pattern SIMPLE_DURATION_PATTERN;
    private static final Pattern JBOD_FULL_DURATION_PATTERN;
    
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
    
    @Override
    public GenericsPair<String, Duration> getPair() {
        return this.genericsPair(String.class, Duration.class);
    }
    
    @Override
    public Duration leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        final Optional<Duration> jbodResult = readJbodPattern(data);
        if (jbodResult.isPresent()) {
            return (Duration)jbodResult.get();
        }
        if (data.matches("-?\\d+")) {
            final long longValue = Long.parseLong(data);
            final TemporalUnit unit = (TemporalUnit)serdesContext.getAttachment(DurationSpecData.class).map(DurationSpecData::getFallbackUnit).orElse((Object)ChronoUnit.SECONDS);
            return Duration.of(longValue, unit);
        }
        return Duration.parse((CharSequence)data);
    }
    
    @Override
    public String rightToLeft(@NonNull final Duration data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        final DurationFormat durationFormat = (DurationFormat)serdesContext.getAttachment(DurationSpecData.class).map(DurationSpecData::getFormat).orElse((Object)DurationFormat.SIMPLIFIED);
        final TemporalUnit fallbackUnit = (TemporalUnit)serdesContext.getAttachment(DurationSpecData.class).map(DurationSpecData::getFallbackUnit).orElse((Object)ChronoUnit.SECONDS);
        if (durationFormat == DurationFormat.ISO) {
            return data.toString();
        }
        if (data.isZero()) {
            return "0";
        }
        final String stringDuration = data.toString();
        final Matcher matcher = DurationTransformer.SIMPLE_ISO_DURATION_PATTERN.matcher((CharSequence)stringDuration);
        if (matcher.matches()) {
            final long longValue = Long.parseLong(matcher.group("value"));
            final String unit = matcher.group("unit").toLowerCase(Locale.ROOT);
            if ("h".equals((Object)unit) && longValue % 24L == 0L && fallbackUnit != ChronoUnit.HOURS) {
                return ((longValue < 0L) ? "-" : "") + longValue / 24L + "d";
            }
            return ((longValue < 0L) ? "-" : "") + longValue + unit;
        }
        else {
            final Matcher subsecMatcher = DurationTransformer.SUBSEC_ISO_DURATION_PATTERN.matcher((CharSequence)stringDuration);
            if (!subsecMatcher.matches()) {
                final String simplified = stringDuration.substring(2).toLowerCase(Locale.ROOT);
                try {
                    if (data.equals((Object)this.leftToRight(simplified, serdesContext))) {
                        return simplified;
                    }
                }
                catch (final Exception ex) {}
                return stringDuration;
            }
            final double doubleValue = Double.parseDouble(subsecMatcher.group("value"));
            final boolean negative = doubleValue < 0.0;
            final double absoluteValue = Math.abs(doubleValue);
            final int msValue = (int)Math.round(absoluteValue * 1000.0);
            if (msValue > 0) {
                return (negative ? "-" : "") + msValue + "ms";
            }
            final int nsValue = (int)Math.ceil(absoluteValue * 1.0E9);
            return (negative ? "-" : "") + nsValue + "ns";
        }
    }
    
    static {
        SIMPLE_ISO_DURATION_PATTERN = Pattern.compile("PT(?<value>-?[0-9]+)(?<unit>H|M|S)");
        SUBSEC_ISO_DURATION_PATTERN = Pattern.compile("PT(?<value>-?[0-9]\\.[0-9]+)S?");
        SIMPLE_DURATION_PATTERN = Pattern.compile("(?<value>-?[0-9]+)(?<unit>ms|ns|d|h|m|s)");
        JBOD_FULL_DURATION_PATTERN = Pattern.compile("((-?[0-9]+)(ms|ns|d|h|m|s))+");
    }
}
