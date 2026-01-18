package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.StyleBuilderApplicable;

final class StylingTagImpl extends AbstractTag implements Inserting
{
    private final StyleBuilderApplicable[] styles;
    
    StylingTagImpl(final StyleBuilderApplicable[] styles) {
        this.styles = styles;
    }
    
    @NotNull
    @Override
    public Component value() {
        return Component.text("", Style.style(this.styles));
    }
    
    @Override
    public int hashCode() {
        return 31 + Arrays.hashCode((Object[])this.styles);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof StylingTagImpl)) {
            return false;
        }
        final StylingTagImpl that = (StylingTagImpl)other;
        return Arrays.equals((Object[])this.styles, (Object[])that.styles);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("styles", this.styles));
    }
}
