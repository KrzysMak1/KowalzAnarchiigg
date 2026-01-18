package cc.dreamcode.kowal.libs.cc.dreamcode.platform.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

public class RawObjectResolver implements ComponentClassResolver
{
    @Override
    public boolean isAssignableFrom(@NonNull final Class type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return true;
    }
    
    @Override
    public String getComponentName() {
        return "raw-component";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final Object o) {
        if (o == null) {
            throw new NullPointerException("o is marked non-null but is null");
        }
        return (Map<String, Object>)new HashMap();
    }
    
    @Override
    public Object resolve(@NonNull final Injector injector, @NonNull final Class type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return injector.createInstance((Class<Object>)type);
    }
}
