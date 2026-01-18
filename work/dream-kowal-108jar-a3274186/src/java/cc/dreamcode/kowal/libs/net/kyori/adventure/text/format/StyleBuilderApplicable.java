package cc.dreamcode.kowal.libs.net.kyori.adventure.text.format;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentBuilder;
import org.jetbrains.annotations.Contract;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentBuilderApplicable;

@FunctionalInterface
public interface StyleBuilderApplicable extends ComponentBuilderApplicable
{
    @Contract(mutates = "param")
    void styleApply(final Style.Builder style);
    
    default void componentBuilderApply(@NotNull final ComponentBuilder<?, ?> component) {
        component.style((Consumer<Style.Builder>)this::styleApply);
    }
}
