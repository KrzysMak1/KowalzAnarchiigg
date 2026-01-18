package cc.dreamcode.kowal.libs.net.kyori.adventure.translation;

import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.properties.AdventureProperties;
import java.util.Locale;
import java.util.function.Supplier;

final class TranslationLocales
{
    private static final Supplier<Locale> GLOBAL;
    
    private TranslationLocales() {
    }
    
    static Locale global() {
        return (Locale)TranslationLocales.GLOBAL.get();
    }
    
    static {
        final String property = AdventureProperties.DEFAULT_TRANSLATION_LOCALE.value();
        if (property == null || property.isEmpty()) {
            GLOBAL = (() -> Locale.US);
        }
        else if (property.equals((Object)"system")) {
            GLOBAL = Locale::getDefault;
        }
        else {
            final Locale locale = Translator.parseLocale(property);
            GLOBAL = (() -> locale);
        }
    }
}
