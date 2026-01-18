package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface LongBinaryTag extends NumberBinaryTag
{
    @NotNull
    default LongBinaryTag longBinaryTag(final long value) {
        return new LongBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default LongBinaryTag of(final long value) {
        return new LongBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<LongBinaryTag> type() {
        return BinaryTagTypes.LONG;
    }
    
    long value();
}
