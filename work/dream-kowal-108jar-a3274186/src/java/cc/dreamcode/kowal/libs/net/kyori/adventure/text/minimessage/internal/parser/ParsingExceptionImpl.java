package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser;

import java.util.Arrays;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;

@ApiStatus.Internal
public class ParsingExceptionImpl extends ParsingException
{
    private static final long serialVersionUID = 2507190809441787202L;
    private final String originalText;
    private Token[] tokens;
    
    public ParsingExceptionImpl(final String message, @Nullable final String originalText, @NotNull final Token... tokens) {
        super(message, null, true, false);
        this.tokens = tokens;
        this.originalText = originalText;
    }
    
    public ParsingExceptionImpl(final String message, @Nullable final String originalText, @Nullable final Throwable cause, final boolean withStackTrace, @NotNull final Token... tokens) {
        super(message, cause, true, withStackTrace);
        this.tokens = tokens;
        this.originalText = originalText;
    }
    
    public String getMessage() {
        final String arrowInfo = (this.tokens().length != 0) ? ("\n\t" + this.arrow()) : "";
        final String messageInfo = (this.originalText() != null) ? ("\n\t" + this.originalText() + arrowInfo) : "";
        return super.getMessage() + messageInfo;
    }
    
    @Nullable
    @Override
    public String detailMessage() {
        return super.getMessage();
    }
    
    @Nullable
    @Override
    public String originalText() {
        return this.originalText;
    }
    
    @NotNull
    public Token[] tokens() {
        return this.tokens;
    }
    
    public void tokens(@NotNull final Token[] tokens) {
        this.tokens = tokens;
    }
    
    private String arrow() {
        final Token[] ts = this.tokens();
        final char[] chars = new char[ts[ts.length - 1].endIndex()];
        int i = 0;
        for (final Token t : ts) {
            Arrays.fill(chars, i, t.startIndex(), ' ');
            chars[t.startIndex()] = '^';
            if (Math.abs(t.startIndex() - t.endIndex()) > 1) {
                Arrays.fill(chars, t.startIndex() + 1, t.endIndex() - 1, '~');
            }
            chars[t.endIndex() - 1] = '^';
            i = t.endIndex();
        }
        return new String(chars);
    }
    
    @Override
    public int startIndex() {
        if (this.tokens.length == 0) {
            return -1;
        }
        return this.tokens[0].startIndex();
    }
    
    @Override
    public int endIndex() {
        if (this.tokens.length == 0) {
            return -1;
        }
        return this.tokens[this.tokens.length - 1].endIndex();
    }
}
