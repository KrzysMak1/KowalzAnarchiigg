package cc.dreamcode.kowal.libs.net.kyori.adventure.translation;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.text.MessageFormat;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.Locale;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;

final class GlobalTranslatorImpl implements GlobalTranslator
{
    private static final Key NAME;
    static final GlobalTranslatorImpl INSTANCE;
    final TranslatableComponentRenderer<Locale> renderer;
    private final Set<Translator> sources;
    
    private GlobalTranslatorImpl() {
        this.renderer = TranslatableComponentRenderer.usingTranslationSource(this);
        this.sources = (Set<Translator>)Collections.newSetFromMap((Map)new ConcurrentHashMap());
    }
    
    @NotNull
    @Override
    public Key name() {
        return GlobalTranslatorImpl.NAME;
    }
    
    @NotNull
    @Override
    public Iterable<? extends Translator> sources() {
        return (Iterable<? extends Translator>)Collections.unmodifiableSet((Set)this.sources);
    }
    
    @Override
    public boolean addSource(@NotNull final Translator source) {
        Objects.requireNonNull((Object)source, "source");
        if (source == this) {
            throw new IllegalArgumentException("GlobalTranslationSource");
        }
        return this.sources.add((Object)source);
    }
    
    @Override
    public boolean removeSource(@NotNull final Translator source) {
        Objects.requireNonNull((Object)source, "source");
        return this.sources.remove((Object)source);
    }
    
    @NotNull
    @Override
    public TriState hasAnyTranslations() {
        if (!this.sources.isEmpty()) {
            return TriState.TRUE;
        }
        return TriState.FALSE;
    }
    
    @Nullable
    @Override
    public MessageFormat translate(@NotNull final String key, @NotNull final Locale locale) {
        Objects.requireNonNull((Object)key, "key");
        Objects.requireNonNull((Object)locale, "locale");
        for (final Translator source : this.sources) {
            final MessageFormat translation = source.translate(key, locale);
            if (translation != null) {
                return translation;
            }
        }
        return null;
    }
    
    @Nullable
    @Override
    public Component translate(@NotNull final TranslatableComponent component, @NotNull final Locale locale) {
        Objects.requireNonNull((Object)component, "component");
        Objects.requireNonNull((Object)locale, "locale");
        for (final Translator source : this.sources) {
            final Component translation = source.translate(component, locale);
            if (translation != null) {
                return translation;
            }
        }
        return null;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("sources", this.sources));
    }
    
    static {
        NAME = Key.key("adventure", "global");
        INSTANCE = new GlobalTranslatorImpl();
    }
}
