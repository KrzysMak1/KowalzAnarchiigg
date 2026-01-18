package cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver;

import lombok.Generated;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.array.ArrayTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import java.util.Optional;
import lombok.NonNull;

public class ResolverService
{
    private final ResolverCache resolverCache;
    
    public boolean support(@NonNull final Class<?> expectingClass, @NonNull final String input) {
        if (expectingClass == null) {
            throw new NullPointerException("expectingClass is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return this.resolve(expectingClass, input).isPresent();
    }
    
    public boolean supportArray(@NonNull final Class<?> expectingClass, @NonNull final Object[] input) {
        if (expectingClass == null) {
            throw new NullPointerException("expectingClass is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return this.resolveArray(expectingClass, input).isPresent();
    }
    
    public <T> Optional<T> resolve(@NonNull final Class<T> expectingClass, @NonNull final String input) {
        if (expectingClass == null) {
            throw new NullPointerException("expectingClass is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        final ObjectTransformer<T> objectTransformer = (ObjectTransformer<T>)this.resolverCache.get(expectingClass).orElseThrow(() -> new RuntimeException("Cannot find resolver for " + (Object)expectingClass));
        return objectTransformer.transform(expectingClass, input);
    }
    
    public <T> Optional<T[]> resolveArray(@NonNull final Class<T> expectingClass, @NonNull final Object[] input) {
        if (expectingClass == null) {
            throw new NullPointerException("expectingClass is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        final ArrayTransformer<T> arrayTransformerTransformer = (ArrayTransformer<T>)this.resolverCache.getArray(expectingClass).orElseThrow(() -> new RuntimeException("Cannot find array-resolver for " + (Object)expectingClass));
        return arrayTransformerTransformer.transform(expectingClass, input);
    }
    
    @Generated
    public ResolverCache getResolverCache() {
        return this.resolverCache;
    }
    
    @Generated
    public ResolverService(final ResolverCache resolverCache) {
        this.resolverCache = resolverCache;
    }
}
