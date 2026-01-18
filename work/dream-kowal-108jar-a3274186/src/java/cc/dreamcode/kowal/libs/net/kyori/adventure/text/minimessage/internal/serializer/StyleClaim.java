package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.BiConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public interface StyleClaim<V>
{
    @NotNull
    default <T> StyleClaim<T> claim(@NotNull final String claimKey, @NotNull final Function<Style, T> lens, @NotNull final BiConsumer<T, TokenEmitter> emitable) {
        return claim(claimKey, lens, (java.util.function.Predicate<T>)($ -> true), emitable);
    }
    
    @NotNull
    default <T> StyleClaim<T> claim(@NotNull final String claimKey, @NotNull final Function<Style, T> lens, @NotNull final Predicate<T> filter, @NotNull final BiConsumer<T, TokenEmitter> emitable) {
        return new StyleClaimImpl<T>((String)Objects.requireNonNull((Object)claimKey, "claimKey"), (java.util.function.Function<Style, T>)Objects.requireNonNull((Object)lens, "lens"), (java.util.function.Predicate<T>)Objects.requireNonNull((Object)filter, "filter"), (java.util.function.BiConsumer<T, TokenEmitter>)Objects.requireNonNull((Object)emitable, "emitable"));
    }
    
    @NotNull
    String claimKey();
    
    @Nullable
    Emitable apply(@NotNull final Style style);
}
