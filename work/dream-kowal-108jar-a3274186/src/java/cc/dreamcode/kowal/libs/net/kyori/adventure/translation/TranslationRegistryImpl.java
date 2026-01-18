package cc.dreamcode.kowal.libs.net.kyori.adventure.translation;

import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import java.text.MessageFormat;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Locale;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

final class TranslationRegistryImpl implements Examinable, TranslationRegistry
{
    private final Key name;
    private final Map<String, Translation> translations;
    private Locale defaultLocale;
    
    TranslationRegistryImpl(final Key name) {
        this.translations = (Map<String, Translation>)new ConcurrentHashMap();
        this.defaultLocale = Locale.US;
        this.name = name;
    }
    
    @Override
    public void register(@NotNull final String key, @NotNull final Locale locale, @NotNull final MessageFormat format) {
        ((Translation)this.translations.computeIfAbsent((Object)key, x$0 -> new Translation(x$0))).register(locale, format);
    }
    
    @Override
    public void unregister(@NotNull final String key) {
        this.translations.remove((Object)key);
    }
    
    @NotNull
    @Override
    public Key name() {
        return this.name;
    }
    
    @Override
    public boolean contains(@NotNull final String key) {
        return this.translations.containsKey((Object)key);
    }
    
    @NotNull
    @Override
    public TriState hasAnyTranslations() {
        if (!this.translations.isEmpty()) {
            return TriState.TRUE;
        }
        return TriState.FALSE;
    }
    
    @Nullable
    @Override
    public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
        final Translation translation = (Translation)this.translations.get((Object)key);
        if (translation == null) {
            return null;
        }
        return translation.translate(locale);
    }
    
    @Override
    public void defaultLocale(@NotNull final Locale defaultLocale) {
        this.defaultLocale = (Locale)Objects.requireNonNull((Object)defaultLocale, "defaultLocale");
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("translations", this.translations));
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TranslationRegistryImpl)) {
            return false;
        }
        final TranslationRegistryImpl that = (TranslationRegistryImpl)other;
        return this.name.equals(that.name) && this.translations.equals((Object)that.translations) && this.defaultLocale.equals((Object)that.defaultLocale);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.name, this.translations, this.defaultLocale });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    final class Translation implements Examinable
    {
        private final String key;
        private final Map<Locale, MessageFormat> formats;
        
        Translation(final String key) {
            this.key = (String)Objects.requireNonNull((Object)key, "translation key");
            this.formats = (Map<Locale, MessageFormat>)new ConcurrentHashMap();
        }
        
        void register(@NotNull final Locale locale, @NotNull final MessageFormat format) {
            if (this.formats.putIfAbsent((Object)Objects.requireNonNull((Object)locale, "locale"), (Object)Objects.requireNonNull((Object)format, "message format")) != null) {
                throw new IllegalArgumentException(String.format("Translation already exists: %s for %s", new Object[] { this.key, locale }));
            }
        }
        
        @Nullable
        MessageFormat translate(@NotNull final Locale locale) {
            MessageFormat format = (MessageFormat)this.formats.get(Objects.requireNonNull((Object)locale, "locale"));
            if (format == null) {
                format = (MessageFormat)this.formats.get((Object)new Locale(locale.getLanguage()));
                if (format == null) {
                    format = (MessageFormat)this.formats.get((Object)TranslationRegistryImpl.this.defaultLocale);
                    if (format == null) {
                        format = (MessageFormat)this.formats.get((Object)TranslationLocales.global());
                    }
                }
            }
            return format;
        }
        
        @NotNull
        @Override
        public Stream<? extends ExaminableProperty> examinableProperties() {
            return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("key", this.key), ExaminableProperty.of("formats", this.formats) });
        }
        
        @Override
        public boolean equals(final Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Translation)) {
                return false;
            }
            final Translation that = (Translation)other;
            return this.key.equals((Object)that.key) && this.formats.equals((Object)that.formats);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(new Object[] { this.key, this.formats });
        }
        
        @Override
        public String toString() {
            return Internals.toString(this);
        }
    }
}
