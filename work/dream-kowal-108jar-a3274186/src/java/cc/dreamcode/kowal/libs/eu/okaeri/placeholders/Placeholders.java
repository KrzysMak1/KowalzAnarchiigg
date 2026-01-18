package cc.dreamcode.kowal.libs.eu.okaeri.placeholders;

import java.util.LinkedHashMap;
import java.util.Iterator;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageFieldAccessor;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageField;
import java.util.HashMap;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.CompiledMessage;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver.PlaceholderResolver;
import java.util.Map;

public class Placeholders
{
    private Map<Class<?>, Map<String, PlaceholderResolver>> resolvers;
    private List<Class<?>> resolversOrdered;
    private PlaceholderResolver fallbackResolver;
    private boolean fastMode;
    
    public static Placeholders create() {
        return create(false);
    }
    
    public static Placeholders create(final boolean registerDefaults) {
        final Placeholders placeholders = new Placeholders();
        if (registerDefaults) {
            placeholders.registerPlaceholders(new DefaultPlaceholderPack());
        }
        return placeholders;
    }
    
    public PlaceholderContext contextOf(@NonNull final CompiledMessage message) {
        if (message == null) {
            throw new NullPointerException("message is marked non-null but is null");
        }
        return PlaceholderContext.of(this, message);
    }
    
    public Placeholders fallbackResolver(final PlaceholderResolver fallbackResolver) {
        this.fallbackResolver = fallbackResolver;
        return this;
    }
    
    public Placeholders fastMode(final boolean fastMode) {
        this.fastMode = fastMode;
        return this;
    }
    
    public Placeholders registerPlaceholders(@NonNull final PlaceholderPack pack) {
        if (pack == null) {
            throw new NullPointerException("pack is marked non-null but is null");
        }
        pack.register(this);
        return this;
    }
    
    public void setResolvers(@NonNull final Map<Class<?>, Map<String, PlaceholderResolver>> resolvers) {
        if (resolvers == null) {
            throw new NullPointerException("resolvers is marked non-null but is null");
        }
        this.resolvers = resolvers;
        final ArrayList<Class<?>> keys = (ArrayList<Class<?>>)new ArrayList((Collection)resolvers.keySet());
        Collections.reverse((List)keys);
        this.resolversOrdered = (List<Class<?>>)keys;
    }
    
    public <T> Placeholders registerPlaceholder(@NonNull final Class<T> type, @NonNull final PlaceholderResolver<T> resolver) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (resolver == null) {
            throw new NullPointerException("resolver is marked non-null but is null");
        }
        if (!this.resolvers.containsKey((Object)type)) {
            this.resolvers.put((Object)type, (Object)new HashMap());
            this.resolversOrdered.add(0, (Object)type);
        }
        final Map<String, PlaceholderResolver> resolverMap = (Map<String, PlaceholderResolver>)this.resolvers.get((Object)type);
        resolverMap.put((Object)null, (Object)resolver);
        return this;
    }
    
    public <T> Placeholders registerPlaceholder(@NonNull final Class<T> type, @NonNull final String name, @NonNull final PlaceholderResolver<T> resolver) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (resolver == null) {
            throw new NullPointerException("resolver is marked non-null but is null");
        }
        if (!this.resolvers.containsKey((Object)type)) {
            this.resolvers.put((Object)type, (Object)new HashMap());
            this.resolversOrdered.add(0, (Object)type);
        }
        final Map<String, PlaceholderResolver> resolverMap = (Map<String, PlaceholderResolver>)this.resolvers.get((Object)type);
        resolverMap.put((Object)name, (Object)resolver);
        return this;
    }
    
    @Deprecated
    public Object readValue(@NonNull final Object from) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        final PlaceholderResolver placeholderResolver = this.getResolver(from, null);
        if (placeholderResolver != null) {
            return placeholderResolver.resolve(from, MessageField.unknown(), null);
        }
        throw new IllegalArgumentException("cannot find resolver for " + (Object)from.getClass());
    }
    
    @Deprecated
    public Object readValue(@NonNull final Object from, @Nullable final String param) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        final PlaceholderResolver placeholderResolver = this.getResolver(from, param);
        if (placeholderResolver != null) {
            return placeholderResolver.resolve(from, MessageField.unknown(), null);
        }
        throw new IllegalArgumentException("cannot find resolver for " + (Object)from.getClass() + ": " + param);
    }
    
    public PlaceholderResolver getResolver(@NonNull final Object from, @Nullable final String param) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        final Class<?> fromClass = from.getClass();
        Map<String, PlaceholderResolver> resolverMap = (Map<String, PlaceholderResolver>)this.resolvers.get((Object)fromClass);
        if (resolverMap == null) {
            for (final Class<?> potentialType : this.resolversOrdered) {
                if (potentialType.isAssignableFrom(fromClass)) {
                    resolverMap = (Map<String, PlaceholderResolver>)this.resolvers.get((Object)potentialType);
                    final PlaceholderResolver resolver = (PlaceholderResolver)resolverMap.get((Object)param);
                    if (resolver != null) {
                        return resolver;
                    }
                    continue;
                }
            }
            return this.fallbackResolver;
        }
        final PlaceholderResolver resolver2 = (PlaceholderResolver)resolverMap.get((Object)param);
        if (resolver2 == null) {
            return this.fallbackResolver;
        }
        return resolver2;
    }
    
    public int getResolversCount() {
        return Math.toIntExact(this.resolvers.values().stream().mapToLong(map -> map.entrySet().size()).sum());
    }
    
    public Placeholders copy() {
        final Placeholders placeholders = new Placeholders();
        placeholders.setResolvers(this.getResolversCopy());
        placeholders.fallbackResolver = this.getFallbackResolver();
        return placeholders;
    }
    
    public Map<Class<?>, Map<String, PlaceholderResolver>> getResolversCopy() {
        final Map<Class<?>, Map<String, PlaceholderResolver>> resolvers = (Map<Class<?>, Map<String, PlaceholderResolver>>)new LinkedHashMap();
        for (final Map.Entry<Class<?>, Map<String, PlaceholderResolver>> entry : this.resolvers.entrySet()) {
            final Map<String, PlaceholderResolver> map = (Map<String, PlaceholderResolver>)new HashMap();
            map.putAll((Map)entry.getValue());
            resolvers.put((Object)entry.getKey(), (Object)map);
        }
        return resolvers;
    }
    
    private Placeholders() {
        this.resolvers = (Map<Class<?>, Map<String, PlaceholderResolver>>)new LinkedHashMap();
        this.resolversOrdered = (List<Class<?>>)new ArrayList();
        this.fallbackResolver = null;
        this.fastMode = true;
    }
    
    public PlaceholderResolver getFallbackResolver() {
        return this.fallbackResolver;
    }
    
    public boolean isFastMode() {
        return this.fastMode;
    }
}
