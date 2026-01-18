package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "String.valueOf(this.value) + \"f\"", hasChildren = "false")
final class FloatBinaryTagImpl extends AbstractBinaryTag implements FloatBinaryTag
{
    private final float value;
    
    FloatBinaryTagImpl(final float value) {
        this.value = value;
    }
    
    @Override
    public float value() {
        return this.value;
    }
    
    @Override
    public byte byteValue() {
        return (byte)(ShadyPines.floor(this.value) & 0xFF);
    }
    
    @Override
    public double doubleValue() {
        return this.value;
    }
    
    @Override
    public float floatValue() {
        return this.value;
    }
    
    @Override
    public int intValue() {
        return ShadyPines.floor(this.value);
    }
    
    @Override
    public long longValue() {
        return (long)this.value;
    }
    
    @Override
    public short shortValue() {
        return (short)(ShadyPines.floor(this.value) & 0xFFFF);
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final FloatBinaryTagImpl that = (FloatBinaryTagImpl)other;
        return Float.floatToIntBits(this.value) == Float.floatToIntBits(that.value);
    }
    
    @Override
    public int hashCode() {
        return Float.hashCode(this.value);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("value", this.value));
    }
}
