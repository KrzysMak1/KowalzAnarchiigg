package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver;

import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.ListBuilder;
import java.util.Collection;
import java.util.Optional;
import lombok.NonNull;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import java.util.List;

public class ResolverCache
{
    private final List<ObjectTransformer<?>> objectTransformers;
    private final List<ArrayTransformer<?>> arrayTransformers;
    private final Map<Class<?>, List<Class<?>>> assignableClasses;
    
    public ResolverCache() {
        this.objectTransformers = (List<ObjectTransformer<?>>)new ArrayList();
        this.arrayTransformers = (List<ArrayTransformer<?>>)new ArrayList();
        this.assignableClasses = (Map<Class<?>, List<Class<?>>>)new HashMap();
    }
    
    public <T> Optional<ObjectTransformer<T>> get(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<ObjectTransformer<T>>)this.objectTransformers.stream().filter(objectTransformer -> {
            final List<Class<?>> classList = (List<Class<?>>)this.assignableClasses.get((Object)objectTransformer.getGeneric());
            if (classList == null) {
                return objectTransformer.isAssignableFrom(type);
            }
            return objectTransformer.isAssignableFrom(type) || classList.contains((Object)type);
        }).map(objectTransformer -> objectTransformer).findAny();
    }
    
    public <T> Optional<ArrayTransformer<T>> getArray(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<ArrayTransformer<T>>)this.arrayTransformers.stream().filter(arrayTransformer -> {
            final List<Class<?>> classList = (List<Class<?>>)this.assignableClasses.get((Object)arrayTransformer.getGeneric());
            if (classList == null) {
                return arrayTransformer.isAssignableFrom(type);
            }
            return arrayTransformer.isAssignableFrom(type) || classList.contains((Object)type);
        }).map(arrayTransformer -> arrayTransformer).findAny();
    }
    
    public List<ObjectTransformer<?>> getObjectTransformers() {
        return (List<ObjectTransformer<?>>)new ArrayList((Collection)this.objectTransformers);
    }
    
    public ResolverCache add(@NonNull final ObjectTransformer<?> objectTransformer) {
        if (objectTransformer == null) {
            throw new NullPointerException("objectTransformer is marked non-null but is null");
        }
        this.objectTransformers.add((Object)objectTransformer);
        return this;
    }
    
    public ResolverCache add(@NonNull final ArrayTransformer<?> arrayTransformer) {
        if (arrayTransformer == null) {
            throw new NullPointerException("arrayTransformer is marked non-null but is null");
        }
        this.arrayTransformers.add((Object)arrayTransformer);
        return this;
    }
    
    public ResolverCache addAssignableClass(@NonNull final Class<?> from, @NonNull final Class<?> to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        final List<Class<?>> classList = (List<Class<?>>)this.assignableClasses.get((Object)from);
        if (classList == null) {
            this.assignableClasses.put((Object)from, (Object)ListBuilder.of(to));
            return this;
        }
        classList.add((Object)to);
        return this;
    }
    
    public ResolverCache remove(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        this.objectTransformers.removeIf(objectTransformer -> objectTransformer.isAssignableFrom(type));
        return this;
    }
    
    public ResolverCache removeArray(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        this.arrayTransformers.removeIf(arrayTransformer -> arrayTransformer.isAssignableFrom(type));
        return this;
    }
    
    public ResolverCache removeAssignableClass(@NonNull final Class<?> from, @NonNull final Class<?> to) {
        if (from == null) {
            throw new NullPointerException("from is marked non-null but is null");
        }
        if (to == null) {
            throw new NullPointerException("to is marked non-null but is null");
        }
        final List<Class<?>> classList = (List<Class<?>>)this.assignableClasses.get((Object)from);
        if (classList == null) {
            return this;
        }
        classList.remove((Object)to);
        return this;
    }
}
