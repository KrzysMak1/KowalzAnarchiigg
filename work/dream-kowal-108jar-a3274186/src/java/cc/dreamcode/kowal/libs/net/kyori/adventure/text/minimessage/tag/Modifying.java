package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.OverrideOnly
public interface Modifying extends Tag
{
    default void visit(@NotNull final Node current, final int depth) {
    }
    
    default void postVisit() {
    }
    
    Component apply(@NotNull final Component current, final int depth);
}
