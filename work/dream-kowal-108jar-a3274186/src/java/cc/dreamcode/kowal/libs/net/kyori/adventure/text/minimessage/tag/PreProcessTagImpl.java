package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

final class PreProcessTagImpl extends AbstractTag implements PreProcess
{
    private final String value;
    
    PreProcessTagImpl(final String value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public String value() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.value });
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof PreProcessTagImpl)) {
            return false;
        }
        final PreProcessTagImpl that = (PreProcessTagImpl)other;
        return Objects.equals((Object)this.value, (Object)that.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("value", this.value));
    }
}
