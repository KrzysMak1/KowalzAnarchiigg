package cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextColor;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.TextDecoration;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.format.NamedTextColor;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public final class LegacyFormat implements Examinable
{
    static final LegacyFormat RESET;
    @Nullable
    private final NamedTextColor color;
    @Nullable
    private final TextDecoration decoration;
    private final boolean reset;
    
    LegacyFormat(@Nullable final NamedTextColor color) {
        this.color = color;
        this.decoration = null;
        this.reset = false;
    }
    
    LegacyFormat(@Nullable final TextDecoration decoration) {
        this.color = null;
        this.decoration = decoration;
        this.reset = false;
    }
    
    private LegacyFormat(final boolean reset) {
        this.color = null;
        this.decoration = null;
        this.reset = reset;
    }
    
    @Nullable
    public TextColor color() {
        return this.color;
    }
    
    @Nullable
    public TextDecoration decoration() {
        return this.decoration;
    }
    
    public boolean reset() {
        return this.reset;
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final LegacyFormat that = (LegacyFormat)other;
        return this.color == that.color && this.decoration == that.decoration && this.reset == that.reset;
    }
    
    @Override
    public int hashCode() {
        int result = Objects.hashCode((Object)this.color);
        result = 31 * result + Objects.hashCode((Object)this.decoration);
        result = 31 * result + Boolean.hashCode(this.reset);
        return result;
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("color", this.color), ExaminableProperty.of("decoration", this.decoration), ExaminableProperty.of("reset", this.reset) });
    }
    
    static {
        RESET = new LegacyFormat(true);
    }
}
