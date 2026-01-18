package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.function.Function;

class StyleClaimImpl<V> implements StyleClaim<V>
{
    private final String claimKey;
    private final Function<Style, V> lens;
    private final Predicate<V> filter;
    private final BiConsumer<V, TokenEmitter> emitable;
    
    StyleClaimImpl(final String claimKey, final Function<Style, V> lens, final Predicate<V> filter, final BiConsumer<V, TokenEmitter> emitable) {
        this.claimKey = claimKey;
        this.lens = lens;
        this.filter = filter;
        this.emitable = emitable;
    }
    
    @NotNull
    @Override
    public String claimKey() {
        return this.claimKey;
    }
    
    @Nullable
    @Override
    public Emitable apply(@NotNull final Style style) {
        final V element = (V)this.lens.apply((Object)style);
        if (element == null || !this.filter.test((Object)element)) {
            return null;
        }
        return emitter -> this.emitable.accept(element, (Object)emitter);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.claimKey });
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StyleClaimImpl)) {
            return false;
        }
        final StyleClaimImpl<?> that = (StyleClaimImpl<?>)other;
        return Objects.equals((Object)this.claimKey, (Object)that.claimKey);
    }
}
