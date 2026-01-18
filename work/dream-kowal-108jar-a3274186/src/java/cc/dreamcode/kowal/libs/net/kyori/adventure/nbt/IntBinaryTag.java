package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface IntBinaryTag extends NumberBinaryTag
{
    @NotNull
    default IntBinaryTag intBinaryTag(final int value) {
        return new IntBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default IntBinaryTag of(final int value) {
        return new IntBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<IntBinaryTag> type() {
        return BinaryTagTypes.INT;
    }
    
    int value();
}
