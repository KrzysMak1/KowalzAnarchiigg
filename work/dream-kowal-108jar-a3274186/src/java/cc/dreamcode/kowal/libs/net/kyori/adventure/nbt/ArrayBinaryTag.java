package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface ArrayBinaryTag extends BinaryTag
{
    @NotNull
    BinaryTagType<? extends ArrayBinaryTag> type();
}
