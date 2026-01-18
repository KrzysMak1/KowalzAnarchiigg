package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface TextComponent extends BuildableComponent<TextComponent, TextComponent.Builder>, ScopedComponent<TextComponent>
{
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default TextComponent ofChildren(@NotNull final ComponentLike... components) {
        return Component.textOfChildren(components);
    }
    
    @NotNull
    String content();
    
    @Contract(pure = true)
    @NotNull
    TextComponent content(@NotNull final String content);
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object)ExaminableProperty.of("content", this.content())), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<TextComponent, Builder>
    {
        @NotNull
        String content();
        
        @Contract("_ -> this")
        @NotNull
        Builder content(@NotNull final String content);
    }
}
