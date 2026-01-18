package cc.dreamcode.kowal.libs.net.kyori.adventure.audience;

import java.util.stream.Collectors;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackStatus;
import java.util.UUID;
import cc.dreamcode.kowal.libs.net.kyori.adventure.resource.ResourcePackCallback;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.stream.Collector;

public final class Audiences
{
    static final Collector<? super Audience, ?, ForwardingAudience> COLLECTOR;
    
    private Audiences() {
    }
    
    @NotNull
    public static Consumer<? super Audience> sendingMessage(@NotNull final ComponentLike message) {
        return (Consumer<? super Audience>)(audience -> audience.sendMessage(message));
    }
    
    @NotNull
    static ResourcePackCallback unwrapCallback(final Audience forwarding, final Audience dest, @NotNull final ResourcePackCallback cb) {
        if (cb == ResourcePackCallback.noOp()) {
            return cb;
        }
        return (uuid, status, audience) -> cb.packEventReceived(uuid, status, (audience == dest) ? forwarding : audience);
    }
    
    static {
        COLLECTOR = Collectors.collectingAndThen(Collectors.toCollection(ArrayList::new), audiences -> Audience.audience((Iterable<? extends Audience>)Collections.unmodifiableCollection((Collection)audiences)));
    }
}
