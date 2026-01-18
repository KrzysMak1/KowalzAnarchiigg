package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface ByteArrayBinaryTag extends ArrayBinaryTag, Iterable<Byte>
{
    @NotNull
    default ByteArrayBinaryTag byteArrayBinaryTag(final byte... value) {
        return new ByteArrayBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default ByteArrayBinaryTag of(final byte... value) {
        return new ByteArrayBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<ByteArrayBinaryTag> type() {
        return BinaryTagTypes.BYTE_ARRAY;
    }
    
    byte[] value();
    
    int size();
    
    byte get(final int index);
}
