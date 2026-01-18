package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard;

import java.util.function.Function;
import java.util.function.BiFunction;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.TokenEmitter;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.NBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.StorageNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.EntityNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.Emitable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.NBTComponentBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.BlockNBTComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class NbtTag
{
    private static final String NBT = "nbt";
    private static final String DATA = "data";
    private static final String BLOCK = "block";
    private static final String ENTITY = "entity";
    private static final String STORAGE = "storage";
    private static final String INTERPRET = "interpret";
    static final TagResolver RESOLVER;
    
    private NbtTag() {
    }
    
    static Tag resolve(final ArgumentQueue args, final Context ctx) throws ParsingException {
        final String type = args.popOr("a type of block, entity, or storage is required").lowerValue();
        NBTComponentBuilder<?, ?> builder;
        if ("block".equals((Object)type)) {
            final String pos = args.popOr("A position is required").value();
            try {
                builder = Component.blockNBT().pos(BlockNBTComponent.Pos.fromString(pos));
            }
            catch (final IllegalArgumentException ex) {
                throw ctx.newException(ex.getMessage(), args);
            }
        }
        else if ("entity".equals((Object)type)) {
            builder = Component.entityNBT().selector(args.popOr("A selector is required").value());
        }
        else {
            if (!"storage".equals((Object)type)) {
                throw ctx.newException("Unknown nbt tag type '" + type + "'", args);
            }
            builder = Component.storageNBT().storage(Key.key(args.popOr("A storage key is required").value()));
        }
        builder.nbtPath(args.popOr("An NBT path is required").value());
        if (args.hasNext()) {
            final String popped = args.pop().value();
            if ("interpret".equalsIgnoreCase(popped)) {
                builder.interpret(true);
            }
            else {
                builder.separator(ctx.deserialize(popped));
                if (args.hasNext() && args.pop().value().equalsIgnoreCase("interpret")) {
                    builder.interpret(true);
                }
            }
        }
        return Tag.inserting(builder.build());
    }
    
    @Nullable
    static Emitable emit(final Component comp) {
        String type;
        String id;
        if (comp instanceof BlockNBTComponent) {
            type = "block";
            id = ((BlockNBTComponent)comp).pos().asString();
        }
        else if (comp instanceof EntityNBTComponent) {
            type = "entity";
            id = ((EntityNBTComponent)comp).selector();
        }
        else {
            if (!(comp instanceof StorageNBTComponent)) {
                return null;
            }
            type = "storage";
            id = ((StorageNBTComponent)comp).storage().asString();
        }
        return out -> {
            final NBTComponent nbt = (NBTComponent)comp;
            out.tag("nbt").argument(type).argument(id).argument(nbt.nbtPath());
            if (nbt.separator() != null) {
                out.argument(nbt.separator());
            }
            if (nbt.interpret()) {
                out.argument("interpret");
            }
        };
    }
    
    static {
        RESOLVER = SerializableResolver.claimingComponent(StandardTags.names("nbt", "data"), (BiFunction<ArgumentQueue, Context, Tag>)NbtTag::resolve, (Function<Component, Emitable>)NbtTag::emit);
    }
}
