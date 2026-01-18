package cc.dreamcode.kowal.libs.net.kyori.adventure.text;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;

final class TranslationArgumentImpl implements TranslationArgument
{
    private static final Component TRUE;
    private static final Component FALSE;
    private final Object value;
    
    TranslationArgumentImpl(final Object value) {
        this.value = value;
    }
    
    @NotNull
    @Override
    public Object value() {
        return this.value;
    }
    
    @NotNull
    @Override
    public Component asComponent() {
        if (this.value instanceof Component) {
            return (Component)this.value;
        }
        if (this.value instanceof Boolean) {
            return this.value ? TranslationArgumentImpl.TRUE : TranslationArgumentImpl.FALSE;
        }
        return Component.text(String.valueOf(this.value));
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final TranslationArgumentImpl that = (TranslationArgumentImpl)other;
        return Objects.equals(this.value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.value });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("value", this.value));
    }
    
    static {
        TRUE = Component.text("true");
        FALSE = Component.text("false");
    }
}
