package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface TranslationArgument extends TranslationArgumentLike, Examinable
{
    @NotNull
    default TranslationArgument bool(final boolean value) {
        return new TranslationArgumentImpl(value);
    }
    
    @NotNull
    default TranslationArgument numeric(@NotNull final Number value) {
        return new TranslationArgumentImpl(Objects.requireNonNull((Object)value, "value"));
    }
    
    @NotNull
    default TranslationArgument component(@NotNull final ComponentLike value) {
        if (value instanceof TranslationArgumentLike) {
            return ((TranslationArgumentLike)value).asTranslationArgument();
        }
        return new TranslationArgumentImpl(Objects.requireNonNull((Object)((ComponentLike)Objects.requireNonNull((Object)value, "value")).asComponent(), "value.asComponent()"));
    }
    
    @NotNull
    Object value();
    
    @NotNull
    default TranslationArgument asTranslationArgument() {
        return this;
    }
}
