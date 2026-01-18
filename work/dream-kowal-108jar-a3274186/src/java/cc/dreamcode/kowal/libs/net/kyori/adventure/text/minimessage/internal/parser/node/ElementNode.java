package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import java.util.Arrays;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import java.util.Collections;
import java.util.ArrayList;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;

public class ElementNode implements Node
{
    @Nullable
    private final ElementNode parent;
    @Nullable
    private final Token token;
    private final String sourceMessage;
    private final List<ElementNode> children;
    
    ElementNode(@Nullable final ElementNode parent, @Nullable final Token token, @NotNull final String sourceMessage) {
        this.children = (List<ElementNode>)new ArrayList();
        this.parent = parent;
        this.token = token;
        this.sourceMessage = sourceMessage;
    }
    
    @Nullable
    @Override
    public ElementNode parent() {
        return this.parent;
    }
    
    @Nullable
    public Token token() {
        return this.token;
    }
    
    @NotNull
    public String sourceMessage() {
        return this.sourceMessage;
    }
    
    @NotNull
    @Override
    public List<ElementNode> children() {
        return (List<ElementNode>)Collections.unmodifiableList((List)this.children);
    }
    
    @NotNull
    public List<ElementNode> unsafeChildren() {
        return this.children;
    }
    
    public void addChild(@NotNull final ElementNode childNode) {
        final int last = this.children.size() - 1;
        if (!(childNode instanceof TextNode) || this.children.isEmpty() || !(this.children.get(last) instanceof TextNode)) {
            this.children.add((Object)childNode);
        }
        else {
            final TextNode lastNode = (TextNode)this.children.remove(last);
            if (lastNode.token().endIndex() == childNode.token().startIndex()) {
                final Token replace = new Token(lastNode.token().startIndex(), childNode.token().endIndex(), TokenType.TEXT);
                this.children.add((Object)new TextNode(this, replace, lastNode.sourceMessage()));
            }
            else {
                this.children.add((Object)lastNode);
                this.children.add((Object)childNode);
            }
        }
    }
    
    @NotNull
    public StringBuilder buildToString(@NotNull final StringBuilder sb, final int indent) {
        final char[] in = this.ident(indent);
        sb.append(in).append("Node {\n");
        for (final ElementNode child : this.children) {
            child.buildToString(sb, indent + 1);
        }
        sb.append(in).append("}\n");
        return sb;
    }
    
    char[] ident(final int indent) {
        final char[] c = new char[indent * 2];
        Arrays.fill(c, ' ');
        return c;
    }
    
    @NotNull
    @Override
    public String toString() {
        return this.buildToString(new StringBuilder(), 0).toString();
    }
}
