package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.match;

import java.util.Collections;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import java.util.List;

public final class TokenListProducingMatchedTokenConsumer extends MatchedTokenConsumer<List<Token>>
{
    private List<Token> result;
    
    public TokenListProducingMatchedTokenConsumer(@NotNull final String input) {
        super(input);
        this.result = null;
    }
    
    @Override
    public void accept(final int start, final int end, @NotNull final TokenType tokenType) {
        super.accept(start, end, tokenType);
        if (this.result == null) {
            this.result = (List<Token>)new ArrayList();
        }
        this.result.add((Object)new Token(start, end, tokenType));
    }
    
    @NotNull
    @Override
    public List<Token> result() {
        return (List<Token>)((this.result == null) ? Collections.emptyList() : this.result);
    }
}
