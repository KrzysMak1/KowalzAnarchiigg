package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;
import java.util.stream.Collectors;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examiner;
import cc.dreamcode.kowal.libs.net.kyori.examination.string.MultiLineStringExaminer;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.ComponentLike;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Inserting;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Modifying;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.TagNode;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ValueNode;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.ElementNode;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.function.Predicate;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.ParsingExceptionImpl;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.node.RootNode;
import java.util.Iterator;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.TokenParser;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.parser.Token;
import java.util.function.BiConsumer;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;

final class MiniMessageParser
{
    final TagResolver tagResolver;
    
    MiniMessageParser() {
        this.tagResolver = TagResolver.standard();
    }
    
    MiniMessageParser(final TagResolver tagResolver) {
        this.tagResolver = tagResolver;
    }
    
    @NotNull
    String escapeTokens(@NotNull final ContextImpl context) {
        final StringBuilder sb = new StringBuilder(context.message().length());
        this.escapeTokens(sb, context);
        return sb.toString();
    }
    
    void escapeTokens(final StringBuilder sb, @NotNull final ContextImpl context) {
        this.escapeTokens(sb, context.message(), context);
    }
    
    private void escapeTokens(final StringBuilder sb, final String richMessage, final ContextImpl context) {
        this.processTokens(sb, richMessage, context, (BiConsumer<Token, StringBuilder>)((token, builder) -> {
            builder.append('\\').append('<');
            if (token.type() == TokenType.CLOSE_TAG) {
                builder.append('/');
            }
            final List<Token> childTokens = token.childTokens();
            for (int i = 0; i < childTokens.size(); ++i) {
                if (i != 0) {
                    builder.append(':');
                }
                this.escapeTokens(builder, ((Token)childTokens.get(i)).get((CharSequence)richMessage).toString(), context);
            }
            builder.append('>');
        }));
    }
    
    @NotNull
    String stripTokens(@NotNull final ContextImpl context) {
        final StringBuilder sb = new StringBuilder(context.message().length());
        this.processTokens(sb, context, (BiConsumer<Token, StringBuilder>)((token, builder) -> {}));
        return sb.toString();
    }
    
    private void processTokens(@NotNull final StringBuilder sb, @NotNull final ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
        this.processTokens(sb, context.message(), context, tagHandler);
    }
    
    private void processTokens(@NotNull final StringBuilder sb, @NotNull final String richMessage, @NotNull final ContextImpl context, final BiConsumer<Token, StringBuilder> tagHandler) {
        final TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
        final List<Token> root = TokenParser.tokenize(richMessage, true);
        for (final Token token : root) {
            switch (token.type()) {
                case TEXT: {
                    sb.append((CharSequence)richMessage, token.startIndex(), token.endIndex());
                    continue;
                }
                case OPEN_TAG:
                case CLOSE_TAG:
                case OPEN_CLOSE_TAG: {
                    if (token.childTokens().isEmpty()) {
                        sb.append((CharSequence)richMessage, token.startIndex(), token.endIndex());
                        continue;
                    }
                    final String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(((Token)token.childTokens().get(0)).get((CharSequence)richMessage).toString());
                    if (combinedResolver.has(sanitized)) {
                        tagHandler.accept((Object)token, (Object)sb);
                        continue;
                    }
                    sb.append((CharSequence)richMessage, token.startIndex(), token.endIndex());
                    continue;
                }
                default: {
                    throw new IllegalArgumentException("Unsupported token type " + (Object)token.type());
                }
            }
        }
    }
    
    @NotNull
    RootNode parseToTree(@NotNull final ContextImpl context) {
        final TagResolver combinedResolver = TagResolver.resolver(this.tagResolver, context.extraTags());
        final String processedMessage = (String)context.preProcessor().apply((Object)context.message());
        final Consumer<String> debug = context.debugOutput();
        if (debug != null) {
            debug.accept((Object)"Beginning parsing message ");
            debug.accept((Object)processedMessage);
            debug.accept((Object)"\n");
        }
        TokenParser.TagProvider transformationFactory;
        if (debug != null) {
            transformationFactory = ((name, args, token) -> {
                try {
                    debug.accept((Object)"Attempting to match node '");
                    debug.accept((Object)name);
                    debug.accept((Object)"'");
                    if (token != null) {
                        debug.accept((Object)" at column ");
                        debug.accept((Object)String.valueOf(token.startIndex()));
                    }
                    debug.accept((Object)"\n");
                    final Tag transformation = combinedResolver.resolve(name, new ArgumentQueueImpl<Object>(context, (java.util.List<?>)args), context);
                    if (transformation == null) {
                        debug.accept((Object)"Could not match node '");
                        debug.accept((Object)name);
                        debug.accept((Object)"'\n");
                    }
                    else {
                        debug.accept((Object)"Successfully matched node '");
                        debug.accept((Object)name);
                        debug.accept((Object)"' to tag ");
                        debug.accept((Object)((transformation instanceof Examinable) ? ((Examinable)transformation).examinableName() : transformation.getClass().getName()));
                        debug.accept((Object)"\n");
                    }
                    return transformation;
                }
                catch (final ParsingException e) {
                    if (token != null && e instanceof ParsingExceptionImpl) {
                        final ParsingExceptionImpl impl = (ParsingExceptionImpl)e;
                        if (impl.tokens().length == 0) {
                            impl.tokens(new Token[] { token });
                        }
                    }
                    debug.accept((Object)"Could not match node '");
                    debug.accept((Object)name);
                    debug.accept((Object)"' - ");
                    debug.accept((Object)e.getMessage());
                    debug.accept((Object)"\n");
                    return null;
                }
            });
        }
        else {
            transformationFactory = ((name, args, token) -> {
                try {
                    return combinedResolver.resolve(name, new ArgumentQueueImpl<Object>(context, (java.util.List<?>)args), context);
                }
                catch (final ParsingException ignored) {
                    return null;
                }
            });
        }
        final Predicate<String> tagNameChecker = (Predicate<String>)(name -> {
            final String sanitized = TokenParser.TagProvider.sanitizePlaceholderName(name);
            return combinedResolver.has(sanitized);
        });
        final String preProcessed = TokenParser.resolvePreProcessTags(processedMessage, transformationFactory);
        context.message(preProcessed);
        final RootNode root = TokenParser.parse(transformationFactory, tagNameChecker, preProcessed, processedMessage, context.strict());
        if (debug != null) {
            debug.accept((Object)"Text parsed into element tree:\n");
            debug.accept((Object)root.toString());
        }
        return root;
    }
    
    @NotNull
    Component parseFormat(@NotNull final ContextImpl context) {
        final ElementNode root = this.parseToTree(context);
        return (Component)Objects.requireNonNull((Object)context.postProcessor().apply((Object)this.treeToComponent(root, context)), "Post-processor must not return null");
    }
    
    @NotNull
    Component treeToComponent(@NotNull final ElementNode node, @NotNull final ContextImpl context) {
        Component comp = Component.empty();
        Tag tag = null;
        if (node instanceof ValueNode) {
            comp = Component.text(((ValueNode)node).value());
        }
        else if (node instanceof TagNode) {
            final TagNode tagNode = (TagNode)node;
            tag = tagNode.tag();
            if (tag instanceof Modifying) {
                final Modifying modTransformation = (Modifying)tag;
                this.visitModifying(modTransformation, tagNode, 0);
                modTransformation.postVisit();
            }
            if (tag instanceof Inserting) {
                comp = ((Inserting)tag).value();
            }
        }
        if (!node.unsafeChildren().isEmpty()) {
            final List<Component> children = (List<Component>)new ArrayList(comp.children().size() + node.children().size());
            children.addAll((Collection)comp.children());
            for (final ElementNode child : node.unsafeChildren()) {
                children.add((Object)this.treeToComponent(child, context));
            }
            comp = comp.children(children);
        }
        if (tag instanceof Modifying) {
            comp = this.handleModifying((Modifying)tag, comp, 0);
        }
        final Consumer<String> debug = context.debugOutput();
        if (debug != null) {
            debug.accept((Object)"==========\ntreeToComponent \n");
            debug.accept((Object)node.toString());
            debug.accept((Object)"\n");
            debug.accept((Object)comp.examine((Examiner<Stream>)MultiLineStringExaminer.simpleEscaping()).collect(Collectors.joining((CharSequence)"\n")));
            debug.accept((Object)"\n==========\n");
        }
        return comp;
    }
    
    private void visitModifying(final Modifying modTransformation, final ElementNode node, final int depth) {
        modTransformation.visit(node, depth);
        for (final ElementNode child : node.unsafeChildren()) {
            this.visitModifying(modTransformation, child, depth + 1);
        }
    }
    
    private Component handleModifying(final Modifying modTransformation, final Component current, final int depth) {
        Component newComp = modTransformation.apply(current, depth);
        for (final Component child : current.children()) {
            newComp = newComp.append(this.handleModifying(modTransformation, child, depth + 1));
        }
        return newComp;
    }
}
