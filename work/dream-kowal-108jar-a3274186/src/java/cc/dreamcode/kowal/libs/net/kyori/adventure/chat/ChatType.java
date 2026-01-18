package cc.dreamcode.kowal.libs.net.kyori.adventure.chat;

import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Keyed;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface ChatType extends Examinable, Keyed
{
    public static final ChatType CHAT = new ChatTypeImpl(Key.key("chat"));
    public static final ChatType SAY_COMMAND = new ChatTypeImpl(Key.key("say_command"));
    public static final ChatType MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("msg_command_incoming"));
    public static final ChatType MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("msg_command_outgoing"));
    public static final ChatType TEAM_MSG_COMMAND_INCOMING = new ChatTypeImpl(Key.key("team_msg_command_incoming"));
    public static final ChatType TEAM_MSG_COMMAND_OUTGOING = new ChatTypeImpl(Key.key("team_msg_command_outgoing"));
    public static final ChatType EMOTE_COMMAND = new ChatTypeImpl(Key.key("emote_command"));
    
    @NotNull
    default ChatType chatType(@NotNull final Keyed key) {
        return (key instanceof ChatType) ? ((ChatType)key) : new ChatTypeImpl(((Keyed)Objects.requireNonNull((Object)key, "key")).key());
    }
    
    @Contract(value = "_ -> new", pure = true)
    default Bound bind(@NotNull final ComponentLike name) {
        return this.bind(name, null);
    }
    
    @Contract(value = "_, _ -> new", pure = true)
    default Bound bind(@NotNull final ComponentLike name, @Nullable final ComponentLike target) {
        return new ChatTypeImpl.BoundImpl(this, (Component)Objects.requireNonNull((Object)name.asComponent(), "name"), ComponentLike.unbox(target));
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("key", this.key()));
    }
    
    public interface Bound extends Examinable
    {
        @Contract(pure = true)
        @NotNull
        ChatType type();
        
        @Contract(pure = true)
        @NotNull
        Component name();
        
        @Contract(pure = true)
        @Nullable
        Component target();
        
        @NotNull
        default Stream<? extends ExaminableProperty> examinableProperties() {
            return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("type", this.type()), ExaminableProperty.of("name", this.name()), ExaminableProperty.of("target", this.target()) });
        }
    }
}
