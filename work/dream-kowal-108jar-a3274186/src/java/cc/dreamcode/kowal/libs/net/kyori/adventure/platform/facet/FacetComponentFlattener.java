package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Locale;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.TranslationRegistry;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.Translator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.GlobalTranslator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.Consumer;
import java.util.function.BiConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import java.util.Collection;
import java.util.regex.Pattern;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class FacetComponentFlattener
{
    private static final Pattern LOCALIZATION_PATTERN;
    
    private FacetComponentFlattener() {
    }
    
    public static <V> ComponentFlattener get(final V instance, final Collection<? extends Translator<V>> candidates) {
        final Translator<V> translator = Facet.of(candidates, instance);
        final ComponentFlattener.Builder flattenerBuilder = ((Buildable<R, ComponentFlattener.Builder>)ComponentFlattener.basic()).toBuilder();
        flattenerBuilder.complexMapper(TranslatableComponent.class, (java.util.function.BiConsumer<TranslatableComponent, Consumer<Component>>)((translatable, consumer) -> {
            final String key = translatable.key();
            for (final cc.dreamcode.kowal.libs.net.kyori.adventure.translation.Translator registry : GlobalTranslator.translator().sources()) {
                if (registry instanceof TranslationRegistry && ((TranslationRegistry)registry).contains(key)) {
                    consumer.accept((Object)GlobalTranslator.render(translatable, Locale.getDefault()));
                    return;
                }
            }
            final String translated = (translator == null) ? key : translator.valueOrDefault(instance, key);
            final Matcher matcher = FacetComponentFlattener.LOCALIZATION_PATTERN.matcher((CharSequence)translated);
            final List<Component> args = translatable.args();
            int argPosition = 0;
            int lastIdx = 0;
            while (matcher.find()) {
                if (lastIdx < matcher.start()) {
                    consumer.accept((Object)Component.text(translated.substring(lastIdx, matcher.start())));
                }
                lastIdx = matcher.end();
                final String argIdx = matcher.group(1);
                if (argIdx != null) {
                    try {
                        final int idx = Integer.parseInt(argIdx) - 1;
                        if (idx >= args.size()) {
                            continue;
                        }
                        consumer.accept((Object)args.get(idx));
                    }
                    catch (final NumberFormatException ex) {}
                }
                else {
                    final int idx = argPosition++;
                    if (idx >= args.size()) {
                        continue;
                    }
                    consumer.accept((Object)args.get(idx));
                }
            }
            if (lastIdx < translated.length()) {
                consumer.accept((Object)Component.text(translated.substring(lastIdx)));
            }
        }));
        return flattenerBuilder.build();
    }
    
    static {
        LOCALIZATION_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?s");
    }
    
    public interface Translator<V> extends Facet<V>
    {
        @NotNull
        String valueOrDefault(@NotNull final V game, @NotNull final String key);
    }
}
