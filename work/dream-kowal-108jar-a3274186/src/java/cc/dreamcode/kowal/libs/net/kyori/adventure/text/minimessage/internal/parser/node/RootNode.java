package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;

public final class RootNode extends ElementNode implements Node.Root
{
    private final String beforePreprocessing;
    
    public RootNode(@NotNull final String sourceMessage, @NotNull final String beforePreprocessing) {
        super(null, null, sourceMessage);
        this.beforePreprocessing = beforePreprocessing;
    }
    
    @NotNull
    @Override
    public String input() {
        return this.beforePreprocessing;
    }
}
