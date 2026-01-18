package cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer;

import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

public interface ComponentRenderer<C>
{
    @NotNull
    Component render(@NotNull final Component component, @NotNull final C context);
    
    default <T> ComponentRenderer<T> mapContext(final Function<T, C> transformer) {
        return (component, ctx) -> this.render(component, transformer.apply(ctx));
    }
}
