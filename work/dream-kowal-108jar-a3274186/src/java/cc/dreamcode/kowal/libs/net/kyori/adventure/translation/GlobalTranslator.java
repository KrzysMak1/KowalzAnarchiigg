package cc.dreamcode.kowal.libs.net.kyori.adventure.translation;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.Locale;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.renderer.TranslatableComponentRenderer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface GlobalTranslator extends Translator, Examinable
{
    @NotNull
    default GlobalTranslator translator() {
        return GlobalTranslatorImpl.INSTANCE;
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default GlobalTranslator get() {
        return GlobalTranslatorImpl.INSTANCE;
    }
    
    @NotNull
    default TranslatableComponentRenderer<Locale> renderer() {
        return GlobalTranslatorImpl.INSTANCE.renderer;
    }
    
    @NotNull
    default Component render(@NotNull final Component component, @NotNull final Locale locale) {
        return renderer().render(component, locale);
    }
    
    @NotNull
    Iterable<? extends Translator> sources();
    
    boolean addSource(@NotNull final Translator source);
    
    boolean removeSource(@NotNull final Translator source);
}
