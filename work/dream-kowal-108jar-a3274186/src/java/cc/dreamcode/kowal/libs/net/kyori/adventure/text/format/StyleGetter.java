package cc.dreamcode.kowal.libs.net.kyori.adventure.text.format;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.HoverEvent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.event.ClickEvent;
import java.util.EnumMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.key.Key;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface StyleGetter
{
    @Nullable
    Key font();
    
    @Nullable
    TextColor color();
    
    default boolean hasDecoration(@NotNull final TextDecoration decoration) {
        return this.decoration(decoration) == TextDecoration.State.TRUE;
    }
    
    TextDecoration.State decoration(@NotNull final TextDecoration decoration);
    
    @NotNull
    default Map<TextDecoration, TextDecoration.State> decorations() {
        final Map<TextDecoration, TextDecoration.State> decorations = (Map<TextDecoration, TextDecoration.State>)new EnumMap((Class)TextDecoration.class);
        for (int i = 0, length = DecorationMap.DECORATIONS.length; i < length; ++i) {
            final TextDecoration decoration = DecorationMap.DECORATIONS[i];
            final TextDecoration.State value = this.decoration(decoration);
            decorations.put((Object)decoration, (Object)value);
        }
        return decorations;
    }
    
    @Nullable
    ClickEvent clickEvent();
    
    @Nullable
    HoverEvent<?> hoverEvent();
    
    @Nullable
    String insertion();
}
