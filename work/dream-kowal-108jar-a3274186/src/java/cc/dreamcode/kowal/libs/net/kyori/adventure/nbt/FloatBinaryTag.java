package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface FloatBinaryTag extends NumberBinaryTag
{
    @NotNull
    default FloatBinaryTag floatBinaryTag(final float value) {
        return new FloatBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default FloatBinaryTag of(final float value) {
        return new FloatBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<FloatBinaryTag> type() {
        return BinaryTagTypes.FLOAT;
    }
    
    float value();
}
