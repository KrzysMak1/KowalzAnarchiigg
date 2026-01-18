package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

class ComponentClaimingResolverImpl implements TagResolver, SerializableResolver.Single
{
    @NotNull
    private final Set<String> names;
    @NotNull
    private final BiFunction<ArgumentQueue, Context, Tag> handler;
    @NotNull
    private final Function<Component, Emitable> componentClaim;
    
    ComponentClaimingResolverImpl(final Set<String> names, final BiFunction<ArgumentQueue, Context, Tag> handler, final Function<Component, Emitable> componentClaim) {
        this.names = names;
        this.handler = handler;
        this.componentClaim = componentClaim;
    }
    
    @Nullable
    @Override
    public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
        if (!this.names.contains((Object)name)) {
            return null;
        }
        return (Tag)this.handler.apply((Object)arguments, (Object)ctx);
    }
    
    @Override
    public boolean has(@NotNull final String name) {
        return this.names.contains((Object)name);
    }
    
    @Nullable
    @Override
    public Emitable claimComponent(@NotNull final Component component) {
        return (Emitable)this.componentClaim.apply((Object)component);
    }
}
