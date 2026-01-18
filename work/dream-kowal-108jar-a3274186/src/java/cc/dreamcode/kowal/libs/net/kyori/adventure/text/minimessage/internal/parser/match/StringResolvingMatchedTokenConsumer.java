package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.match;

import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.PreProcess;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;

public final class StringResolvingMatchedTokenConsumer extends MatchedTokenConsumer<String>
{
    private final StringBuilder builder;
    private final TokenParser.TagProvider tagProvider;
    
    public StringResolvingMatchedTokenConsumer(@NotNull final String input, @NotNull final TokenParser.TagProvider tagProvider) {
        super(input);
        this.builder = new StringBuilder(input.length());
        this.tagProvider = tagProvider;
    }
    
    @Override
    public void accept(final int start, final int end, @NotNull final TokenType tokenType) {
        super.accept(start, end, tokenType);
        if (tokenType != TokenType.OPEN_TAG) {
            this.builder.append((CharSequence)this.input, start, end);
        }
        else {
            final String match = this.input.substring(start, end);
            final String cleanup = this.input.substring(start + 1, end - 1);
            final int index = cleanup.indexOf(58);
            final String tag = (index == -1) ? cleanup : cleanup.substring(0, index);
            if (TagInternals.sanitizeAndCheckValidTagName(tag)) {
                final List<Token> tokens = TokenParser.tokenize(match, false);
                final List<TagPart> parts = (List<TagPart>)new ArrayList();
                final List<Token> childs = tokens.isEmpty() ? null : ((Token)tokens.get(0)).childTokens();
                if (childs != null) {
                    for (int i = 1; i < childs.size(); ++i) {
                        parts.add((Object)new TagPart(match, (Token)childs.get(i), this.tagProvider));
                    }
                }
                final Tag replacement = this.tagProvider.resolve(TokenParser.TagProvider.sanitizePlaceholderName(tag), parts, (Token)tokens.get(0));
                if (replacement instanceof PreProcess) {
                    this.builder.append((String)Objects.requireNonNull((Object)((PreProcess)replacement).value(), "PreProcess replacements cannot return null"));
                    return;
                }
            }
            this.builder.append(match);
        }
    }
    
    @NotNull
    @Override
    public String result() {
        return this.builder.toString();
    }
}
