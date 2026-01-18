package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface TranslationArgumentLike extends ComponentLike
{
    @NotNull
    TranslationArgument asTranslationArgument();
    
    @NotNull
    default Component asComponent() {
        return this.asTranslationArgument().asComponent();
    }
}
