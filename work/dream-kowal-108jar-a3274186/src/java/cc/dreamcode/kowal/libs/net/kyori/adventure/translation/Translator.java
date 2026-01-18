package cc.dreamcode.kowal.libs.net.kyori.adventure.translation;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import java.text.MessageFormat;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public interface Translator
{
    @Nullable
    default Locale parseLocale(@NotNull final String string) {
        final String[] segments = string.split("_", 3);
        final int length = segments.length;
        if (length == 1) {
            return new Locale(string);
        }
        if (length == 2) {
            return new Locale(segments[0], segments[1]);
        }
        if (length == 3) {
            return new Locale(segments[0], segments[1], segments[2]);
        }
        return null;
    }
    
    @NotNull
    Key name();
    
    @NotNull
    default TriState hasAnyTranslations() {
        return TriState.NOT_SET;
    }
    
    @Nullable
    MessageFormat translate(@NotNull final String key, @NotNull final Locale locale);
    
    @Nullable
    default Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
        return null;
    }
}
