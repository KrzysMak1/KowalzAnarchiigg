package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.Function;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.SelectorComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class SelectorTag
{
    private static final String SEL = "sel";
    private static final String SELECTOR = "selector";
    static final TagResolver RESOLVER;
    
    private SelectorTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String key = args.popOr("A selection key is required").value();
        ComponentLike separator = null;
        if (args.hasNext()) {
            separator = ctx.deserialize(args.pop().value());
        }
        return Tag.inserting(Component.selector(key, separator));
    }
    
    @Nullable
    static Emitable claim(final Component input) {
        if (!(input instanceof SelectorComponent)) {
            return null;
        }
        final SelectorComponent st = (SelectorComponent)input;
        return emit -> {
            emit.tag("sel");
            emit.argument(st.pattern());
            if (st.separator() != null) {
                emit.argument(st.separator());
            }
        };
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("sel", "selector"), (BiFunction<ArgumentQueue, Context, Tag>)SelectorTag::create, (Function<Component, Emitable>)SelectorTag::claim);
    }
}
