package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface SelectorComponent extends BuildableComponent<SelectorComponent, SelectorComponent.Builder>, ScopedComponent<SelectorComponent>
{
    @NotNull
    String pattern();
    
    @Contract(pure = true)
    @NotNull
    SelectorComponent pattern(@NotNull final String pattern);
    
    @Nullable
    Component separator();
    
    @NotNull
    SelectorComponent separator(@Nullable final ComponentLike separator);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("pattern", this.pattern()), ExaminableProperty.of("separator", this.separator()) }), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<SelectorComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder pattern(@NotNull final String pattern);
        
        @Contract("_ -> this")
        @NotNull
        Builder separator(@Nullable final ComponentLike separator);
    }
}
