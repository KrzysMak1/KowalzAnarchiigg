package cc.dreamcode.kowal.libs.net.kyori.adventure.nbt;

import org.jetbrains.annotations.NotNull;

public interface NumberBinaryTag extends BinaryTag
{
    @NotNull
    BinaryTagType<? extends NumberBinaryTag> type();
    
    byte byteValue();
    
    double doubleValue();
    
    float floatValue();
    
    int intValue();
    
    long longValue();
    
    short shortValue();
}
