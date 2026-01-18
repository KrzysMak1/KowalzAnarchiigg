package cc.dreamcode.kowal.libs.cc.dreamcode.command.bind;

import lombok.Generated;
import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import lombok.NonNull;

public class BindService
{
    private final BindCache bindCache;
    
    public Optional<?> resolveBind(@NonNull final Class<?> bindClass, @NonNull final DreamSender<?> sender) {
        if (bindClass == null) {
            throw new NullPointerException("bindClass is marked non-null but is null");
        }
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        final Optional<BindResolver<?>> optionalBindResolver = this.bindCache.get(bindClass);
        if (!optionalBindResolver.isPresent()) {
            return (Optional<?>)Optional.empty();
        }
        final BindResolver<?> bindResolver = (BindResolver<?>)optionalBindResolver.get();
        return (Optional<?>)Optional.of((Object)bindResolver.resolveBind(sender));
    }
    
    @Generated
    public BindService(final BindCache bindCache) {
        this.bindCache = bindCache;
    }
}
