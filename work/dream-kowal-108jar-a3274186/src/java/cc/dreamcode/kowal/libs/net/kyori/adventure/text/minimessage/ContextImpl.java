package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagPart;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.UnaryOperator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;

class ContextImpl implements Context
{
    private static final Token[] EMPTY_TOKEN_ARRAY;
    private final boolean strict;
    private final Consumer<String> debugOutput;
    private String message;
    private final MiniMessage miniMessage;
    @Nullable
    private final Pointered target;
    private final TagResolver tagResolver;
    private final UnaryOperator<String> preProcessor;
    private final UnaryOperator<Component> postProcessor;
    
    ContextImpl(final boolean strict, final Consumer<String> debugOutput, final String message, final MiniMessage miniMessage, @Nullable final Pointered target, @Nullable final TagResolver extraTags, @Nullable final UnaryOperator<String> preProcessor, @Nullable final UnaryOperator<Component> postProcessor) {
        this.strict = strict;
        this.debugOutput = debugOutput;
        this.message = message;
        this.miniMessage = miniMessage;
        this.target = target;
        this.tagResolver = ((extraTags == null) ? TagResolver.empty() : extraTags);
        this.preProcessor = (UnaryOperator<String>)((preProcessor == null) ? UnaryOperator.identity() : preProcessor);
        this.postProcessor = (UnaryOperator<Component>)((postProcessor == null) ? UnaryOperator.identity() : postProcessor);
    }
    
    public boolean strict() {
        return this.strict;
    }
    
    public Consumer<String> debugOutput() {
        return this.debugOutput;
    }
    
    @NotNull
    public String message() {
        return this.message;
    }
    
    void message(@NotNull final String message) {
        this.message = message;
    }
    
    @NotNull
    public TagResolver extraTags() {
        return this.tagResolver;
    }
    
    public UnaryOperator<Component> postProcessor() {
        return this.postProcessor;
    }
    
    public UnaryOperator<String> preProcessor() {
        return this.preProcessor;
    }
    
    @Nullable
    @Override
    public Pointered target() {
        return this.target;
    }
    
    @NotNull
    @Override
    public Pointered targetOrThrow() {
        if (this.target == null) {
            throw this.newException("A target is required for this deserialization attempt");
        }
        return this.target;
    }
    
    @NotNull
    @Override
    public <T extends Pointered> T targetAsType(@NotNull final Class<T> targetClass) {
        if (((Class)Objects.requireNonNull((Object)targetClass, "targetClass")).isInstance(this.target)) {
            return targetClass.cast(this.target);
        }
        throw this.newException("A target with type " + targetClass.getSimpleName() + " is required for this deserialization attempt");
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String message) {
        return this.miniMessage.deserialize((String)Objects.requireNonNull((Object)message, "message"), this.tagResolver);
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String message, @NotNull final TagResolver resolver) {
        return this.miniMessage.deserialize((String)Objects.requireNonNull((Object)message, "message"), TagResolver.builder().resolver(this.tagResolver).resolver((TagResolver)Objects.requireNonNull((Object)resolver, "resolver")).build());
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String message, @NotNull final TagResolver... resolvers) {
        return this.miniMessage.deserialize((String)Objects.requireNonNull((Object)message, "message"), TagResolver.builder().resolver(this.tagResolver).resolvers((TagResolver[])Objects.requireNonNull((Object)resolvers, "resolvers")).build());
    }
    
    @NotNull
    @Override
    public ParsingException newException(@NotNull final String message) {
        return new ParsingExceptionImpl(message, this.message, null, false, ContextImpl.EMPTY_TOKEN_ARRAY);
    }
    
    @NotNull
    @Override
    public ParsingException newException(@NotNull final String message, @NotNull final ArgumentQueue tags) {
        return new ParsingExceptionImpl(message, this.message, null, false, tagsToTokens(((ArgumentQueueImpl)tags).args));
    }
    
    @NotNull
    @Override
    public ParsingException newException(@NotNull final String message, @Nullable final Throwable cause, @NotNull final ArgumentQueue tags) {
        return new ParsingExceptionImpl(message, this.message, cause, false, tagsToTokens(((ArgumentQueueImpl)tags).args));
    }
    
    private static Token[] tagsToTokens(final List<? extends Tag.Argument> tags) {
        final Token[] tokens = new Token[tags.size()];
        for (int i = 0, length = tokens.length; i < length; ++i) {
            tokens[i] = ((TagPart)tags.get(i)).token();
        }
        return tokens;
    }
    
    static {
        EMPTY_TOKEN_ARRAY = new Token[0];
    }
}
