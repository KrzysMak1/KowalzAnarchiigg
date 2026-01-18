package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface EndBinaryTag extends BinaryTag
{
    @NotNull
    default EndBinaryTag endBinaryTag() {
        return EndBinaryTagImpl.INSTANCE;
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default EndBinaryTag get() {
        return EndBinaryTagImpl.INSTANCE;
    }
    
    @NotNull
    default BinaryTagType<EndBinaryTag> type() {
        return BinaryTagTypes.END;
    }
}
