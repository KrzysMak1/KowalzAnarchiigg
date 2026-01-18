package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import org.jetbrains.annotations.Contract;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.translation.Translatable;
import org.jetbrains.annotations.NotNull;

public interface TranslatableComponent extends BuildableComponent<TranslatableComponent, TranslatableComponent.Builder>, ScopedComponent<TranslatableComponent>
{
    @NotNull
    String key();
    
    @Contract(pure = true)
    @NotNull
    default TranslatableComponent key(@NotNull final Translatable translatable) {
        return this.key(((Translatable)Objects.requireNonNull((Object)translatable, "translatable")).translationKey());
    }
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent key(@NotNull final String key);
    
    @Deprecated
    @NotNull
    List<Component> args();
    
    @Deprecated
    @Contract(pure = true)
    @NotNull
    default TranslatableComponent args(@NotNull final ComponentLike... args) {
        return this.arguments(args);
    }
    
    @Deprecated
    @Contract(pure = true)
    @NotNull
    default TranslatableComponent args(@NotNull final List<? extends ComponentLike> args) {
        return this.arguments(args);
    }
    
    @NotNull
    List<TranslationArgument> arguments();
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent arguments(@NotNull final ComponentLike... args);
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent arguments(@NotNull final List<? extends ComponentLike> args);
    
    @Nullable
    String fallback();
    
    @Contract(pure = true)
    @NotNull
    TranslatableComponent fallback(@Nullable final String fallback);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("key", this.key()), ExaminableProperty.of("arguments", this.arguments()), ExaminableProperty.of("fallback", this.fallback()) }), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<TranslatableComponent, Builder>
    {
        @Contract(pure = true)
        @NotNull
        default Builder key(@NotNull final Translatable translatable) {
            return this.key(((Translatable)Objects.requireNonNull((Object)translatable, "translatable")).translationKey());
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder key(@NotNull final String key);
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        default Builder args(@NotNull final ComponentBuilder<?, ?> arg) {
            return this.arguments(arg);
        }
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        default Builder args(@NotNull final ComponentBuilder<?, ?>... args) {
            return this.arguments((ComponentLike[])args);
        }
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        default Builder args(@NotNull final Component arg) {
            return this.arguments(arg);
        }
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        default Builder args(@NotNull final ComponentLike... args) {
            return this.arguments(args);
        }
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        default Builder args(@NotNull final List<? extends ComponentLike> args) {
            return this.arguments(args);
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder arguments(@NotNull final ComponentLike... args);
        
        @Contract("_ -> this")
        @NotNull
        Builder arguments(@NotNull final List<? extends ComponentLike> args);
        
        @Contract("_ -> this")
        @NotNull
        Builder fallback(@Nullable final String fallback);
    }
}
