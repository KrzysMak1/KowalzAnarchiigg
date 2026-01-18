package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface ScoreComponent extends BuildableComponent<ScoreComponent, ScoreComponent.Builder>, ScopedComponent<ScoreComponent>
{
    @NotNull
    String name();
    
    @Contract(pure = true)
    @NotNull
    ScoreComponent name(@NotNull final String name);
    
    @NotNull
    String objective();
    
    @Contract(pure = true)
    @NotNull
    ScoreComponent objective(@NotNull final String objective);
    
    @Deprecated
    @Nullable
    String value();
    
    @Deprecated
    @Contract(pure = true)
    @NotNull
    ScoreComponent value(@Nullable final String value);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("name", this.name()), ExaminableProperty.of("objective", this.objective()), ExaminableProperty.of("value", this.value()) }), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<ScoreComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder name(@NotNull final String name);
        
        @Contract("_ -> this")
        @NotNull
        Builder objective(@NotNull final String objective);
        
        @Deprecated
        @Contract("_ -> this")
        @NotNull
        Builder value(@Nullable final String value);
    }
}
