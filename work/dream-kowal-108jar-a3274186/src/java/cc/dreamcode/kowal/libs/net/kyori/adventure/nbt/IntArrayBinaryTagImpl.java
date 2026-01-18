package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import java.util.function.IntConsumer;
import org.jetbrains.annotations.NotNull;
import java.util.stream.IntStream;
import java.util.Spliterator;
import java.util.NoSuchElementException;
import java.util.PrimitiveIterator;
import java.util.Arrays;
import org.jetbrains.annotations.Debug;

@Debug.Renderer(text = "\"int[\" + this.value.length + \"]\"", childrenArray = "this.value", hasChildren = "this.value.length > 0")
final class IntArrayBinaryTagImpl extends ArrayBinaryTagImpl implements IntArrayBinaryTag
{
    final int[] value;
    
    IntArrayBinaryTagImpl(final int... value) {
        this.value = Arrays.copyOf(value, value.length);
    }
    
    @Override
    public int[] value() {
        return Arrays.copyOf(this.value, this.value.length);
    }
    
    @Override
    public int size() {
        return this.value.length;
    }
    
    @Override
    public int get(final int index) {
        ArrayBinaryTagImpl.checkIndex(index, this.value.length);
        return this.value[index];
    }
    
    @Override
    public PrimitiveIterator.OfInt iterator() {
        return (PrimitiveIterator.OfInt)new PrimitiveIterator.OfInt() {
            private int index;
            
            public boolean hasNext() {
                return this.index < IntArrayBinaryTagImpl.this.value.length - 1;
            }
            
            public int nextInt() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                return IntArrayBinaryTagImpl.this.value[this.index++];
            }
        };
    }
    
    @Override
    public Spliterator.OfInt spliterator() {
        return Arrays.spliterator(this.value);
    }
    
    @NotNull
    @Override
    public IntStream stream() {
        return Arrays.stream(this.value);
    }
    
    @Override
    public void forEachInt(@NotNull final IntConsumer action) {
        for (int i = 0, length = this.value.length; i < length; ++i) {
            action.accept(this.value[i]);
        }
    }
    
    static int[] value(final IntArrayBinaryTag tag) {
        return (tag instanceof IntArrayBinaryTagImpl) ? ((IntArrayBinaryTagImpl)tag).value : tag.value();
    }
    
    @Override
    public boolean equals(@Nullable final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || this.getClass() != other.getClass()) {
            return false;
        }
        final IntArrayBinaryTagImpl that = (IntArrayBinaryTagImpl)other;
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
}
