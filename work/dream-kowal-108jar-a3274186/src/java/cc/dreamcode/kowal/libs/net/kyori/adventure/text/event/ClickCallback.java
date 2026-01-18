package cc.dreamcode.kowal.libs.net.kyori.adventure.text.event;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.PlatformAPI;
import java.time.temporal.TemporalAmount;
import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.permission.PermissionChecker;
import java.util.function.Predicate;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.CheckReturnValue;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import java.time.Duration;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.Audience;

@FunctionalInterface
public interface ClickCallback<T extends Audience>
{
    public static final Duration DEFAULT_LIFETIME = Duration.ofHours(12L);
    public static final int UNLIMITED_USES = -1;
    
    @CheckReturnValue
    @Contract(pure = true)
    @NotNull
    default <W extends Audience, N extends W> ClickCallback<W> widen(@NotNull final ClickCallback<N> original, @NotNull final Class<N> type, @Nullable final Consumer<? super Audience> otherwise) {
        return audience -> {
            if (type.isInstance(audience)) {
                original.accept(type.cast(audience));
            }
            else if (otherwise != null) {
                otherwise.accept((Object)audience);
            }
        };
    }
    
    @CheckReturnValue
    @Contract(pure = true)
    @NotNull
    default <W extends Audience, N extends W> ClickCallback<W> widen(@NotNull final ClickCallback<N> original, @NotNull final Class<N> type) {
        return widen(original, type, null);
    }
    
    void accept(@NotNull final T audience);
    
    @CheckReturnValue
    @Contract(pure = true)
    @NotNull
    default ClickCallback<T> filter(@NotNull final Predicate<T> filter) {
        return this.filter(filter, null);
    }
    
    @CheckReturnValue
    @Contract(pure = true)
    @NotNull
    default ClickCallback<T> filter(@NotNull final Predicate<T> filter, @Nullable final Consumer<? super Audience> otherwise) {
        return audience -> {
            if (filter.test((Object)audience)) {
                this.accept(audience);
            }
            else if (otherwise != null) {
                otherwise.accept((Object)audience);
            }
        };
    }
    
    @CheckReturnValue
    @Contract(pure = true)
    @NotNull
    default ClickCallback<T> requiringPermission(@NotNull final String permission) {
        return this.requiringPermission(permission, null);
    }
    
    @CheckReturnValue
    @Contract(pure = true)
    @NotNull
    default ClickCallback<T> requiringPermission(@NotNull final String permission, @Nullable final Consumer<? super Audience> otherwise) {
        return this.filter((Predicate<T>)(audience -> audience.getOrDefault(PermissionChecker.POINTER, ClickCallbackInternals.ALWAYS_FALSE).test(permission)), otherwise);
    }
    
    @ApiStatus.NonExtendable
    public interface Options extends Examinable
    {
        @NotNull
        default Builder builder() {
            return new ClickCallbackOptionsImpl.BuilderImpl();
        }
        
        @NotNull
        default Builder builder(@NotNull final Options existing) {
            return new ClickCallbackOptionsImpl.BuilderImpl(existing);
        }
        
        int uses();
        
        @NotNull
        Duration lifetime();
        
        @ApiStatus.NonExtendable
        public interface Builder extends AbstractBuilder<Options>
        {
            @NotNull
            Builder uses(final int useCount);
            
            @NotNull
            Builder lifetime(@NotNull final TemporalAmount duration);
        }
    }
    
    @PlatformAPI
    @ApiStatus.Internal
    public interface Provider
    {
        @NotNull
        ClickEvent create(@NotNull final ClickCallback<Audience> callback, @NotNull final Options options);
    }
}
