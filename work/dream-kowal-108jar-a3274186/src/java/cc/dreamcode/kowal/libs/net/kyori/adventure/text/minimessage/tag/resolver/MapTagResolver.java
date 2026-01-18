package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;

final class MapTagResolver implements TagResolver.WithoutArguments, MappableResolver
{
    private final Map<String, ? extends Tag> tagMap;
    
    MapTagResolver(@NotNull final Map<String, ? extends Tag> placeholderMap) {
        this.tagMap = placeholderMap;
    }
    
    @Nullable
    @Override
    public Tag resolve(@NotNull final String name) {
        return (Tag)this.tagMap.get((Object)name);
    }
    
    @Override
    public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
        map.putAll((Map)this.tagMap);
        return true;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MapTagResolver)) {
            return false;
        }
        final MapTagResolver that = (MapTagResolver)other;
        return Objects.equals((Object)this.tagMap, (Object)that.tagMap);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.tagMap });
    }
}
