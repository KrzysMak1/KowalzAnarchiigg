package cc.dreamcode.kowal.libs.cc.dreamcode.platform.other.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProvider;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.result.ResultResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class CommandResultResolver implements ComponentClassResolver<ResultResolver>
{
    private final CommandProvider commandProvider;
    
    @Inject
    public CommandResultResolver(final CommandProvider commandProvider) {
        this.commandProvider = commandProvider;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<ResultResolver> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ResultResolver.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "cmd-result-resolver";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final ResultResolver resultResolver) {
        if (resultResolver == null) {
            throw new NullPointerException("resultResolver is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public ResultResolver resolve(@NonNull final Injector injector, @NonNull final Class<ResultResolver> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final ResultResolver resultResolver = injector.createInstance(type);
        this.commandProvider.registerResult(resultResolver);
        return resultResolver;
    }
}
