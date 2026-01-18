package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;
import java.util.Set;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class StyleClaimingResolverImpl implements TagResolver, SerializableResolver.Single
{
    @NotNull
    private final Set<String> names;
    @NotNull
    private final BiFunction<ArgumentQueue, Context, Tag> handler;
    @NotNull
    private final StyleClaim<?> styleClaim;
    
    StyleClaimingResolverImpl(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final StyleClaim<?> styleClaim) {
        this.names = names;
        this.handler = handler;
        this.styleClaim = styleClaim;
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
    public StyleClaim<?> claimStyle() {
        return this.styleClaim;
    }
}
