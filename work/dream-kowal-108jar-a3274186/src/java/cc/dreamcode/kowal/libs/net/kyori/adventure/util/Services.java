package cc.dreamcode.kowal.libs.net.kyori.adventure.util;

import cc.dreamcode.kowal.libs.net.kyori.adventure.internal.properties.AdventureProperties;
import java.util.Collections;
import java.util.ServiceConfigurationError;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class Services
{
    private static final boolean SERVICE_LOAD_FAILURES_ARE_FATAL;
    
    private Services() {
    }
    
    @NotNull
    public static <P> Optional<P> service(@NotNull final Class<P> type) {
        final ServiceLoader<P> loader = Services0.loader(type);
        final Iterator<P> it = (Iterator<P>)loader.iterator();
        while (it.hasNext()) {
            P instance;
            try {
                instance = (P)it.next();
            }
            catch (final Throwable t) {
                if (Services.SERVICE_LOAD_FAILURES_ARE_FATAL) {
                    throw new IllegalStateException("Encountered an exception loading service " + (Object)type, t);
                }
                continue;
            }
            if (it.hasNext()) {
                throw new IllegalStateException("Expected to find one service " + (Object)type + ", found multiple");
            }
            return (Optional<P>)Optional.of((Object)instance);
        }
        return (Optional<P>)Optional.empty();
    }
    
    @NotNull
    public static <P> Optional<P> serviceWithFallback(@NotNull final Class<P> type) {
        final ServiceLoader<P> loader = Services0.loader(type);
        final Iterator<P> it = (Iterator<P>)loader.iterator();
        P firstFallback = null;
        while (it.hasNext()) {
            P instance;
            try {
                instance = (P)it.next();
            }
            catch (final Throwable t) {
                if (Services.SERVICE_LOAD_FAILURES_ARE_FATAL) {
                    throw new IllegalStateException("Encountered an exception loading service " + (Object)type, t);
                }
                continue;
            }
            if (!(instance instanceof Fallback)) {
                return (Optional<P>)Optional.of((Object)instance);
            }
            if (firstFallback != null) {
                continue;
            }
            firstFallback = instance;
        }
        return (Optional<P>)Optional.ofNullable((Object)firstFallback);
    }
    
    public static <P> Set<P> services(final Class<? extends P> clazz) {
        final ServiceLoader<? extends P> loader = Services0.loader(clazz);
        final Set<P> providers = (Set<P>)new HashSet();
        final Iterator<? extends P> it = (Iterator<? extends P>)loader.iterator();
        while (it.hasNext()) {
            P instance;
            try {
                instance = (P)it.next();
            }
            catch (final ServiceConfigurationError ex) {
                if (Services.SERVICE_LOAD_FAILURES_ARE_FATAL) {
                    throw new IllegalStateException("Encountered an exception loading a provider for " + (Object)clazz + ": ", (Throwable)ex);
                }
                continue;
            }
            providers.add((Object)instance);
        }
        return (Set<P>)Collections.unmodifiableSet((Set)providers);
    }
    
    static {
        SERVICE_LOAD_FAILURES_ARE_FATAL = Boolean.TRUE.equals((Object)AdventureProperties.SERVICE_LOAD_FAILURES_ARE_FATAL.value());
    }
    
    public interface Fallback
    {
    }
}
