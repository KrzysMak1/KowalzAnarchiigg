package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser;

import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.Internals;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public final class Token implements Examinable
{
    private final int startIndex;
    private final int endIndex;
    private final TokenType type;
    private List<Token> childTokens;
    
    public Token(final int startIndex, final int endIndex, final TokenType type) {
        this.childTokens = null;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.type = type;
    }
    
    public int startIndex() {
        return this.startIndex;
    }
    
    public int endIndex() {
        return this.endIndex;
    }
    
    public TokenType type() {
        return this.type;
    }
    
    public List<Token> childTokens() {
        return this.childTokens;
    }
    
    public void childTokens(final List<Token> childTokens) {
        this.childTokens = childTokens;
    }
    
    public CharSequence get(final CharSequence message) {
        return message.subSequence(this.startIndex, this.endIndex);
    }
    
    @NotNull
    @Override
    public Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("startIndex", this.startIndex), ExaminableProperty.of("endIndex", this.endIndex), ExaminableProperty.of("type", this.type) });
    }
    
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Token)) {
            return false;
        }
        final Token that = (Token)other;
        return this.startIndex == that.startIndex && this.endIndex == that.endIndex && this.type == that.type;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(new Object[] { this.startIndex, this.endIndex, this.type });
    }
    
    @Override
    public String toString() {
        return Internals.toString(this);
    }
}
