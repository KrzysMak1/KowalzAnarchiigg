package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.Objects;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;

final class SingleResolver implements TagResolver.Single, MappableResolver
{
    private final String key;
    private final Tag tag;
    
    SingleResolver(final String key, final Tag tag) {
        this.key = key;
        this.tag = tag;
    }
    
    @NotNull
    @Override
    public String key() {
        return this.key;
    }
    
    @NotNull
    @Override
    public Tag tag() {
        return this.tag;
    }
    
    @Override
    public boolean contributeToMap(@NotNull final Map<String, Tag> map) {
        map.put((Object)this.key, (Object)this.tag);
        return true;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.key, this.tag });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (this.getClass() != other.getClass()) {
            return false;
        }
        final SingleResolver that = (SingleResolver)other;
        return Objects.equals((Object)this.key, (Object)that.key) && Objects.equals((Object)this.tag, (Object)that.tag);
    }
}
