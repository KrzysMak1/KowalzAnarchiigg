package cc.dreamcode.kowal.libs.cc.dreamcode.command.result;

import lombok.Generated;
import java.util.Optional;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;

public class ResultService
{
    private final ResultCache resultCache;
    
    public void resolveResult(@NonNull final DreamSender<?> sender, @NonNull final Class<?> type, @NonNull final Object object) {
        if (sender == null) {
            throw new NullPointerException("sender is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        final Optional<ResultResolver> optionalResultResolver = this.resultCache.get(type);
        if (!optionalResultResolver.isPresent()) {
            throw new RuntimeException("Cannot resolve method result, missing resolver: " + (Object)type);
        }
        final ResultResolver resultResolver = (ResultResolver)optionalResultResolver.get();
        resultResolver.resolveResult(sender, type, object);
    }
    
    @Generated
    public ResultService(final ResultCache resultCache) {
        this.resultCache = resultCache;
    }
}
