package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface KeybindComponent extends BuildableComponent<KeybindComponent, KeybindComponent.Builder>, ScopedComponent<KeybindComponent>
{
    @NotNull
    String keybind();
    
    @Contract(pure = true)
    @NotNull
    KeybindComponent keybind(@NotNull final String keybind);
    
    @Contract(pure = true)
    @NotNull
    default KeybindComponent keybind(@NotNull final KeybindLike keybind) {
        return this.keybind(((KeybindLike)Objects.requireNonNull((Object)keybind, "keybind")).asKeybind());
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.concat(Stream.of((Object)ExaminableProperty.of("keybind", this.keybind())), (Stream)super.examinableProperties());
    }
    
    public interface Builder extends ComponentBuilder<KeybindComponent, Builder>
    {
        @Contract("_ -> this")
        @NotNull
        Builder keybind(@NotNull final String keybind);
        
        @Contract(pure = true)
        @NotNull
        default Builder keybind(@NotNull final KeybindLike keybind) {
            return this.keybind(((KeybindLike)Objects.requireNonNull((Object)keybind, "keybind")).asKeybind());
        }
    }
    
    public interface KeybindLike
    {
        @NotNull
        String asKeybind();
    }
}
