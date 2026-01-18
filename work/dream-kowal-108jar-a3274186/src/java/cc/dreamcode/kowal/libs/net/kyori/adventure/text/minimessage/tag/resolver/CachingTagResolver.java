package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.ClaimConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;

final class CachingTagResolver implements TagResolver.WithoutArguments, MappableResolver, SerializableResolver
{
    private static final Tag NULL_REPLACEMENT;
    private final Map<String, Tag> cache;
    private final TagResolver.WithoutArguments resolver;
    
    CachingTagResolver(final TagResolver.WithoutArguments resolver) {
        this.cache = (Map<String, Tag>)new HashMap();
        this.resolver = resolver;
    }
    
    private Tag query(@NotNull final String key) {
        return (Tag)this.cache.computeIfAbsent((Object)key, k -> {
            final Tag result = this.resolver.resolve(k);
            return (result == null) ? CachingTagResolver.NULL_REPLACEMENT : result;
        });
    }
    
    @Nullable
    @Override
    public Tag resolve(@NotNull final String name) {
        final Tag potentialValue = this.query(name);
        return (potentialValue == CachingTagResolver.NULL_REPLACEMENT) ? null : potentialValue;
    }
    
    @Override
    public boolean has(@NotNull final String name) {
        return this.query(name) != CachingTagResolver.NULL_REPLACEMENT;
    }
    
    @Override
    public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
        return this.resolver instanceof MappableResolver && ((MappableResolver)this.resolver).contributeToMap(map);
    }
    
    @Override
    public void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
        if (this.resolver instanceof SerializableResolver) {
            ((SerializableResolver)this.resolver).handle(serializable, consumer);
        }
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CachingTagResolver)) {
            return false;
        }
        final CachingTagResolver that = (CachingTagResolver)other;
        return Objects.equals((Object)this.resolver, (Object)that.resolver);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.resolver });
    }
    
    static {
        NULL_REPLACEMENT = (() -> {
            throw new UnsupportedOperationException("no-op null tag");
        });
    }
}
