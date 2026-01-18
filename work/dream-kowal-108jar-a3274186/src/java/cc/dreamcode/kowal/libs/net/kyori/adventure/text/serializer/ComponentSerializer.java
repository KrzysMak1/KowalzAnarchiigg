package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

public interface ComponentSerializer<I extends Component, O extends Component, R> extends ComponentEncoder<I, R>, ComponentDecoder<R, O>
{
    @NotNull
    O deserialize(@NotNull final R input);
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @Contract(value = "!null -> !null; null -> null", pure = true)
    @Nullable
    default O deseializeOrNull(@Nullable final R input) {
        return super.deserializeOrNull(input);
    }
    
    @Contract(value = "!null -> !null; null -> null", pure = true)
    @Nullable
    default O deserializeOrNull(@Nullable final R input) {
        return super.deserializeOr(input, null);
    }
    
    @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
    @Nullable
    default O deserializeOr(@Nullable final R input, @Nullable final O fallback) {
        return super.deserializeOr(input, fallback);
    }
    
    @NotNull
    R serialize(@NotNull final I component);
    
    @Contract(value = "!null -> !null; null -> null", pure = true)
    @Nullable
    default R serializeOrNull(@Nullable final I component) {
        return this.serializeOr(component, null);
    }
    
    @Contract(value = "!null, _ -> !null; null, _ -> param2", pure = true)
    @Nullable
    default R serializeOr(@Nullable final I component, @Nullable final R fallback) {
        if (component == null) {
            return fallback;
        }
        return this.serialize(component);
    }
}
