package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import org.jetbrains.annotations.ApiStatus;
import java.util.stream.Collector;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.ParsingException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.Context;
import java.util.function.BiFunction;
import java.util.Objects;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.TagPattern;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.jetbrains.annotations.NotNull;

public interface TagResolver
{
    @NotNull
    default Builder builder() {
        return new TagResolverBuilderImpl();
    }
    
    @NotNull
    default TagResolver standard() {
        return StandardTags.defaults();
    }
    
    @NotNull
    default TagResolver empty() {
        return EmptyTagResolver.INSTANCE;
    }
    
    default Single resolver(@TagPattern @NotNull final String name, @NotNull final Tag tag) {
        TagInternals.assertValidTagName(name);
        return new SingleResolver(name, (Tag)Objects.requireNonNull((Object)tag, "tag"));
    }
    
    @NotNull
    default TagResolver resolver(@TagPattern @NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
        return resolver((Set<String>)Collections.singleton((Object)name), handler);
    }
    
    @NotNull
    default TagResolver resolver(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
        final Set<String> ownNames = (Set<String>)new HashSet((Collection)names);
        for (final String name : ownNames) {
            TagInternals.assertValidTagName(name);
        }
        Objects.requireNonNull((Object)handler, "handler");
        return new TagResolver() {
            @Nullable
            @Override
            public Tag resolve(@NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
                if (!names.contains((Object)name)) {
                    return null;
                }
                return (Tag)handler.apply((Object)arguments, (Object)ctx);
            }
            
            @Override
            public boolean has(@NotNull final String name) {
                return names.contains((Object)name);
            }
        };
    }
    
    @NotNull
    default TagResolver resolver(@NotNull final TagResolver... resolvers) {
        if (((TagResolver[])Objects.requireNonNull((Object)resolvers, "resolvers")).length == 1) {
            return (TagResolver)Objects.requireNonNull((Object)resolvers[0], "resolvers must not contain null elements");
        }
        return builder().resolvers(resolvers).build();
    }
    
    @NotNull
    default TagResolver resolver(@NotNull final Iterable<? extends TagResolver> resolvers) {
        if (resolvers instanceof Collection) {
            final int size = ((Collection)resolvers).size();
            if (size == 0) {
                return empty();
            }
            if (size == 1) {
                return (TagResolver)Objects.requireNonNull((Object)resolvers.iterator().next(), "resolvers must not contain null elements");
            }
        }
        return builder().resolvers(resolvers).build();
    }
    
    @NotNull
    default TagResolver caching(final WithoutArguments resolver) {
        if (resolver instanceof CachingTagResolver) {
            return resolver;
        }
        return new CachingTagResolver((WithoutArguments)Objects.requireNonNull((Object)resolver, "resolver"));
    }
    
    @NotNull
    default Collector<TagResolver, ?, TagResolver> toTagResolver() {
        return TagResolverBuilderImpl.COLLECTOR;
    }
    
    @Nullable
    Tag resolve(@TagPattern @NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException;
    
    boolean has(@NotNull final String name);
    
    @ApiStatus.NonExtendable
    public interface Single extends WithoutArguments
    {
        @NotNull
        String key();
        
        @NotNull
        Tag tag();
        
        @Nullable
        default Tag resolve(@TagPattern @NotNull final String name) {
            if (this.has(name)) {
                return this.tag();
            }
            return null;
        }
        
        default boolean has(@NotNull final String name) {
            return name.equalsIgnoreCase(this.key());
        }
    }
    
    @FunctionalInterface
    public interface WithoutArguments extends TagResolver
    {
        @Nullable
        Tag resolve(@TagPattern @NotNull final String name);
        
        default boolean has(@NotNull final String name) {
            return this.resolve(name) != null;
        }
        
        @Nullable
        default Tag resolve(@TagPattern @NotNull final String name, @NotNull final ArgumentQueue arguments, @NotNull final Context ctx) throws ParsingException {
            final Tag resolved = this.resolve(name);
            if (resolved != null && arguments.hasNext()) {
                throw ctx.newException("Tag '<" + name + ">' does not accept any arguments");
            }
            return resolved;
        }
    }
    
    public interface Builder
    {
        @NotNull
        Builder tag(@TagPattern @NotNull final String name, @NotNull final Tag tag);
        
        @NotNull
        default Builder tag(@TagPattern @NotNull final String name, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
            return this.tag((Set<String>)Collections.singleton((Object)name), handler);
        }
        
        @NotNull
        default Builder tag(@NotNull final Set<String> names, @NotNull final BiFunction<ArgumentQueue, Context, Tag> handler) {
            return this.resolver(TagResolver.resolver(names, handler));
        }
        
        @NotNull
        Builder resolver(@NotNull final TagResolver resolver);
        
        @NotNull
        Builder resolvers(@NotNull final TagResolver... resolvers);
        
        @NotNull
        Builder resolvers(@NotNull final Iterable<? extends TagResolver> resolvers);
        
        @NotNull
        default Builder caching(final WithoutArguments dynamic) {
            return this.resolver(TagResolver.caching(dynamic));
        }
        
        @NotNull
        TagResolver build();
    }
}
