package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.Function;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class NewlineTag
{
    private static final String BR = "br";
    private static final String NEWLINE = "newline";
    static final TagResolver RESOLVER;
    
    private NewlineTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        return Tag.selfClosingInserting(Component.newline());
    }
    
    @Nullable
    static Emitable claimComponent(final Component input) {
        if (Component.newline().equals(input)) {
            return emit -> emit.selfClosingTag("br");
        }
        return null;
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("newline", "br"), (BiFunction<ArgumentQueue, Context, Tag>)NewlineTag::create, (Function<Component, Emitable>)NewlineTag::claimComponent);
    }
}
