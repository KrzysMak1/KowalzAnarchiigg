package cc.dreamcode.kowal.libs.net.kyori.option;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Set;

final class OptionImpl<V> implements Option<V>
{
    private static final Set<String> KNOWN_KEYS;
    private final String id;
    private final Class<V> type;
    @Nullable
    private final V defaultValue;
    
    OptionImpl(@NotNull final String id, @NotNull final Class<V> type, @Nullable final V defaultValue) {
        this.id = id;
        this.type = type;
        this.defaultValue = defaultValue;
    }
    
    static <T> Option<T> option(final String id, final Class<T> type, @Nullable final T defaultValue) {
        if (!OptionImpl.KNOWN_KEYS.add((Object)id)) {
            throw new IllegalStateException("Key " + id + " has already been used. Option keys must be unique.");
        }
        return new OptionImpl<T>((String)Objects.requireNonNull((Object)id, "id"), (Class<T>)Objects.requireNonNull((Object)type, "type"), defaultValue);
    }
    
    @NotNull
    @Override
    public String id() {
        return this.id;
    }
    
    @NotNull
    @Override
    public Class<V> type() {
        return this.type;
    }
    
    @Nullable
    @Override
    public V defaultValue() {
        return this.defaultValue;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final OptionImpl<?> that = (OptionImpl<?>)other;
        return Objects.equals((Object)this.id, (Object)that.id) && Objects.equals((Object)this.type, (Object)that.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.id, this.type });
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "{id=" + this.id + ",type=" + (Object)this.type + ",defaultValue=" + (Object)this.defaultValue + '}';
    }
    
    static {
        KNOWN_KEYS = (Set)ConcurrentHashMap.newKeySet();
    }
}
