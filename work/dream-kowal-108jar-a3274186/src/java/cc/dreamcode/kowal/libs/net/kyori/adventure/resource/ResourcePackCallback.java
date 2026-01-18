package cc.dreamcode.kowal.libs.net.kyori.adventure.resource;

import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import java.util.UUID;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface ResourcePackCallback
{
    @NotNull
    default ResourcePackCallback noOp() {
        return ResourcePackCallbacks.NO_OP;
    }
    
    @NotNull
    default ResourcePackCallback onTerminal(@NotNull final BiConsumer<UUID, Audience> success, @NotNull final BiConsumer<UUID, Audience> failure) {
        return (uuid, status, audience) -> {
            if (status == ResourcePackStatus.SUCCESSFULLY_LOADED) {
                success.accept((Object)uuid, (Object)audience);
            }
            else if (!status.intermediate()) {
                failure.accept((Object)uuid, (Object)audience);
            }
        };
    }
    
    void packEventReceived(@NotNull final UUID uuid, @NotNull final ResourcePackStatus status, @NotNull final Audience audience);
}
