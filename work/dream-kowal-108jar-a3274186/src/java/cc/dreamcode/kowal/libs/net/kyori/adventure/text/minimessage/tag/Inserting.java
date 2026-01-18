package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;

public interface Inserting extends Tag
{
    @NotNull
    Component value();
    
    default boolean allowsChildren() {
        return true;
    }
}
