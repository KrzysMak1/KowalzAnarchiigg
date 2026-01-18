package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.UUID;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.InvalidKeyException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.DataComponentValue;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Keyed;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import java.util.HashMap;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import java.util.function.BiConsumer;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.StyleClaim;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class HoverTag
{
    private static final String HOVER = "hover";
    static final TagResolver RESOLVER;
    
    private HoverTag() {
    }
    
    static Tag create(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String actionName = args.popOr("Hover event requires an action as its first argument").value();
        final HoverEvent.Action<Object> action = HoverEvent.Action.NAMES.value(actionName);
        final ActionHandler<Object> value = actionHandler(action);
        if (value == null) {
            throw ctx.newException("Don't know how to turn '" + (Object)args + "' into a hover event", args);
        }
        return Tag.styling(HoverEvent.hoverEvent(action, value.parse(args, ctx)));
    }
    
    static void emit(final HoverEvent<?> event, final TokenEmitter emitter) {
        final ActionHandler<Object> handler = actionHandler(event.action());
        emitter.tag("hover").argument(HoverEvent.Action.NAMES.key(event.action()));
        handler.emit(event.value(), emitter);
    }
    
    @Nullable
    static <V> ActionHandler<V> actionHandler(final HoverEvent.Action<V> action) {
        ActionHandler<?> ret = null;
        if (action == HoverEvent.Action.SHOW_TEXT) {
            ret = ShowText.INSTANCE;
        }
        else if (action == HoverEvent.Action.SHOW_ITEM) {
            ret = ShowItem.INSTANCE;
        }
        else if (action == HoverEvent.Action.SHOW_ENTITY) {
            ret = ShowEntity.INSTANCE;
        }
        return (ActionHandler<V>)ret;
    }
    
    @NotNull
    static String compactAsString(@NotNull final Key key) {
        if (key.namespace().equals((Object)"minecraft")) {
            return key.value();
        }
        return key.asString();
    }
    
    static {
        RESOLVER = SerializableResolver.claimingStyle("hover", (BiFunction<ArgumentQueue, Context, Tag>)HoverTag::create, StyleClaim.claim("hover", (java.util.function.Function<Style, Object>)Style::hoverEvent, (java.util.function.BiConsumer<Object, TokenEmitter>)HoverTag::emit));
    }
    
    static final class ShowText implements ActionHandler<Component>
    {
        private static final ShowText INSTANCE;
        
        private ShowText() {
        }
        
        @NotNull
        @Override
        public Component parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
            return ctx.deserialize(args.popOr("show_text action requires a message").value());
        }
        
        @Override
        public void emit(final Component event, final TokenEmitter emit) {
            emit.argument(event);
        }
        
        static {
            INSTANCE = new ShowText();
        }
    }
    
    static final class ShowItem implements ActionHandler<HoverEvent.ShowItem>
    {
        private static final ShowItem INSTANCE;
        
        private ShowItem() {
        }
        
        @Override
        public HoverEvent.ShowItem parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
            try {
                final Key key = Key.key(args.popOr("Show item hover needs at least an item ID").value());
                final int count = args.hasNext() ? args.pop().asInt().orElseThrow(() -> ctx.newException("The count argument was not a valid integer")) : 1;
                if (!args.hasNext()) {
                    return HoverEvent.ShowItem.showItem(key, count);
                }
                final String value = args.peek().value();
                if (value.startsWith("{")) {
                    args.pop();
                    return legacyShowItem(key, count, value);
                }
                final Map<Key, DataComponentValue> datas = (Map<Key, DataComponentValue>)new HashMap();
                while (args.hasNext()) {
                    final Key dataKey = Key.key(args.pop().value());
                    final String dataVal = args.popOr("a value was expected for key " + (Object)dataKey).value();
                    datas.put((Object)dataKey, (Object)BinaryTagHolder.binaryTagHolder(dataVal));
                }
                return HoverEvent.ShowItem.showItem(key, count, datas);
            }
            catch (final InvalidKeyException | NumberFormatException ex) {
                throw ctx.newException("Exception parsing show_item hover", (Throwable)ex, args);
            }
        }
        
        private static HoverEvent.ShowItem legacyShowItem(final Key id, final int count, final String value) {
            return HoverEvent.ShowItem.showItem(id, count, BinaryTagHolder.binaryTagHolder(value));
        }
        
        @Override
        public void emit(final HoverEvent.ShowItem event, final TokenEmitter emit) {
            emit.argument(HoverTag.compactAsString(event.item()));
            if (event.count() != 1 || hasLegacy(event) || !event.dataComponents().isEmpty()) {
                emit.argument(Integer.toString(event.count()));
                if (hasLegacy(event)) {
                    emitLegacyHover(event, emit);
                }
                else {
                    for (final Map.Entry<Key, DataComponentValue.TagSerializable> entry : event.dataComponentsAs(DataComponentValue.TagSerializable.class).entrySet()) {
                        emit.argument(((Key)entry.getKey()).asMinimalString());
                        emit.argument(((DataComponentValue.TagSerializable)entry.getValue()).asBinaryTag().string());
                    }
                }
            }
        }
        
        static boolean hasLegacy(final HoverEvent.ShowItem event) {
            return event.nbt() != null;
        }
        
        static void emitLegacyHover(final HoverEvent.ShowItem event, final TokenEmitter emit) {
            if (event.nbt() != null) {
                emit.argument(event.nbt().string());
            }
        }
        
        static {
            INSTANCE = new ShowItem();
        }
    }
    
    static final class ShowEntity implements ActionHandler<HoverEvent.ShowEntity>
    {
        static final ShowEntity INSTANCE;
        
        private ShowEntity() {
        }
        
        @Override
        public HoverEvent.ShowEntity parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException {
            try {
                final Key key = Key.key(args.popOr("Show entity needs a type argument").value());
                final UUID id = UUID.fromString(args.popOr("Show entity needs an entity UUID").value());
                if (args.hasNext()) {
                    final Component name = ctx.deserialize(args.pop().value());
                    return HoverEvent.ShowEntity.showEntity(key, id, name);
                }
                return HoverEvent.ShowEntity.showEntity(key, id);
            }
            catch (final IllegalArgumentException | InvalidKeyException ex) {
                throw ctx.newException("Exception parsing show_entity hover", (Throwable)ex, args);
            }
        }
        
        @Override
        public void emit(final HoverEvent.ShowEntity event, final TokenEmitter emit) {
            emit.argument(HoverTag.compactAsString(event.type())).argument(event.id().toString());
            if (event.name() != null) {
                emit.argument(event.name());
            }
        }
        
        static {
            INSTANCE = new ShowEntity();
        }
    }
    
    interface ActionHandler<V>
    {
        @NotNull
        V parse(@NotNull final ArgumentQueue args, @NotNull final Context ctx) throws ParsingException;
        
        void emit(final V event, final TokenEmitter emit);
    }
}
