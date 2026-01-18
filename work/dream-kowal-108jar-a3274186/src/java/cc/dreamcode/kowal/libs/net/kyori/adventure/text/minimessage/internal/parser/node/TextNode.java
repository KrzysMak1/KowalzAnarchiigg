package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import org.jetbrains.annotations.Nullable;

public final class TextNode extends ValueNode
{
    private static boolean isEscape(final int escape) {
        return escape == 60 || escape == 92;
    }
    
    public TextNode(@Nullable final ElementNode parent, @NotNull final Token token, @NotNull final String sourceMessage) {
        super(parent, token, sourceMessage, TokenParser.unescape(sourceMessage, token.startIndex(), token.endIndex(), TextNode::isEscape));
    }
    
    @Override
    String valueName() {
        return "TextNode";
    }
}
