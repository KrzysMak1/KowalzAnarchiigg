package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public interface CompoundTagSetter<R>
{
    @NotNull
    R put(@NotNull final String key, @NotNull final BinaryTag tag);
    
    @NotNull
    R put(@NotNull final CompoundBinaryTag tag);
    
    @NotNull
    R put(@NotNull final Map<String, ? extends BinaryTag> tags);
    
    @NotNull
    default R remove(@NotNull final String key) {
        return this.remove(key, null);
    }
    
    @NotNull
    R remove(@NotNull final String key, @Nullable final Consumer<? super BinaryTag> removed);
    
    @NotNull
    default R putBoolean(@NotNull final String key, final boolean value) {
        return this.put(key, value ? ByteBinaryTag.ONE : ByteBinaryTag.ZERO);
    }
    
    @NotNull
    default R putByte(@NotNull final String key, final byte value) {
        return this.put(key, ByteBinaryTag.byteBinaryTag(value));
    }
    
    @NotNull
    default R putShort(@NotNull final String key, final short value) {
        return this.put(key, ShortBinaryTag.shortBinaryTag(value));
    }
    
    @NotNull
    default R putInt(@NotNull final String key, final int value) {
        return this.put(key, IntBinaryTag.intBinaryTag(value));
    }
    
    @NotNull
    default R putLong(@NotNull final String key, final long value) {
        return this.put(key, LongBinaryTag.longBinaryTag(value));
    }
    
    @NotNull
    default R putFloat(@NotNull final String key, final float value) {
        return this.put(key, FloatBinaryTag.floatBinaryTag(value));
    }
    
    @NotNull
    default R putDouble(@NotNull final String key, final double value) {
        return this.put(key, DoubleBinaryTag.doubleBinaryTag(value));
    }
    
    @NotNull
    default R putByteArray(@NotNull final String key, final byte[] value) {
        return this.put(key, ByteArrayBinaryTag.byteArrayBinaryTag(value));
    }
    
    @NotNull
    default R putString(@NotNull final String key, @NotNull final String value) {
        return this.put(key, StringBinaryTag.stringBinaryTag(value));
    }
    
    @NotNull
    default R putIntArray(@NotNull final String key, final int[] value) {
        return this.put(key, IntArrayBinaryTag.intArrayBinaryTag(value));
    }
    
    @NotNull
    default R putLongArray(@NotNull final String key, final long[] value) {
        return this.put(key, LongArrayBinaryTag.longArrayBinaryTag(value));
    }
}
