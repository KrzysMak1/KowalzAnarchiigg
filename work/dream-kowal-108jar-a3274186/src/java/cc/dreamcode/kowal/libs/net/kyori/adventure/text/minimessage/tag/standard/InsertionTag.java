package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import java.util.function.BiConsumer;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class InsertionTag
{
    private static final String INSERTION = "insert";
    static final TagResolver RESOLVER;
    
    private InsertionTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String insertion = args.popOr("A value is required to produce an insertion component").value();
        return Tag.styling((Consumer<Style.Builder>)(b -> b.insertion(insertion)));
    }
    
    static void emit(final String insertion, final TokenEmitter emitter) {
        emitter.tag("insert").argument(insertion);
    }
    
    static {
        RESOLVER = SerializableResolver.claimingStyle("insert", (BiFunction<ArgumentQueue, Context, Tag>)InsertionTag::create, StyleClaim.claim("insert", (java.util.function.Function<Style, Object>)Style::insertion, (java.util.function.BiConsumer<Object, TokenEmitter>)InsertionTag::emit));
    }
}
