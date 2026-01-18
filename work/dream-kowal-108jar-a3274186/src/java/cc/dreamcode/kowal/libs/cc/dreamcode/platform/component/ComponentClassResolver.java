package cc.dreamcode.kowal.libs.cc.dreamcode.platform.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injectable;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamLogger;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.Map;
import lombok.NonNull;

public interface ComponentClassResolver<T>
{
    boolean isAssignableFrom(@NonNull final Class<T> type);
    
    String getComponentName();
    
    Map<String, Object> getMetas(@NonNull final T t);
    
    T resolve(@NonNull final Injector injector, @NonNull final Class<T> type);
    
    default T register(@NonNull final Injector injector, @NonNull final Class<T> type, final boolean debug) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final long start = System.currentTimeMillis();
        final T instance = this.resolve(injector, type);
        injector.registerInjectable(instance);
        final long took = System.currentTimeMillis() - start;
        if (debug) {
            injector.getInjectable("", DreamLogger.class).map(Injectable::getObject).ifPresent(dreamLogger -> dreamLogger.info(new DreamLogger.Builder().type("Added " + this.getComponentName() + " component").name(type.getSimpleName()).took(took).meta(this.getMetas(instance)).build()));
        }
        return instance;
    }
}
