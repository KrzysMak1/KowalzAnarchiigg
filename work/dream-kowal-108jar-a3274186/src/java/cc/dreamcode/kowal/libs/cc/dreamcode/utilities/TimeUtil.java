package cc.dreamcode.kowal.libs.cc.dreamcode.utilities;

import lombok.Generated;
import lombok.NonNull;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public final class TimeUtil
{
    public static String format(final long mills) {
        final long days = TimeUnit.MILLISECONDS.toDays(mills);
        final long hours = TimeUnit.MILLISECONDS.toHours(mills) - days * 24L;
        final long minutes = TimeUnit.MILLISECONDS.toMinutes(mills) - days * 24L * 60L - hours * 60L;
        final long seconds = TimeUnit.MILLISECONDS.toSeconds(mills) - days * 24L * 60L * 60L - hours * 60L * 60L - minutes * 60L;
        long millisecondsFinal = mills - days * 24L * 60L * 60L * 1000L - hours * 60L * 60L * 1000L - minutes * 60L * 1000L - seconds * 1000L;
        if (millisecondsFinal == 1000L) {
            --millisecondsFinal;
        }
        if (days != 0L) {
            if (millisecondsFinal == 0L) {
                return days + "d " + hours + "h " + minutes + "min " + seconds + "s";
            }
            return days + "d " + hours + "h " + minutes + "min " + seconds + "s " + millisecondsFinal + "ms";
        }
        else if (hours != 0L) {
            if (millisecondsFinal == 0L) {
                return hours + "h " + minutes + "min " + seconds + "s";
            }
            return hours + "h " + minutes + "min " + seconds + "s " + millisecondsFinal + "ms";
        }
        else if (minutes != 0L) {
            if (millisecondsFinal == 0L) {
                return minutes + "min " + seconds + "s";
            }
            return minutes + "min " + seconds + "s " + millisecondsFinal + "ms";
        }
        else {
            if (seconds == 0L) {
                return millisecondsFinal + "ms";
            }
            if (millisecondsFinal == 0L) {
                return seconds + "s";
            }
            return seconds + "s " + millisecondsFinal + "ms";
        }
    }
    
    public static String format(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return format(duration.toMillis());
    }
    
    public static String formatSec(final long seconds) {
        final long days = TimeUnit.SECONDS.toDays(seconds);
        final long hours = TimeUnit.SECONDS.toHours(seconds) - days * 24L;
        final long minutes = TimeUnit.SECONDS.toMinutes(seconds) - days * 24L * 60L - hours * 60L;
        final long secs = seconds - days * 24L * 60L * 60L - hours * 60L * 60L - minutes * 60L;
        if (days != 0L) {
            return days + "d " + hours + "h " + minutes + "min " + secs + "s";
        }
        if (hours != 0L) {
            return hours + "h " + minutes + "min " + secs + "s";
        }
        if (minutes != 0L) {
            return minutes + "min " + secs + "s";
        }
        return secs + "s";
    }
    
    public static String formatSec(@NonNull final Duration duration) {
        if (duration == null) {
            throw new NullPointerException("duration is marked non-null but is null");
        }
        return formatSec(duration.getSeconds());
    }
    
    @Generated
    private TimeUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
