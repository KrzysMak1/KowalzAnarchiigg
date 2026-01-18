package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Locale;
import org.jetbrains.annotations.ApiStatus;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

public interface Tag
{
    @NotNull
    default PreProcess preProcessParsed(@NotNull final String content) {
        return new PreProcessTagImpl((String)Objects.requireNonNull((Object)content, "content"));
    }
    
    @NotNull
    default Tag inserting(@NotNull final Component content) {
        return new InsertingImpl(true, (Component)Objects.requireNonNull((Object)content, "content must not be null"));
    }
    
    @NotNull
    default Tag inserting(@NotNull final ComponentLike value) {
        return inserting(((ComponentLike)Objects.requireNonNull((Object)value, "value")).asComponent());
    }
    
    @NotNull
    default Tag selfClosingInserting(@NotNull final Component content) {
        return new InsertingImpl(false, (Component)Objects.requireNonNull((Object)content, "content must not be null"));
    }
    
    @NotNull
    default Tag selfClosingInserting(@NotNull final ComponentLike value) {
        return selfClosingInserting(((ComponentLike)Objects.requireNonNull((Object)value, "value")).asComponent());
    }
    
    @NotNull
    default Tag styling(final Consumer<Style.Builder> styles) {
        return new CallbackStylingTagImpl(styles);
    }
    
    @NotNull
    default Tag styling(@NotNull final StyleBuilderApplicable... actions) {
        Objects.requireNonNull((Object)actions, "actions");
        for (int i = 0, length = actions.length; i < length; ++i) {
            if (actions[i] == null) {
                throw new NullPointerException("actions[" + i + "]");
            }
        }
        return new StylingTagImpl((StyleBuilderApplicable[])Arrays.copyOf((Object[])actions, actions.length));
    }
    
    @ApiStatus.NonExtendable
    public interface Argument
    {
        @NotNull
        String value();
        
        @NotNull
        default String lowerValue() {
            return this.value().toLowerCase(Locale.ROOT);
        }
        
        default boolean isTrue() {
            return "true".equals((Object)this.value()) || "on".equals((Object)this.value());
        }
        
        default boolean isFalse() {
            return "false".equals((Object)this.value()) || "off".equals((Object)this.value());
        }
        
        @NotNull
        default OptionalInt asInt() {
            try {
                return OptionalInt.of(Integer.parseInt(this.value()));
            }
            catch (final NumberFormatException ex) {
                return OptionalInt.empty();
            }
        }
        
        @NotNull
        default OptionalDouble asDouble() {
            try {
                return OptionalDouble.of(Double.parseDouble(this.value()));
            }
            catch (final NumberFormatException ex) {
                return OptionalDouble.empty();
            }
        }
    }
}
