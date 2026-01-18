package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import java.util.regex.MatchResult;
import java.util.function.BiFunction;
import org.jetbrains.annotations.Nullable;
import java.util.function.Function;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.IntFunction2;
import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.Contract;
import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Buildable;

public interface TextReplacementConfig extends Buildable<TextReplacementConfig, TextReplacementConfig.Builder>, Examinable
{
    @NotNull
    static Builder builder() {
        return new TextReplacementConfigImpl.Builder();
    }
    
    @NotNull
    Pattern matchPattern();
    
    public interface Builder extends AbstractBuilder<TextReplacementConfig>, Buildable.Builder<TextReplacementConfig>
    {
        @Contract("_ -> this")
        default Builder matchLiteral(final String literal) {
            return this.match(Pattern.compile(literal, 16));
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder match(@NotNull @RegExp final String pattern) {
            return this.match(Pattern.compile(pattern));
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder match(@NotNull final Pattern pattern);
        
        @NotNull
        default Builder once() {
            return this.times(1);
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder times(final int times) {
            return this.condition((index, replaced) -> (replaced < times) ? PatternReplacementResult.REPLACE : PatternReplacementResult.STOP);
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder condition(@NotNull final IntFunction2<PatternReplacementResult> condition) {
            return this.condition((result, matchCount, replaced) -> condition.apply(matchCount, replaced));
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder condition(@NotNull final Condition condition);
        
        @Contract("_ -> this")
        @NotNull
        default Builder replacement(@NotNull final String replacement) {
            Objects.requireNonNull((Object)replacement, "replacement");
            return this.replacement((Function<TextComponent.Builder, ComponentLike>)(builder -> builder.content(replacement)));
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder replacement(@Nullable final ComponentLike replacement) {
            final Component baked = ComponentLike.unbox(replacement);
            return this.replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((result, input) -> baked));
        }
        
        @Contract("_ -> this")
        @NotNull
        default Builder replacement(@NotNull final Function<TextComponent.Builder, ComponentLike> replacement) {
            Objects.requireNonNull((Object)replacement, "replacement");
            return this.replacement((BiFunction<MatchResult, TextComponent.Builder, ComponentLike>)((result, input) -> (ComponentLike)replacement.apply((Object)input)));
        }
        
        @Contract("_ -> this")
        @NotNull
        Builder replacement(@NotNull final BiFunction<MatchResult, TextComponent.Builder, ComponentLike> replacement);
    }
    
    @FunctionalInterface
    public interface Condition
    {
        @NotNull
        PatternReplacementResult shouldReplace(@NotNull final MatchResult result, final int matchCount, final int replaced);
    }
}
