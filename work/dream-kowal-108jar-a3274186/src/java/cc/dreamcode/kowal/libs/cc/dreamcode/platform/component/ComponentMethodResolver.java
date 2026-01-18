package cc.dreamcode.kowal.libs.cc.dreamcode.platform.component;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injectable;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamLogger;
import java.lang.reflect.Method;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.Map;
import lombok.NonNull;
import java.lang.annotation.Annotation;

public interface ComponentMethodResolver<T extends Annotation>
{
    boolean isAssignableFrom(@NonNull final Class<T> type);
    
    String getComponentName();
    
    Map<String, Object> getMetas(@NonNull final T t);
    
    void apply(@NonNull final Injector injector, @NonNull final T t, @NonNull final Method method, @NonNull final Object instance);
    
    default void register(@NonNull final Injector injector, @NonNull final T t, @NonNull final Method method, @NonNull final Object instance, final boolean debug) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (t == null) {
            throw new NullPointerException("t is marked non-null but is null");
        }
        if (method == null) {
            throw new NullPointerException("method is marked non-null but is null");
        }
        if (instance == null) {
            throw new NullPointerException("instance is marked non-null but is null");
        }
        final long start = System.currentTimeMillis();
        this.apply(injector, t, method, instance);
        final long took = System.currentTimeMillis() - start;
        if (debug) {
            injector.getInjectable("", DreamLogger.class).map(Injectable::getObject).ifPresent(dreamLogger -> dreamLogger.info(new DreamLogger.Builder().type("Added " + this.getComponentName() + " method component").name(instance.getClass().getSimpleName()).took(took).meta(this.getMetas(t)).build()));
        }
    }
}
