package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node;

import java.util.Objects;
import java.util.Iterator;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.List;

public final class TagNode extends ElementNode
{
    private final List<TagPart> parts;
    @Nullable
    private Tag tag;
    
    public TagNode(@NotNull final ElementNode parent, @NotNull final Token token, @NotNull final String sourceMessage, final TokenParser.TagProvider tagProvider) {
        super(parent, token, sourceMessage);
        this.tag = null;
        this.parts = genParts(token, sourceMessage, tagProvider);
        if (this.parts.isEmpty()) {
            throw new ParsingExceptionImpl("Tag has no parts? " + (Object)this, this.sourceMessage(), new Token[] { this.token() });
        }
    }
    
    @NotNull
    private static List<TagPart> genParts(@NotNull final Token token, @NotNull final String sourceMessage, final TokenParser.TagProvider tagProvider) {
        final ArrayList<TagPart> parts = (ArrayList<TagPart>)new ArrayList();
        if (token.childTokens() != null) {
            for (final Token childToken : token.childTokens()) {
                parts.add((Object)new TagPart(sourceMessage, childToken, tagProvider));
            }
        }
        return (List<TagPart>)parts;
    }
    
    @NotNull
    public List<TagPart> parts() {
        return this.parts;
    }
    
    @NotNull
    public String name() {
        return ((TagPart)this.parts.get(0)).value();
    }
    
    @NotNull
    @Override
    public Token token() {
        return (Token)Objects.requireNonNull((Object)super.token(), "token is not set");
    }
    
    @NotNull
    public Tag tag() {
        return (Tag)Objects.requireNonNull((Object)this.tag, "no tag set");
    }
    
    public void tag(@NotNull final Tag tag) {
        this.tag = tag;
    }
    
    @NotNull
    @Override
    public StringBuilder buildToString(@NotNull final StringBuilder sb, final int indent) {
        final char[] in = this.ident(indent);
        sb.append(in).append("TagNode(");
        for (int size = this.parts.size(), i = 0; i < size; ++i) {
            final TagPart part = (TagPart)this.parts.get(i);
            sb.append('\'').append(part.value()).append('\'');
            if (i != size - 1) {
                sb.append(", ");
            }
        }
        sb.append(") {\n");
        for (final ElementNode child : this.children()) {
            child.buildToString(sb, indent + 1);
        }
        sb.append(in).append("}\n");
        return sb;
    }
}
