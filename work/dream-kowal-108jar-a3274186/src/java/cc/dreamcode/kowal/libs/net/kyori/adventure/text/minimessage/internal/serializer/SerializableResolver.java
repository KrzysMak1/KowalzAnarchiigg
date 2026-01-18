package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.serializer;

import org.jetbrains.annotations.Nullable;
import java.util.Iterator;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver.ArgumentQueue;
import java.util.function.BiFunction;
import org.jetbrains.annotations.NotNull;

public interface SerializableResolver
{
    @NotNull
    default TagResolver claimingComponent(@NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final Function<Component, Emitable> componentClaim) {
        return claimingComponent((Set<String>)Collections.singleton((Object)name), handler, componentClaim);
    }
    
    @NotNull
    default TagResolver claimingComponent(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final Function<Component, Emitable> componentClaim) {
        final Set<String> ownNames = (Set<String>)new HashSet((Collection)names);
        for (final String name : ownNames) {
            TagInternals.assertValidTagName(name);
        }
        Objects.requireNonNull((Object)handler, "handler");
        return new ComponentClaimingResolverImpl(ownNames, handler, componentClaim);
    }
    
    @NotNull
    default TagResolver claimingStyle(@NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final StyleClaim<?> styleClaim) {
        return claimingStyle((Set<String>)Collections.singleton((Object)name), handler, styleClaim);
    }
    
    @NotNull
    default TagResolver claimingStyle(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler, @NotNull final StyleClaim<?> styleClaim) {
        final Set<String> ownNames = (Set<String>)new HashSet((Collection)names);
        for (final String name : ownNames) {
            TagInternals.assertValidTagName(name);
        }
        Objects.requireNonNull((Object)handler, "handler");
        return new StyleClaimingResolverImpl(ownNames, handler, styleClaim);
    }
    
    void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer);
    
    public interface Single extends SerializableResolver
    {
        default void handle(@NotNull final Component serializable, @NotNull final ClaimConsumer consumer) {
            final StyleClaim<?> style = this.claimStyle();
            if (style != null && !consumer.styleClaimed(style.claimKey())) {
                final Emitable applied = style.apply(serializable.style());
                if (applied != null) {
                    consumer.style(style.claimKey(), applied);
                }
            }
            if (!consumer.componentClaimed()) {
                final Emitable component = this.claimComponent(serializable);
                if (component != null) {
                    consumer.component(component);
                }
            }
        }
        
        @Nullable
        default StyleClaim<?> claimStyle() {
            return null;
        }
        
        @Nullable
        default Emitable claimComponent(@NotNull final Component component) {
            return null;
        }
    }
}
