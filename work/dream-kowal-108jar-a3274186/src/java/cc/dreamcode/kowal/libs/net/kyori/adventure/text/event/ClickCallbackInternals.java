package cc.dreamcode.kowal.libs.net.kyori.adventure.text.event;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Services;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.TriState;
import cc.dreamcode.kowal.libs.net.kyori.adventure.permission.PermissionChecker;

final class ClickCallbackInternals
{
    static final PermissionChecker ALWAYS_FALSE;
    static final ClickCallback.Provider PROVIDER;
    
    private ClickCallbackInternals() {
    }
    
    static {
        ALWAYS_FALSE = PermissionChecker.always(TriState.FALSE);
        PROVIDER = (ClickCallback.Provider)Services.service(ClickCallback.Provider.class).orElseGet(Fallback::new);
    }
    
    static final class Fallback implements ClickCallback.Provider
    {
        @NotNull
        @Override
        public ClickEvent create(@NotNull final ClickCallback<Audience> callback, final ClickCallback.Options options) {
            return ClickEvent.suggestCommand("Callbacks are not supported on this platform!");
        }
    }
}
