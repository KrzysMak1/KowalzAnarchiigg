package cc.dreamcode.kowal.libs.cc.dreamcode.command.result;

import java.util.Optional;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;

public class ResultCache
{
    private final List<ResultResolver> resultResolvers;
    
    public ResultCache() {
        this.resultResolvers = (List<ResultResolver>)new ArrayList();
    }
    
    public Optional<ResultResolver> get(@NonNull final Class<?> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<ResultResolver>)this.resultResolvers.stream().filter(resultResolver -> resultResolver.isAssignableFrom(type)).findAny();
    }
    
    public void registerResult(@NonNull final ResultResolver resultResolver) {
        if (resultResolver == null) {
            throw new NullPointerException("resultResolver is marked non-null but is null");
        }
        this.resultResolvers.add((Object)resultResolver);
    }
    
    public void unregisterResult(@NonNull final Class<?> resultResolverClass) {
        if (resultResolverClass == null) {
            throw new NullPointerException("resultResolverClass is marked non-null but is null");
        }
        this.resultResolvers.removeIf(resultResolver -> resultResolver.isAssignableFrom(resultResolverClass));
    }
}
