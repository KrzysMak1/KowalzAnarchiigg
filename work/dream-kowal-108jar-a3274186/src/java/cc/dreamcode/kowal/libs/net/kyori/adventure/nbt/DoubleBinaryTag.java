package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface DoubleBinaryTag extends NumberBinaryTag
{
    @NotNull
    default DoubleBinaryTag doubleBinaryTag(final double value) {
        return new DoubleBinaryTagImpl(value);
    }
    
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "5.0.0")
    @NotNull
    default DoubleBinaryTag of(final double value) {
        return new DoubleBinaryTagImpl(value);
    }
    
    @NotNull
    default BinaryTagType<DoubleBinaryTag> type() {
        return BinaryTagTypes.DOUBLE;
    }
    
    double value();
}
