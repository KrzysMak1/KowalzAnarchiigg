package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface ShortBinaryTag extends NumberBinaryTag
{
    @NotNull
    default ShortBinaryTag shortBinaryTag(final short value) {
        return new ShortBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default ShortBinaryTag of(final short value) {
        return new ShortBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<ShortBinaryTag> type() {
        return BinaryTagTypes.SHORT;
    }
    
    short value();
}
