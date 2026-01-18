package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface BinaryTag extends BinaryTagLike, Examinable
{
    @NotNull
    BinaryTagType<? extends BinaryTag> type();
    
    @NotNull
    default BinaryTag asBinaryTag() {
        return this;
    }
}
