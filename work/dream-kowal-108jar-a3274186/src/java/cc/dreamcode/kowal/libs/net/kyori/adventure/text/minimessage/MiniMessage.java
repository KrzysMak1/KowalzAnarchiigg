package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage;

import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.util.PlatformAPI;
import java.util.function.UnaryOperator;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.builder.AbstractBuilder;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tree.Node;
import cc.dreamcode.kowal.libs.net.kyori.adventure.pointer.Pointered;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;

public interface MiniMessage extends ComponentSerializer<Component, Component, String>
{
    @NotNull
    default MiniMessage miniMessage() {
        return MiniMessageImpl.Instances.INSTANCE;
    }
    
    @NotNull
    String escapeTags(@NotNull final String input);
    
    @NotNull
    String escapeTags(@NotNull final String input, @NotNull final TagResolver tagResolver);
    
    @NotNull
    default String escapeTags(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
        return this.escapeTags(input, TagResolver.resolver(tagResolvers));
    }
    
    @NotNull
    String stripTags(@NotNull final String input);
    
    @NotNull
    String stripTags(@NotNull final String input, @NotNull final TagResolver tagResolver);
    
    @NotNull
    default String stripTags(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
        return this.stripTags(input, TagResolver.resolver(tagResolvers));
    }
    
    @NotNull
    Component deserialize(@NotNull final String input, @NotNull final Pointered target);
    
    @NotNull
    Component deserialize(@NotNull final String input, @NotNull final TagResolver tagResolver);
    
    @NotNull
    Component deserialize(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver);
    
    @NotNull
    default Component deserialize(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
        return this.deserialize(input, TagResolver.resolver(tagResolvers));
    }
    
    @NotNull
    default Component deserialize(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver... tagResolvers) {
        return this.deserialize(input, target, TagResolver.resolver(tagResolvers));
    }
    
    Node.Root deserializeToTree(@NotNull final String input);
    
    Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target);
    
    Node.Root deserializeToTree(@NotNull final String input, @NotNull final TagResolver tagResolver);
    
    Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver tagResolver);
    
    default Node.Root deserializeToTree(@NotNull final String input, @NotNull final TagResolver... tagResolvers) {
        return this.deserializeToTree(input, TagResolver.resolver(tagResolvers));
    }
    
    default Node.Root deserializeToTree(@NotNull final String input, @NotNull final Pointered target, @NotNull final TagResolver... tagResolvers) {
        return this.deserializeToTree(input, target, TagResolver.resolver(tagResolvers));
    }
    
    boolean strict();
    
    @NotNull
    TagResolver tags();
    
    default Builder builder() {
        return new MiniMessageImpl.BuilderImpl();
    }
    
    public interface Builder extends AbstractBuilder<MiniMessage>
    {
        @NotNull
        Builder tags(@NotNull final TagResolver tags);
        
        @NotNull
        Builder editTags(@NotNull final Consumer<TagResolver.Builder> adder);
        
        @NotNull
        Builder strict(final boolean strict);
        
        @NotNull
        Builder debug(@Nullable final Consumer<String> debugOutput);
        
        @NotNull
        Builder postProcessor(@NotNull final UnaryOperator<Component> postProcessor);
        
        @NotNull
        Builder preProcessor(@NotNull final UnaryOperator<String> preProcessor);
        
        @NotNull
        MiniMessage build();
    }
    
    @PlatformAPI
    @ApiStatus.Internal
    public interface Provider
    {
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        MiniMessage miniMessage();
        
        @PlatformAPI
        @ApiStatus.Internal
        @NotNull
        Consumer<Builder> builder();
    }
}
