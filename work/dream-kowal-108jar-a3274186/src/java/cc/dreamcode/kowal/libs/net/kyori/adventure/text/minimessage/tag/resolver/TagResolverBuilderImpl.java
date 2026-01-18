package cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.resolver;

import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.internal.TagInternals;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.minimessage.tag.Tag;
import java.util.Map;
import java.util.stream.Collector;

final class TagResolverBuilderImpl implements TagResolver.Builder
{
    static final Collector<TagResolver, TagResolver.Builder, TagResolver> COLLECTOR;
    private final Map<String, Tag> replacements;
    private final List<TagResolver> resolvers;
    
    TagResolverBuilderImpl() {
        this.replacements = (Map<String, Tag>)new HashMap();
        this.resolvers = (List<TagResolver>)new ArrayList();
    }
    
    @Override
    public TagResolver.Builder tag(@NotNull final String name, @NotNull final Tag tag) {
        TagInternals.assertValidTagName((String)Objects.requireNonNull((Object)name, "name"));
        this.replacements.put((Object)name, (Object)Objects.requireNonNull((Object)tag, "tag"));
        return this;
    }
    
    @Override
    public TagResolver.Builder resolver(@NotNull final TagResolver resolver) {
        if (resolver instanceof SequentialTagResolver) {
            this.resolvers(((SequentialTagResolver)resolver).resolvers, false);
        }
        else if (!this.consumePotentialMappable(resolver)) {
            this.popMap();
            this.resolvers.add((Object)Objects.requireNonNull((Object)resolver, "resolver"));
        }
        return this;
    }
    
    @Override
    public TagResolver.Builder resolvers(@NotNull final TagResolver... resolvers) {
        return this.resolvers(resolvers, true);
    }
    
    private TagResolver.Builder resolvers(@NotNull final TagResolver[] resolvers, final boolean forwards) {
        boolean popped = false;
        Objects.requireNonNull((Object)resolvers, "resolvers");
        if (forwards) {
            for (final TagResolver resolver : resolvers) {
                popped = this.single(resolver, popped);
            }
        }
        else {
            for (int i = resolvers.length - 1; i >= 0; --i) {
                popped = this.single(resolvers[i], popped);
            }
        }
        return this;
    }
    
    @Override
    public TagResolver.Builder resolvers(@NotNull final Iterable<? extends TagResolver> resolvers) {
        boolean popped = false;
        for (final TagResolver resolver : (Iterable)Objects.requireNonNull((Object)resolvers, "resolvers")) {
            popped = this.single(resolver, popped);
        }
        return this;
    }
    
    private boolean single(final TagResolver resolver, final boolean popped) {
        if (resolver instanceof SequentialTagResolver) {
            this.resolvers(((SequentialTagResolver)resolver).resolvers, false);
        }
        else if (!this.consumePotentialMappable(resolver)) {
            if (!popped) {
                this.popMap();
            }
            this.resolvers.add((Object)Objects.requireNonNull((Object)resolver, "resolvers[?]"));
            return true;
        }
        return false;
    }
    
    private void popMap() {
        if (!this.replacements.isEmpty()) {
            this.resolvers.add((Object)new MapTagResolver((Map<String, ? extends Tag>)new HashMap((Map)this.replacements)));
            this.replacements.clear();
        }
    }
    
    private boolean consumePotentialMappable(final TagResolver resolver) {
        return resolver instanceof MappableResolver && ((MappableResolver)resolver).contributeToMap(this.replacements);
    }
    
    @NotNull
    @Override
    public TagResolver build() {
        this.popMap();
        if (this.resolvers.size() == 0) {
            return EmptyTagResolver.INSTANCE;
        }
        if (this.resolvers.size() == 1) {
            return (TagResolver)this.resolvers.get(0);
        }
        final TagResolver[] resolvers = (TagResolver[])this.resolvers.toArray((Object[])new TagResolver[0]);
        Collections.reverse(Arrays.asList((Object[])resolvers));
        return new SequentialTagResolver(resolvers);
    }
    
    static {
        COLLECTOR = Collector.of(TagResolver::builder, TagResolver.Builder::resolver, (left, right) -> TagResolver.builder().resolvers(left.build(), right.build()), TagResolver.Builder::build, new Collector.Characteristics[0]);
    }
}
