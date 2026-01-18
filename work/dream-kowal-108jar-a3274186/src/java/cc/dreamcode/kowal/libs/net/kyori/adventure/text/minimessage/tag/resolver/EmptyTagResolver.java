package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.Map;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;

final class EmptyTagResolver implements TagResolver, MappableResolver, SerializableResolver
{
    static final EmptyTagResolver INSTANCE;
    
    private EmptyTagResolver() {
    }
    
    @Nullable
    @Override
    public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) {
        return null;
    }
    
    @Override
    public boolean has(@NotNull final String name) {
        return false;
    }
    
    @Override
    public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
        return true;
    }
    
    @Override
    public void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
    }
    
    static {
        INSTANCE = new EmptyTagResolver();
    }
}
