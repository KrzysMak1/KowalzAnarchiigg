package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit;

import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import lombok.Generated;
import java.util.regex.Matcher;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Comparator;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.CompiledMessage;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit.adventure.AdventureLegacy;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Locale;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.StringUtil;
import lombok.NonNull;
import net.md_5.bungee.api.ChatColor;
import java.awt.Color;
import java.util.Map;
import java.util.regex.Pattern;

public final class StringColorUtil
{
    private static final char COLOR_CHAR = '§';
    private static final char ALT_COLOR_CHAR = '&';
    private static final Pattern hexPattern;
    private static final Map<Color, ChatColor> COLORS;
    
    public static String legacyFixColor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return ChatColor.translateAlternateColorCodes('&', processRgb(text));
    }
    
    public static String legacyFixColor(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return legacyFixColor(StringUtil.replace(text, placeholders));
    }
    
    public static String legacyFixColor(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return legacyFixColor(StringUtil.replace(locale, text, placeholders));
    }
    
    public static List<String> legacyFixColor(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(StringColorUtil::legacyFixColor).collect(Collectors.toList());
    }
    
    public static List<String> legacyFixColor(@NonNull final List<String> stringList, @NonNull final Map<String, Object> placeholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(text -> legacyFixColor(text, placeholders)).collect(Collectors.toList());
    }
    
    public static List<String> legacyFixColor(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return (List<String>)Arrays.stream((Object[])strings).map(StringColorUtil::legacyFixColor).collect(Collectors.toList());
    }
    
    public static String fixColor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return AdventureLegacy.serialize(AdventureLegacy.deserialize(text));
    }
    
    public static String fixColor(@NonNull final String text, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final CompiledMessage compiledMessage = CompiledMessage.of(Locale.forLanguageTag("pl"), text);
        final PlaceholderContext placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage).with(placeholders);
        return AdventureLegacy.serialize(AdventureLegacy.deserialize(text, AdventureLegacy.getPlaceholderConfig(placeholderContext)));
    }
    
    public static String fixColor(@NonNull final String text, @NonNull final Locale locale, @NonNull final Map<String, Object> placeholders) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        if (locale == null) {
            throw new NullPointerException("locale is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, text);
        final PlaceholderContext placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage).with(placeholders);
        return AdventureLegacy.serialize(AdventureLegacy.deserialize(text, AdventureLegacy.getPlaceholderConfig(placeholderContext)));
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(StringColorUtil::fixColor).collect(Collectors.toList());
    }
    
    public static List<String> fixColor(@NonNull final List<String> stringList, @NonNull final Map<String, Object> placeholders) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        if (placeholders == null) {
            throw new NullPointerException("placeholders is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(text -> fixColor(text, placeholders)).collect(Collectors.toList());
    }
    
    public static List<String> fixColor(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return (List<String>)Arrays.stream((Object[])strings).map(StringColorUtil::fixColor).collect(Collectors.toList());
    }
    
    public static String breakColor(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        return text.replace((CharSequence)"§", (CharSequence)"&");
    }
    
    public static List<String> breakColor(@NonNull final List<String> stringList) {
        if (stringList == null) {
            throw new NullPointerException("stringList is marked non-null but is null");
        }
        return (List<String>)stringList.stream().map(StringColorUtil::breakColor).collect(Collectors.toList());
    }
    
    public static List<String> breakColor(@NonNull final String... strings) {
        if (strings == null) {
            throw new NullPointerException("strings is marked non-null but is null");
        }
        return (List<String>)Arrays.stream((Object[])strings).map(StringColorUtil::breakColor).collect(Collectors.toList());
    }
    
    private static ChatColor getClosestColor(@NonNull final Color color) {
        if (color == null) {
            throw new NullPointerException("color is marked non-null but is null");
        }
        return (ChatColor)StringColorUtil.COLORS.entrySet().stream().sorted(Comparator.comparing(entry -> Math.pow((double)(color.getRed() - ((Color)entry.getKey()).getRed()), 2.0) + Math.pow((double)(color.getRed() - ((Color)entry.getKey()).getRed()), 2.0) + Math.pow((double)(color.getRed() - ((Color)entry.getKey()).getRed()), 2.0))).map(Map.Entry::getValue).findAny().orElseThrow(() -> new RuntimeException("Cannot find closest rgb color to format chat-color. (" + (Object)color + ")"));
    }
    
    private static Color hexToRgb(@NonNull final String hex) {
        if (hex == null) {
            throw new NullPointerException("hex is marked non-null but is null");
        }
        return new Color(Integer.parseInt(hex.substring(2), 16));
    }
    
    private static String processRgb(@NonNull final String text) {
        if (text == null) {
            throw new NullPointerException("text is marked non-null but is null");
        }
        final Matcher matcher = StringColorUtil.hexPattern.matcher((CharSequence)text);
        final AtomicReference<String> atomicText = (AtomicReference<String>)new AtomicReference((Object)text);
        while (matcher.find()) {
            final String hex = matcher.group();
            final Color color = hexToRgb(hex);
            if (VersionUtil.isSupported(16)) {
                atomicText.set((Object)((String)atomicText.get()).replace((CharSequence)hex, (CharSequence)((Object)ChatColor.of(color) + "")));
            }
            else {
                atomicText.set((Object)((String)atomicText.get()).replace((CharSequence)hex, (CharSequence)((Object)getClosestColor(color) + "")));
            }
        }
        return (String)atomicText.get();
    }
    
    @Generated
    private StringColorUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    
    static {
        hexPattern = Pattern.compile("&#([0-9A-Fa-f]{6})");
        COLORS = new MapBuilder<Color, ChatColor>().put(new Color(0), ChatColor.getByChar('0')).put(new Color(170), ChatColor.getByChar('1')).put(new Color(43520), ChatColor.getByChar('2')).put(new Color(43690), ChatColor.getByChar('3')).put(new Color(11141120), ChatColor.getByChar('4')).put(new Color(11141290), ChatColor.getByChar('5')).put(new Color(16755200), ChatColor.getByChar('6')).put(new Color(11184810), ChatColor.getByChar('7')).put(new Color(5592405), ChatColor.getByChar('8')).put(new Color(5592575), ChatColor.getByChar('9')).put(new Color(5635925), ChatColor.getByChar('a')).put(new Color(5636095), ChatColor.getByChar('b')).put(new Color(16733525), ChatColor.getByChar('c')).put(new Color(16733695), ChatColor.getByChar('d')).put(new Color(16777045), ChatColor.getByChar('e')).put(new Color(16777215), ChatColor.getByChar('f')).build();
    }
}
