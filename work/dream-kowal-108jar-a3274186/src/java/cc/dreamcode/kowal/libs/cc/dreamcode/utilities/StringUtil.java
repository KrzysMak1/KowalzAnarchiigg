package cc.dreamcode.kowal.libs.cc.dreamcode.utilities;

import lombok.Generated;
import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.CompiledMessage;
import java.util.Locale;
import lombok.NonNull;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.Placeholders;

public final class StringUtil
{
    private static Placeholders placeholders;
    
    public static String join(final List<String> stringList) {
        return join((String[])stringList.toArray((Object[])new String[0]), "", 0, stringList.size());
    }
    
    public static String join(final List<String> stringList, final String separator) {
        return join((String[])stringList.toArray((Object[])new String[0]), separator, 0, stringList.size());
    }
    
    public static String join(final List<String> stringList, final String separator, final int from, final int to) {
        return join((String[])stringList.toArray((Object[])new String[0]), separator, from, to);
    }
    
    public static String join(final String[] array) {
        return join(array, "", 0, array.length);
    }
    
    public static String join(final String[] array, final String separator) {
        return join(array, separator, 0, array.length);
    }
    
    public static String join(final String[] array, final String separator, final int from, final int to) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = from; i < to; ++i) {
            if (i > from) {
                stringBuilder.append(separator);
            }
            if (array[i] != null) {
                stringBuilder.append(array[i]);
            }
        }
        return stringBuilder.toString();
    }
    
    public static String replace(@NonNull final String text, @NonNull final String from, @NonNull final Object to) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        return replace(Locale.forLanguageTag("pl"), text, from, to);
    }
    
    public static String replace(@NonNull final Locale locale, @NonNull final String text, @NonNull final String from, @NonNull final Object to) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, text);
        return StringUtil.placeholders.contextOf(compiledMessage).with(from, to).apply();
    }
    
    public static String replace(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return replace(Locale.forLanguageTag("pl"), text, placeholders);
    }
    
    public static String replace(@NonNull final Locale locale, @NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, text);
        return StringUtil.placeholders.contextOf(compiledMessage).with(placeholders).apply();
    }
    
    public static Placeholders getPlaceholders() {
        return StringUtil.placeholders;
    }
    
    public static void setPlaceholders(@NonNull final Placeholders placeholders) {
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        StringUtil.placeholders = placeholders;
    }
    
    @Generated
    private StringUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        StringUtil.placeholders = Placeholders.create(true);
    }
}
