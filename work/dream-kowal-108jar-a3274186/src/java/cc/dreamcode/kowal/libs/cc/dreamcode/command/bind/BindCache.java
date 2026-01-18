package cc.dreamcode.kowal.libs.cc.dreamcode.command.bind;

import java.util.Optional;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;

public class BindCache
{
    private final List<BindResolver<?>> bindResolvers;
    
    public BindCache() {
        this.bindResolvers = (List<BindResolver<?>>)new ArrayList();
    }
    
    public Optional<BindResolver<?>> get(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<BindResolver<?>>)this.bindResolvers.stream().filter(bindResolver -> bindResolver.isAssignableFrom(type)).findAny();
    }
    
    public void registerBind(@NonNull final BindResolver<?> bindResolver) {
        if (bindResolver == null) {
            throw new NullPointerException("bindResolver is marked non-null but is null");
        }
        this.bindResolvers.add((Object)bindResolver);
    }
    
    public void unregisterBind(@NonNull final Class<?> bindClass) {
        if (bindClass == null) {
            throw new NullPointerException("bindClass is marked non-null but is null");
        }
        this.bindResolvers.removeIf(bindResolver -> bindResolver.isAssignableFrom(bindClass));
    }
}
