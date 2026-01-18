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
import java.util.Collections;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class TranslatableTag
{
    private static final String TR = "tr";
    private static final String TRANSLATE = "translate";
    private static final String LANG = "lang";
    static final TagResolver RESOLVER;
    
    private TranslatableTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String key = args.popOr("A translation key is required").value();
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
        return Tag.inserting(Component.translatable(key, with));
    }
    
    @Nullable
    static Emitable claim(final Component input) {
        if (!(input instanceof TranslatableComponent) || ((TranslatableComponent)input).fallback() != null) {
            return null;
        }
        final TranslatableComponent tr = (TranslatableComponent)input;
        return emit -> {
            emit.tag("lang");
            emit.argument(tr.key());
            tr.arguments().iterator();
            final Iterator iterator;
            while (iterator.hasNext()) {
                final TranslationArgument with = (TranslationArgument)iterator.next();
                emit.argument(with.asComponent());
            }
        };
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("lang", "translate", "tr"), (BiFunction<ArgumentQueue, Context, Tag>)TranslatableTag::create, (Function<Component, Emitable>)TranslatableTag::claim);
    }
}
