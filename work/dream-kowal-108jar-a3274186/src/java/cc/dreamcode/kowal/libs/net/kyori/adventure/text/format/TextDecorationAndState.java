package cc.dreamcode.kowal.libs.net.kyori.adventure.text.format;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

@ApiStatus.NonExtendable
public interface TextDecorationAndState extends Examinable, StyleBuilderApplicable
{
    @NotNull
    TextDecoration decoration();
    
    TextDecoration.State state();
    
    default void styleApply(final Style.Builder style) {
        style.decoration(this.decoration(), this.state());
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("decoration", this.decoration()), ExaminableProperty.of("state", this.state()) });
    }
}
