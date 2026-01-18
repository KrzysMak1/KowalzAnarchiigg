package cc.dreamcode.kowal.libs.net.kyori.adventure.identity;

import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import java.util.Locale;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.UUID;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointer;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface Identity extends Examinable, Identified
{
    public static final Pointer<String> NAME = Pointer.pointer(String.class, Key.key("adventure", "name"));
    public static final Pointer<UUID> UUID = Pointer.pointer(UUID.class, Key.key("adventure", "uuid"));
    public static final Pointer<Component> DISPLAY_NAME = Pointer.pointer(Component.class, Key.key("adventure", "display_name"));
    public static final Pointer<Locale> LOCALE = Pointer.pointer(Locale.class, Key.key("adventure", "locale"));
    
    @NotNull
    default Identity nil() {
        return NilIdentity.INSTANCE;
    }
    
    @NotNull
    default Identity identity(@NotNull final UUID uuid) {
        if (uuid.equals((Object)NilIdentity.NIL_UUID)) {
            return NilIdentity.INSTANCE;
        }
        return new IdentityImpl(uuid);
    }
    
    @NotNull
    UUID uuid();
    
    @NotNull
    default Identity identity() {
        return this;
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("uuid", this.uuid()));
    }
}
