package cc.dreamcode.kowal.libs.net.kyori.adventure.text.event;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.api.BinaryTagHolder;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface DataComponentValue extends Examinable
{
    default Removed removed() {
        return RemovedDataComponentValueImpl.REMOVED;
    }
    
    public interface Removed extends DataComponentValue
    {
    }
    
    public interface TagSerializable extends DataComponentValue
    {
        @NotNull
        BinaryTagHolder asBinaryTag();
    }
}
