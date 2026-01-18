package cc.dreamcode.kowal.libs.eu.okaeri.injector;

import java.lang.reflect.Parameter;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.exception.InjectorException;
import lombok.NonNull;

public interface Injector
{
     <T> Injector registerInjectable(@NonNull final String name, @NonNull final T object, @NonNull final Class<T> type) throws InjectorException;
    
    List<Injectable> all();
    
    Stream<Injectable> stream();
    
    void removeIf(@NonNull final Predicate<Injectable> predicate);
    
     <T> List<Injectable<T>> allOf(@NonNull final Class<T> type);
    
     <T> Stream<Injectable<T>> streamInjectableOf(@NonNull final Class<T> type);
    
     <T> Stream<T> streamOf(@NonNull final Class<T> type);
    
    default <T> Injector registerInjectable(@NonNull final T object) throws InjectorException {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        final Class<T> objectClazz = (Class<T>)object.getClass();
        return this.registerInjectable("", object, objectClazz);
    }
    
    default <T> Injector registerInjectable(@NonNull final String name, @NonNull final T object) throws InjectorException {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        final Class<T> objectClazz = (Class<T>)object.getClass();
        return this.registerInjectable(name, object, objectClazz);
    }
    
    default <T> Injector registerExclusive(@NonNull final String name, @NonNull final T object) throws InjectorException {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        final Class<T> type = (Class<T>)object.getClass();
        this.removeIf((Predicate<Injectable>)(injectable -> name.equals(injectable.getName()) && (type.isAssignableFrom(injectable.getType()) || injectable.getType().isAssignableFrom(type))));
        return this.registerInjectable(name, object, type);
    }
    
    default <T> Injector registerExclusive(@NonNull final String name, @NonNull final T object, @NonNull final Class<T> type) throws InjectorException {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        this.removeIf((Predicate<Injectable>)(injectable -> name.equals(injectable.getName()) && type.isAssignableFrom(injectable.getType())));
        return this.registerInjectable(name, object, (Class<Object>)type);
    }
    
    default <T> Optional<? extends Injectable<T>> getInjectable(@NonNull final String name, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final Injectable<T> value = (Injectable<T>)this.getInjectableExact(name, (Class<Object>)type).orElse(null);
        if (value == null && !"".equals(name)) {
            return (Optional<? extends Injectable<T>>)this.getInjectableExact("", (Class<Object>)type);
        }
        return (Optional<? extends Injectable<T>>)Optional.ofNullable(value);
    }
    
    default <T> Optional<T> get(@NonNull final String name, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<T>)this.getInjectable(name, (Class<Object>)type).map(Injectable::getObject);
    }
    
    default <T> T getOrThrow(@NonNull final String name, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (T)this.get(name, type).orElseThrow(() -> new InjectorException("no injectable for " + name + " of type " + (Object)type));
    }
    
     <T> Optional<? extends Injectable<T>> getInjectableExact(@NonNull final String name, @NonNull final Class<T> type);
    
    default <T> Optional<T> getExact(@NonNull final String name, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Optional<T>)this.getInjectableExact(name, (Class<Object>)type).map(Injectable::getObject);
    }
    
    default <T> T getExactOrThrow(@NonNull final String name, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (T)this.getExact(name, type).orElseThrow(() -> new InjectorException("no exact injectable for " + name + " of type " + (Object)type));
    }
    
     <T> T createInstance(@NonNull final Class<T> clazz) throws InjectorException;
    
     <T> T injectFields(@NonNull final T instance) throws InjectorException;
    
     <T> T invokePostConstructs(@NonNull final T instance) throws InjectorException;
    
    Object invoke(@NonNull final Constructor constructor) throws InjectorException;
    
    Object invoke(@NonNull final Object object, @NonNull final Method method) throws InjectorException;
    
    Object[] fillParameters(@NonNull final Parameter[] parameters, final boolean force) throws InjectorException;
}
