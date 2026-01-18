package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.Function;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslationArgument;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TranslatableComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import java.util.Collections;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class TranslatableFallbackTag
{
    private static final String TR_OR = "tr_or";
    private static final String TRANSLATE_OR = "translate_or";
    private static final String LANG_OR = "lang_or";
    static final TagResolver RESOLVER;
    
    private TranslatableFallbackTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String key = args.popOr("A translation key is required").value();
        final String fallback = args.popOr("A fallback messages is required").value();
        List<Component> with;
        if (args.hasNext()) {
            with = (List<Component>)new ArrayList();
            while (args.hasNext()) {
                with.add((Object)ctx.deserialize(args.pop().value()));
            }
        }
        else {
            with = (List<Component>)Collections.emptyList();
        }
        return Tag.inserting(Component.translatable(key, fallback, with, new StyleBuilderApplicable[0]));
    }
    
    @Nullable
    static Emitable claim(final Component input) {
        if (!(input instanceof TranslatableComponent) || ((TranslatableComponent)input).fallback() == null) {
            return null;
        }
        final TranslatableComponent tr = (TranslatableComponent)input;
        return emit -> {
            emit.tag("lang_or");
            emit.argument(tr.key());
            emit.argument(tr.fallback());
            tr.arguments().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final TranslationArgument with = (TranslationArgument)iterator.next();
                emit.argument(with.asComponent());
            }
        };
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("lang_or", "translate_or", "tr_or"), (BiFunction<ArgumentQueue, Context, Tag>)TranslatableFallbackTag::create, (Function<Component, Emitable>)TranslatableFallbackTag::claim);
    }
}
