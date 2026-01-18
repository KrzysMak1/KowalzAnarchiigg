package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.Function;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.KeybindComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class KeybindTag
{
    public static final String KEYBIND = "key";
    static final TagResolver RESOLVER;
    
    private KeybindTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        return Tag.inserting(Component.keybind(args.popOr("A keybind id is required").value()));
    }
    
    @Nullable
    static Emitable emit(final Component component) {
        if (!(component instanceof KeybindComponent)) {
            return null;
        }
        final String key = ((KeybindComponent)component).keybind();
        return emit -> emit.tag("key").argument(key);
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent("key", (BiFunction<ArgumentQueue, Context, Tag>)KeybindTag::create, (Function<Component, Emitable>)KeybindTag::emit);
    }
}
