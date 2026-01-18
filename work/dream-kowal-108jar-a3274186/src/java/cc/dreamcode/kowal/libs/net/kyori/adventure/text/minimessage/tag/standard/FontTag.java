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
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.InvalidKeyException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class FontTag
{
    static final String FONT = "font";
    static final TagResolver RESOLVER;
    
    private FontTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String valueOrNamespace = args.popOr("A font tag must have either arguments of either <value> or <namespace:value>").value();
        Key font;
        try {
            if (!args.hasNext()) {
                font = Key.key(valueOrNamespace);
            }
            else {
                final String fontKey = args.pop().value();
                font = Key.key(valueOrNamespace, fontKey);
            }
        }
        catch (final InvalidKeyException ex) {
            throw ctx.newException(ex.getMessage(), args);
        }
        return Tag.styling((Consumer<Style.Builder>)(builder -> builder.font(font)));
    }
    
    static void emit(final Key font, final TokenEmitter emitter) {
        emitter.tag("font");
        if (font.namespace().equals((Object)"minecraft")) {
            emitter.argument(font.value());
        }
        else {
            emitter.arguments(font.namespace(), font.value());
        }
    }
    
    static {
        RESOLVER = SerializableResolver.claimingStyle("font", (BiFunction<ArgumentQueue, Context, Tag>)FontTag::create, StyleClaim.claim("font", (java.util.function.Function<Style, Object>)Style::font, (java.util.function.BiConsumer<Object, TokenEmitter>)FontTag::emit));
    }
}
