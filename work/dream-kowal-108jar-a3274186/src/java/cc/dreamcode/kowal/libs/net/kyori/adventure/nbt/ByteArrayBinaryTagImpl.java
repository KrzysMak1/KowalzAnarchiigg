package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"byte[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
final class ByteArrayBinaryTagImpl extends ArrayBinaryTagImpl implements ByteArrayBinaryTag
{
    final byte[] value;
    
    ByteArrayBinaryTagImpl(final byte[] value) {
        this.value = Arrays.copyOf(value, value.length);
    }
    
    @Override
    public byte[] value() {
        return Arrays.copyOf(this.value, this.value.length);
    }
    
    @Override
    public int size() {
        return this.value.length;
    }
    
    @Override
    public byte get(final int index) {
        ArrayBinaryTagImpl.checkIndex(index, this.value.length);
        return this.value[index];
    }
    
    static byte[] value(final ByteArrayBinaryTag tag) {
        return (tag instanceof ByteArrayBinaryTagImpl) ? ((ByteArrayBinaryTagImpl)tag).value : tag.value();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final ByteArrayBinaryTagImpl that = (ByteArrayBinaryTagImpl)other;
        return Arrays.equals(this.value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.value);
    }
    
    @NotNull
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object)ExaminableProperty.of("value", this.value));
    }
    
    public Iterator<Byte> iterator() {
        return (Iterator<Byte>)new Iterator<Byte>() {
            private int index;
            
            public boolean hasNext() {
                return this.index < ByteArrayBinaryTagImpl.this.value.length - 1;
            }
            
            public Byte next() {
                return ByteArrayBinaryTagImpl.this.value[this.index++];
            }
        };
    }
}
