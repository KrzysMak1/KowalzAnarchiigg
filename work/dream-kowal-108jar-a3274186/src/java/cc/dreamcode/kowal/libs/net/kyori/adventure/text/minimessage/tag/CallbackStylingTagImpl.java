package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.Style;
import java.util.function.Consumer;

final class CallbackStylingTagImpl extends AbstractTag implements Inserting
{
    private final Consumer<Style.Builder> styles;
    
    CallbackStylingTagImpl(final Consumer<Style.Builder> styles) {
        this.styles = styles;
    }
    
    @NotNull
    @Override
    public Component value() {
        return Component.text("", Style.style(this.styles));
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.styles });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CallbackStylingTagImpl)) {
            return false;
        }
        final CallbackStylingTagImpl that = (CallbackStylingTagImpl)other;
        return Objects.equals((Object)this.styles, (Object)that.styles);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("styles", this.styles));
    }
}
