package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser;

import java.util.Locale;
import org.jetbrains.annotations.Nullable;
import java.util.function.IntPredicate;
import java.util.Collections;
import java.util.ListIterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.ParserDirective;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TextNode;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.match.TokenListProducingMatchedTokenConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.match.MatchedTokenConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.match.StringResolvingMatchedTokenConsumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import java.util.function.Predicate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public final class TokenParser
{
    private static final int MAX_DEPTH = 16;
    public static final char TAG_START = '<';
    public static final char TAG_END = '>';
    public static final char CLOSE_TAG = '/';
    public static final char SEPARATOR = ':';
    public static final char ESCAPE = '\\';
    
    private TokenParser() {
    }
    
    public static RootNode parse(@NotNull final TagProvider tagProvider, @NotNull final Predicate<String> tagNameChecker, @NotNull final String message, @NotNull final String originalMessage, final boolean strict) throws ParsingException {
        final List<Token> tokens = tokenize(message, false);
        return buildTree(tagProvider, tagNameChecker, tokens, message, originalMessage, strict);
    }
    
    public static String resolvePreProcessTags(final String message, final TagProvider provider) {
        int passes = 0;
        String result = message;
        String lastResult;
        do {
            lastResult = result;
            final StringResolvingMatchedTokenConsumer stringTokenResolver = new StringResolvingMatchedTokenConsumer(lastResult, provider);
            parseString(lastResult, false, stringTokenResolver);
            result = stringTokenResolver.result();
        } while (++passes < 16 && !lastResult.equals((Object)result));
        return lastResult;
    }
    
    public static List<Token> tokenize(final String message, final boolean lenient) {
        final TokenListProducingMatchedTokenConsumer listProducer = new TokenListProducingMatchedTokenConsumer(message);
        parseString(message, lenient, listProducer);
        final List<Token> tokens = listProducer.result();
        parseSecondPass(message, tokens);
        return tokens;
    }
    
    public static void parseString(final String message, final boolean lenient, final MatchedTokenConsumer<?> consumer) {
        FirstPassState state = FirstPassState.NORMAL;
        boolean escaped = false;
        int currentTokenEnd = 0;
        int marker = -1;
        char currentStringChar = '\0';
        for (int length = message.length(), i = 0; i < length; ++i) {
            final int codePoint = message.codePointAt(i);
            if (!lenient && codePoint == 167 && i + 1 < length) {
                final int nextChar = Character.toLowerCase(message.codePointAt(i + 1));
                if ((nextChar >= 48 && nextChar <= 57) || (nextChar >= 97 && nextChar <= 102) || nextChar == 114 || (nextChar >= 107 && nextChar <= 111)) {
                    throw new ParsingExceptionImpl("Legacy formatting codes have been detected in a MiniMessage string - this is unsupported behaviour. Please refer to the Adventure documentation (https://docs.advntr.dev) for more information.", message, null, true, new Token[] { new Token(i, i + 2, TokenType.TEXT) });
                }
            }
            if (!Character.isBmpCodePoint(codePoint)) {
                ++i;
            }
            if (!escaped) {
                if (codePoint == 92 && i + 1 < message.length()) {
                    final int nextCodePoint = message.codePointAt(i + 1);
                    switch (state) {
                        case NORMAL: {
                            escaped = (nextCodePoint == 60 || nextCodePoint == 92);
                            break;
                        }
                        case STRING: {
                            escaped = (currentStringChar == nextCodePoint || nextCodePoint == 92);
                            break;
                        }
                        case TAG: {
                            if (nextCodePoint == 60) {
                                escaped = true;
                                state = FirstPassState.NORMAL;
                                break;
                            }
                            break;
                        }
                    }
                    if (escaped) {
                        continue;
                    }
                }
                switch (state) {
                    case NORMAL: {
                        if (codePoint == 60) {
                            marker = i;
                            state = FirstPassState.TAG;
                            break;
                        }
                        break;
                    }
                    case TAG: {
                        switch (codePoint) {
                            case 62: {
                                if (i == marker + 1) {
                                    state = FirstPassState.NORMAL;
                                    break;
                                }
                                if (currentTokenEnd != marker) {
                                    consumer.accept(currentTokenEnd, marker, TokenType.TEXT);
                                }
                                currentTokenEnd = i + 1;
                                TokenType thisType = TokenType.OPEN_TAG;
                                if (boundsCheck(message, marker, 1) && message.charAt(marker + 1) == '/') {
                                    thisType = TokenType.CLOSE_TAG;
                                }
                                else if (boundsCheck(message, marker, 2) && message.charAt(i - 1) == '/') {
                                    thisType = TokenType.OPEN_CLOSE_TAG;
                                }
                                consumer.accept(marker, currentTokenEnd, thisType);
                                state = FirstPassState.NORMAL;
                                break;
                            }
                            case 60: {
                                marker = i;
                                break;
                            }
                            case 34:
                            case 39: {
                                currentStringChar = (char)codePoint;
                                if (message.indexOf(codePoint, i + 1) != -1) {
                                    state = FirstPassState.STRING;
                                    break;
                                }
                                break;
                            }
                        }
                        break;
                    }
                    case STRING: {
                        if (codePoint == currentStringChar) {
                            state = FirstPassState.TAG;
                            break;
                        }
                        break;
                    }
                }
                if (i == length - 1 && state == FirstPassState.TAG) {
                    i = marker;
                    state = FirstPassState.NORMAL;
                }
            }
            else {
                escaped = false;
            }
        }
        final int end = consumer.lastEndIndex();
        if (end == -1) {
            consumer.accept(0, message.length(), TokenType.TEXT);
        }
        else if (end != message.length()) {
            consumer.accept(end, message.length(), TokenType.TEXT);
        }
    }
    
    private static void parseSecondPass(final String message, final List<Token> tokens) {
        for (final Token token : tokens) {
            final TokenType type = token.type();
            if (type != TokenType.OPEN_TAG && type != TokenType.OPEN_CLOSE_TAG && type != TokenType.CLOSE_TAG) {
                continue;
            }
            final int startIndex = (type == TokenType.CLOSE_TAG) ? (token.startIndex() + 2) : (token.startIndex() + 1);
            final int endIndex = (type == TokenType.OPEN_CLOSE_TAG) ? (token.endIndex() - 2) : (token.endIndex() - 1);
            SecondPassState state = SecondPassState.NORMAL;
            boolean escaped = false;
            char currentStringChar = '\0';
            int marker = startIndex;
            for (int i = startIndex; i < endIndex; ++i) {
                final int codePoint = message.codePointAt(i);
                if (!Character.isBmpCodePoint(i)) {
                    ++i;
                }
                if (!escaped) {
                    if (codePoint == 92 && i + 1 < message.length()) {
                        final int nextCodePoint = message.codePointAt(i + 1);
                        switch (state) {
                            case NORMAL: {
                                escaped = (nextCodePoint == 60 || nextCodePoint == 92);
                                break;
                            }
                            case STRING: {
                                escaped = (currentStringChar == nextCodePoint || nextCodePoint == 92);
                                break;
                            }
                        }
                        if (escaped) {
                            continue;
                        }
                    }
                    switch (state) {
                        case NORMAL: {
                            if (codePoint == 58) {
                                if (boundsCheck(message, i, 2) && message.charAt(i + 1) == '/' && message.charAt(i + 2) == '/') {
                                    break;
                                }
                                if (marker == i) {
                                    insert(token, new Token(i, i, TokenType.TAG_VALUE));
                                    ++marker;
                                    break;
                                }
                                insert(token, new Token(marker, i, TokenType.TAG_VALUE));
                                marker = i + 1;
                                break;
                            }
                            else {
                                if (codePoint == 39 || codePoint == 34) {
                                    state = SecondPassState.STRING;
                                    currentStringChar = (char)codePoint;
                                    break;
                                }
                                break;
                            }
                            break;
                        }
                        case STRING: {
                            if (codePoint == currentStringChar) {
                                state = SecondPassState.NORMAL;
                                break;
                            }
                            break;
                        }
                    }
                }
                else {
                    escaped = false;
                }
            }
            if (token.childTokens() == null || token.childTokens().isEmpty()) {
                insert(token, new Token(startIndex, endIndex, TokenType.TAG_VALUE));
            }
            else {
                final int end = ((Token)token.childTokens().get(token.childTokens().size() - 1)).endIndex();
                if (end == endIndex) {
                    continue;
                }
                insert(token, new Token(end + 1, endIndex, TokenType.TAG_VALUE));
            }
        }
    }
    
    private static RootNode buildTree(@NotNull final TagProvider tagProvider, @NotNull final Predicate<String> tagNameChecker, @NotNull final List<Token> tokens, @NotNull final String message, @NotNull final String originalMessage, final boolean strict) throws ParsingException {
        ElementNode node;
        final RootNode root = (RootNode)(node = new RootNode(message, originalMessage));
        for (final Token token : tokens) {
            final TokenType type = token.type();
            switch (type) {
                case TEXT: {
                    node.addChild(new TextNode(node, token, message));
                    continue;
                }
                case OPEN_TAG:
                case OPEN_CLOSE_TAG: {
                    final Token tagNamePart = (Token)token.childTokens().get(0);
                    final String tagName = message.substring(tagNamePart.startIndex(), tagNamePart.endIndex());
                    if (!TagInternals.sanitizeAndCheckValidTagName(tagName)) {
                        node.addChild(new TextNode(node, token, message));
                        continue;
                    }
                    final TagNode tagNode = new TagNode(node, token, message, tagProvider);
                    if (tagNameChecker.test((Object)tagNode.name())) {
                        final Tag tag = tagProvider.resolve(tagNode);
                        if (tag == null) {
                            node.addChild(new TextNode(node, token, message));
                        }
                        else if (tag == ParserDirective.RESET) {
                            if (strict) {
                                throw new ParsingExceptionImpl("<reset> tags are not allowed when strict mode is enabled", message, new Token[] { token });
                            }
                            node = root;
                        }
                        else {
                            tagNode.tag(tag);
                            node.addChild(tagNode);
                            if (type == TokenType.OPEN_CLOSE_TAG || (tag instanceof Inserting && !((Inserting)tag).allowsChildren())) {
                                continue;
                            }
                            node = tagNode;
                        }
                        continue;
                    }
                    node.addChild(new TextNode(node, token, message));
                    continue;
                }
                case CLOSE_TAG: {
                    final List<Token> childTokens = token.childTokens();
                    if (childTokens.isEmpty()) {
                        throw new IllegalStateException("CLOSE_TAG token somehow has no children - the parser should not allow this. Original text: " + message);
                    }
                    final ArrayList<String> closeValues = (ArrayList<String>)new ArrayList(childTokens.size());
                    for (final Token childToken : childTokens) {
                        closeValues.add((Object)TagPart.unquoteAndEscape(message, childToken.startIndex(), childToken.endIndex()));
                    }
                    final String closeTagName = (String)closeValues.get(0);
                    if (!tagNameChecker.test((Object)closeTagName)) {
                        node.addChild(new TextNode(node, token, message));
                        continue;
                    }
                    final Tag tag2 = tagProvider.resolve(closeTagName);
                    if (tag2 == ParserDirective.RESET) {
                        continue;
                    }
                    ElementNode parentNode = node;
                    while (parentNode instanceof TagNode) {
                        final List<TagPart> openParts = ((TagNode)parentNode).parts();
                        if (tagCloses((List<String>)closeValues, openParts)) {
                            if (parentNode != node && strict) {
                                final String msg = "Unclosed tag encountered; " + ((TagNode)node).name() + " is not closed, because " + (String)closeValues.get(0) + " was closed first.";
                                throw new ParsingExceptionImpl(msg, message, new Token[] { parentNode.token(), node.token(), token });
                            }
                            final ElementNode par = parentNode.parent();
                            if (par != null) {
                                node = par;
                                break;
                            }
                            throw new IllegalStateException("Root node matched with close tag value, this should not be possible. Original text: " + message);
                        }
                        else {
                            parentNode = parentNode.parent();
                        }
                    }
                    if (parentNode == null || parentNode instanceof RootNode) {
                        node.addChild(new TextNode(node, token, message));
                        continue;
                    }
                    continue;
                }
            }
        }
        if (strict && root != node) {
            final ArrayList<TagNode> openTags = (ArrayList<TagNode>)new ArrayList();
            for (ElementNode n = node; n != null && n instanceof TagNode; n = n.parent()) {
                openTags.add((Object)n);
            }
            final Token[] errorTokens = new Token[openTags.size()];
            final StringBuilder sb = new StringBuilder("All tags must be explicitly closed while in strict mode. End of string found with open tags: ");
            int i = 0;
            final ListIterator<TagNode> iter = (ListIterator<TagNode>)openTags.listIterator(openTags.size());
            while (iter.hasPrevious()) {
                final TagNode n2 = (TagNode)iter.previous();
                errorTokens[i++] = n2.token();
                sb.append(n2.name());
                if (iter.hasPrevious()) {
                    sb.append(", ");
                }
            }
            throw new ParsingExceptionImpl(sb.toString(), message, errorTokens);
        }
        return root;
    }
    
    private static boolean tagCloses(final List<String> closeParts, final List<TagPart> openParts) {
        if (closeParts.size() > openParts.size()) {
            return false;
        }
        if (!((String)closeParts.get(0)).equalsIgnoreCase(((TagPart)openParts.get(0)).value())) {
            return false;
        }
        for (int i = 1; i < closeParts.size(); ++i) {
            if (!((String)closeParts.get(i)).equals((Object)((TagPart)openParts.get(i)).value())) {
                return false;
            }
        }
        return true;
    }
    
    private static boolean boundsCheck(final String text, final int index, final int length) {
        return index + length < text.length();
    }
    
    private static void insert(final Token token, final Token value) {
        if (token.childTokens() == null) {
            token.childTokens((List<Token>)Collections.singletonList((Object)value));
            return;
        }
        if (token.childTokens().size() == 1) {
            final ArrayList<Token> list = (ArrayList<Token>)new ArrayList(3);
            list.add((Object)token.childTokens().get(0));
            list.add((Object)value);
            token.childTokens((List<Token>)list);
        }
        else {
            token.childTokens().add((Object)value);
        }
    }
    
    public static String unescape(final String text, final int startIndex, final int endIndex, final IntPredicate escapes) {
        int from = startIndex;
        int i = text.indexOf(92, from);
        if (i == -1 || i >= endIndex) {
            return text.substring(from, endIndex);
        }
        final StringBuilder sb = new StringBuilder(endIndex - startIndex);
        while (i != -1 && i + 1 < endIndex) {
            if (escapes.test(text.codePointAt(i + 1))) {
                sb.append((CharSequence)text, from, i);
                if (++i >= endIndex) {
                    from = endIndex;
                    break;
                }
                final int codePoint = text.codePointAt(i);
                sb.appendCodePoint(codePoint);
                if (Character.isBmpCodePoint(codePoint)) {
                    ++i;
                }
                else {
                    i += 2;
                }
                if (i >= endIndex) {
                    from = endIndex;
                    break;
                }
            }
            else {
                ++i;
                sb.append((CharSequence)text, from, i);
            }
            from = i;
            i = text.indexOf(92, from);
        }
        sb.append((CharSequence)text, from, endIndex);
        return sb.toString();
    }
    
    enum FirstPassState
    {
        NORMAL, 
        TAG, 
        STRING;
    }
    
    enum SecondPassState
    {
        NORMAL, 
        STRING;
    }
    
    @ApiStatus.Internal
    public interface TagProvider
    {
        @Nullable
        Tag resolve(@NotNull final String name, @NotNull final List<? extends Tag.Argument> trimmedArgs, @Nullable final Token token);
        
        @Nullable
        default Tag resolve(@NotNull final String name) {
            return this.resolve(name, (List<? extends Tag.Argument>)Collections.emptyList(), null);
        }
        
        @Nullable
        default Tag resolve(@NotNull final TagNode node) {
            return this.resolve(sanitizePlaceholderName(node.name()), (List<? extends Tag.Argument>)node.parts().subList(1, node.parts().size()), node.token());
        }
        
        @NotNull
        default String sanitizePlaceholderName(@NotNull final String name) {
            return name.toLowerCase(Locale.ROOT);
        }
    }
}
