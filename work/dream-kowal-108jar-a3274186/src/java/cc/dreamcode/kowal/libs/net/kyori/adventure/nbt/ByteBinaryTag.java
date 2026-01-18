package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface ByteBinaryTag extends NumberBinaryTag
{
    public static final ByteBinaryTag ZERO = new ByteBinaryTagImpl((byte)0);
    public static final ByteBinaryTag ONE = new ByteBinaryTagImpl((byte)1);
    
    @NotNull
    default ByteBinaryTag byteBinaryTag(final byte value) {
        if (value == 0) {
            return ByteBinaryTag.ZERO;
        }
        if (value == 1) {
            return ByteBinaryTag.ONE;
        }
        return new ByteBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default ByteBinaryTag of(final byte value) {
        return byteBinaryTag(value);
    }
    
    @NotNull
    default BinaryTagType<ByteBinaryTag> type() {
        return BinaryTagTypes.BYTE;
    }
    
    byte value();
}
