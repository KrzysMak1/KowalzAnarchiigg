package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
public interface Node
{
    @NotNull
    String toString();
    
    @NotNull
    List<? extends Node> children();
    
    @Nullable
    Node parent();
    
    @ApiStatus.NonExtendable
    public interface Root extends Node
    {
        @NotNull
        String input();
    }
}
