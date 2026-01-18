package cc.dreamcode.kowal.libs.net.kyori.adventure.permission;

import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointer;
import java.util.function.Predicate;

public interface PermissionChecker extends Predicate<String>
{
    public static final Pointer<PermissionChecker> POINTER = Pointer.pointer(PermissionChecker.class, Key.key("adventure", "permission"));
    
    @NotNull
    default PermissionChecker always(@NotNull final TriState state) {
        Objects.requireNonNull((Object)state);
        if (state == TriState.TRUE) {
            return PermissionCheckers.TRUE;
        }
        if (state == TriState.FALSE) {
            return PermissionCheckers.FALSE;
        }
        return PermissionCheckers.NOT_SET;
    }
    
    @NotNull
    TriState value(@NotNull final String permission);
    
    default boolean test(@NotNull final String permission) {
        return this.value(permission) == TriState.TRUE;
    }
}
