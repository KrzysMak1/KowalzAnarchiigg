package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

final class InsertingImpl extends AbstractTag implements Inserting
{
    private final boolean allowsChildren;
    private final Component value;
    
    InsertingImpl(final boolean allowsChildren, final Component value) {
        this.allowsChildren = allowsChildren;
        this.value = value;
    }
    
    @Override
    public boolean allowsChildren() {
        return this.allowsChildren;
    }
    
    @NotNull
    @Override
    public Component value() {
        return this.value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.allowsChildren, this.value });
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InsertingImpl)) {
            return false;
        }
        final InsertingImpl that = (InsertingImpl)other;
        return this.allowsChildren == that.allowsChildren && Objects.equals((Object)this.value, (Object)that.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("allowsChildren", this.allowsChildren), ExaminableProperty.of("value", this.value) });
    }
}
