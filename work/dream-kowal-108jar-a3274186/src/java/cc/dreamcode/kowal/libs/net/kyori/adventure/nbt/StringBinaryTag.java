package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface StringBinaryTag extends BinaryTag
{
    @NotNull
    default StringBinaryTag stringBinaryTag(@NotNull final String value) {
        return new StringBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default StringBinaryTag of(@NotNull final String value) {
        return new StringBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<StringBinaryTag> type() {
        return BinaryTagTypes.STRING;
    }
    
    @NotNull
    String value();
}
