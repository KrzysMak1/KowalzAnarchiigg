package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.match;

import org.jetbrains.annotations.MustBeInvokedByOverriders;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.NotNull;

public abstract class MatchedTokenConsumer<T>
{
    protected final String input;
    private int lastIndex;
    
    public MatchedTokenConsumer(@NotNull final String input) {
        this.lastIndex = -1;
        this.input = input;
    }
    
    @MustBeInvokedByOverriders
    public void accept(final int start, final int end, @NotNull final TokenType tokenType) {
        this.lastIndex = end;
    }
    
    public abstract T result();
    
    public final int lastEndIndex() {
        return this.lastIndex;
    }
}
