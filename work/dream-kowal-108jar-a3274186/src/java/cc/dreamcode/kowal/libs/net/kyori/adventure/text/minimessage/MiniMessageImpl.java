package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import cc.dreamcode.kowal.libs.net.kyori.adventure.util.Services;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer.SerializableResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.UnaryOperator;
import java.util.function.Consumer;
import java.util.Optional;

final class MiniMessageImpl implements MiniMessage
{
    private static final Optional<Provider> SERVICE;
    static final Consumer<Builder> BUILDER;
    static final UnaryOperator<String> DEFAULT_NO_OP;
    static final UnaryOperator<Component> DEFAULT_COMPACTING_METHOD;
    private final boolean strict;
    @Nullable
    private final Consumer<String> debugOutput;
    private final UnaryOperator<Component> postProcessor;
    private final UnaryOperator<String> preProcessor;
    final MiniMessageParser parser;
    
    MiniMessageImpl(@NotNull final TagResolver resolver, final boolean strict, @Nullable final Consumer<String> debugOutput, @NotNull final UnaryOperator<String> preProcessor, @NotNull final UnaryOperator<Component> postProcessor) {
        this.parser = new MiniMessageParser(resolver);
        this.strict = strict;
        this.debugOutput = debugOutput;
        this.preProcessor = preProcessor;
        this.postProcessor = postProcessor;
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String input) {
        return this.parser.parseFormat(this.newContext(input, null, null));
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String input, @NotNull final Pointered target) {
        return this.parser.parseFormat(this.newContext(input, (Pointered)Objects.requireNonNull((Object)target, "target"), null));
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String input, @NotNull final TagResolver tagResolver) {
        return this.parser.parseFormat(this.newContext(input, null, (TagResolver)Objects.requireNonNull((Object)tagResolver, "tagResolver")));
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver) {
        return this.parser.parseFormat(this.newContext(input, (Pointered)Objects.requireNonNull((Object)target, "target"), (TagResolver)Objects.requireNonNull((Object)tagResolver, "tagResolver")));
    }
    
    @Override
    public Node.Root deserializeToTree(@NotNull final String input) {
        return this.parser.parseToTree(this.newContext(input, null, null));
    }
    
    @Override
    public Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target) {
        return this.parser.parseToTree(this.newContext(input, (Pointered)Objects.requireNonNull((Object)target, "target"), null));
    }
    
    @Override
    public Node.Root deserializeToTree(@NotNull final String input, @NotNull final TagResolver tagResolver) {
        return this.parser.parseToTree(this.newContext(input, null, (TagResolver)Objects.requireNonNull((Object)tagResolver, "tagResolver")));
    }
    
    @Override
    public Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver) {
        return this.parser.parseToTree(this.newContext(input, (Pointered)Objects.requireNonNull((Object)target, "target"), (TagResolver)Objects.requireNonNull((Object)tagResolver, "tagResolver")));
    }
    
    @NotNull
    @Override
    public String serialize(@NotNull final Component component) {
        return MiniMessageSerializer.serialize(component, this.serialResolver(null), this.strict);
    }
    
    private SerializableResolver serialResolver(@Nullable final TagResolver extraResolver) {
        if (extraResolver == null) {
            if (this.parser.tagResolver instanceof SerializableResolver) {
                return (SerializableResolver)this.parser.tagResolver;
            }
        }
        else {
            final TagResolver combined = TagResolver.resolver(this.parser.tagResolver, extraResolver);
            if (combined instanceof SerializableResolver) {
                return (SerializableResolver)combined;
            }
        }
        return (SerializableResolver)TagResolver.empty();
    }
    
    @NotNull
    @Override
    public String escapeTags(@NotNull final String input) {
        return this.parser.escapeTokens(this.newContext(input, null, null));
    }
    
    @NotNull
    @Override
    public String escapeTags(@NotNull final String input, @NotNull final TagResolver tagResolver) {
        return this.parser.escapeTokens(this.newContext(input, null, tagResolver));
    }
    
    @NotNull
    @Override
    public String stripTags(@NotNull final String input) {
        return this.parser.stripTokens(this.newContext(input, null, null));
    }
    
    @NotNull
    @Override
    public String stripTags(@NotNull final String input, @NotNull final TagResolver tagResolver) {
        return this.parser.stripTokens(this.newContext(input, null, tagResolver));
    }
    
    @Override
    public boolean strict() {
        return this.strict;
    }
    
    @NotNull
    @Override
    public TagResolver tags() {
        return this.parser.tagResolver;
    }
    
    @NotNull
    private ContextImpl newContext(@NotNull final String input, @Nullable final Pointered target, @Nullable final TagResolver resolver) {
        Objects.requireNonNull((Object)input, "input");
        return new ContextImpl(this.strict, this.debugOutput, input, this, target, resolver, this.preProcessor, this.postProcessor);
    }
    
    static {
        SERVICE = Services.service(Provider.class);
        BUILDER = (Consumer)MiniMessageImpl.SERVICE.map(Provider::builder).orElseGet(() -> builder -> {});
        DEFAULT_NO_OP = UnaryOperator.identity();
        DEFAULT_COMPACTING_METHOD = Component::compact;
    }
    
    static final class Instances
    {
        static final MiniMessage INSTANCE;
        
        static {
            INSTANCE = (MiniMessage)MiniMessageImpl.SERVICE.map(Provider::miniMessage).orElseGet(() -> new MiniMessageImpl(TagResolver.standard(), false, null, MiniMessageImpl.DEFAULT_NO_OP, MiniMessageImpl.DEFAULT_COMPACTING_METHOD));
        }
    }
    
    static final class BuilderImpl implements Builder
    {
        private TagResolver tagResolver;
        private boolean strict;
        private Consumer<String> debug;
        private UnaryOperator<Component> postProcessor;
        private UnaryOperator<String> preProcessor;
        
        BuilderImpl() {
            this.tagResolver = TagResolver.standard();
            this.strict = false;
            this.debug = null;
            this.postProcessor = MiniMessageImpl.DEFAULT_COMPACTING_METHOD;
            this.preProcessor = MiniMessageImpl.DEFAULT_NO_OP;
            MiniMessageImpl.BUILDER.accept((Object)this);
        }
        
        BuilderImpl(final MiniMessageImpl serializer) {
            this();
            this.tagResolver = serializer.parser.tagResolver;
            this.strict = serializer.strict;
            this.debug = serializer.debugOutput;
            this.postProcessor = serializer.postProcessor;
            this.preProcessor = serializer.preProcessor;
        }
        
        @NotNull
        @Override
        public Builder tags(@NotNull final TagResolver tags) {
            this.tagResolver = (TagResolver)Objects.requireNonNull((Object)tags, "tags");
            return this;
        }
        
        @NotNull
        @Override
        public Builder editTags(@NotNull final Consumer<TagResolver.Builder> adder) {
            Objects.requireNonNull((Object)adder, "adder");
            final TagResolver.Builder builder = TagResolver.builder().resolver(this.tagResolver);
            adder.accept((Object)builder);
            this.tagResolver = builder.build();
            return this;
        }
        
        @NotNull
        @Override
        public Builder strict(final boolean strict) {
            this.strict = strict;
            return this;
        }
        
        @NotNull
        @Override
        public Builder debug(@Nullable final Consumer<String> debugOutput) {
            this.debug = debugOutput;
            return this;
        }
        
        @NotNull
        @Override
        public Builder postProcessor(@NotNull final UnaryOperator<Component> postProcessor) {
            this.postProcessor = (UnaryOperator<Component>)Objects.requireNonNull((Object)postProcessor, "postProcessor");
            return this;
        }
        
        @NotNull
        @Override
        public Builder preProcessor(@NotNull final UnaryOperator<String> preProcessor) {
            this.preProcessor = (UnaryOperator<String>)Objects.requireNonNull((Object)preProcessor, "preProcessor");
            return this;
        }
        
        @NotNull
        @Override
        public MiniMessage build() {
            return new MiniMessageImpl(this.tagResolver, this.strict, this.debug, this.preProcessor, this.postProcessor);
        }
    }
}
