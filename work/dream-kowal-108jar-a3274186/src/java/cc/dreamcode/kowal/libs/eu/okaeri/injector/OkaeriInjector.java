package cc.dreamcode.kowal.libs.eu.okaeri.injector;

import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.PostConstruct;
import java.lang.reflect.Parameter;
import java.lang.reflect.InvocationTargetException;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import java.util.Comparator;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.Collections;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.exception.InjectorException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import lombok.NonNull;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class OkaeriInjector implements Injector
{
    private final List<Injectable> injectables;
    private final boolean unsafe;
    
    public static OkaeriInjector create() {
        return create(false);
    }
    
    public static OkaeriInjector create(final boolean unsafe) {
        return create((List<Injectable>)new CopyOnWriteArrayList(), unsafe);
    }
    
    public static OkaeriInjector create(@NonNull final List<Injectable> injectables, final boolean unsafe) {
        if (injectables == null) {
            throw new NullPointerException("injectables is marked non-null but is null");
        }
        return new OkaeriInjector(injectables, unsafe);
    }
    
    private static Object allocateInstance(@NonNull final Class<?> clazz) throws Exception {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        final Class<?> unsafeClazz = Class.forName("sun.misc.Unsafe");
        final Field theUnsafeField = unsafeClazz.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        final Object unsafeInstance = theUnsafeField.get((Object)null);
        final Method allocateInstance = unsafeClazz.getDeclaredMethod("allocateInstance", Class.class);
        return allocateInstance.invoke(unsafeInstance, new Object[] { clazz });
    }
    
    private static <T> T tryCreateInstance(@NonNull final Class<T> clazz, final boolean unsafe) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        T instance;
        try {
            instance = clazz.newInstance();
        }
        catch (final InstantiationException | IllegalAccessException exception0) {
            if (!unsafe) {
                throw new InjectorException("Cannot initialize new instance of " + (Object)clazz, (Throwable)exception0);
            }
            try {
                instance = (T)allocateInstance(clazz);
            }
            catch (final Exception exception2) {
                throw new InjectorException("Cannot (unsafe) initialize new instance of " + (Object)clazz, (Throwable)exception2);
            }
        }
        return instance;
    }
    
    @Override
    public List<Injectable> all() {
        return (List<Injectable>)Collections.unmodifiableList((List)this.injectables);
    }
    
    @Override
    public Stream<Injectable> stream() {
        return (Stream<Injectable>)this.all().stream();
    }
    
    @Override
    public void removeIf(@NonNull final Predicate<Injectable> filter) {
        if (filter == null) {
            throw new NullPointerException("filter is marked non-null but is null");
        }
        this.injectables.removeIf((Predicate)filter);
    }
    
    @Override
    public <T> List<Injectable<T>> allOf(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final List<Injectable<T>> data = (List<Injectable<T>>)new ArrayList();
        final List found = (List)this.injectables.stream().filter(injectable -> type.isAssignableFrom(injectable.getType())).collect(Collectors.toList());
        data.addAll((Collection)found);
        return (List<Injectable<T>>)Collections.unmodifiableList((List)data);
    }
    
    @Override
    public <T> Stream<Injectable<T>> streamInjectableOf(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Stream<Injectable<T>>)this.injectables.stream().filter(injectable -> type.isAssignableFrom(injectable.getType())).map(injectable -> injectable);
    }
    
    @Override
    public <T> Stream<T> streamOf(@NonNull final Class<T> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return (Stream<T>)this.streamInjectableOf((Class<Object>)type).map(Injectable::getObject);
    }
    
    @Override
    public <T> Injector registerInjectable(@NonNull final String name, @NonNull final T object, @NonNull final Class<T> type) throws InjectorException {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        this.injectables.add(0, (Object)Injectable.of(name, object, type));
        return this;
    }
    
    @Override
    public <T> Optional<? extends Injectable<T>> getInjectableExact(@NonNull final String name, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final Injectable<T> value = (Injectable<T>)this.injectables.stream().filter(injectable -> name.isEmpty() || name.equals((Object)injectable.getName())).filter(injectable -> type.isAssignableFrom(injectable.getType())).findAny().orElse((Object)null);
        return (Optional<? extends Injectable<T>>)Optional.ofNullable((Object)value);
    }
    
    @Override
    public <T> T createInstance(@NonNull final Class<T> clazz) throws InjectorException {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        final List<Constructor<?>> injectableConstructors = (List<Constructor<?>>)Arrays.stream((Object[])clazz.getConstructors()).filter(constructor -> constructor.getAnnotation((Class)Inject.class) != null).collect(Collectors.toList());
        T instance;
        if (injectableConstructors.isEmpty()) {
            instance = tryCreateInstance(clazz, this.unsafe);
        }
        else {
            if (injectableConstructors.size() != 1) {
                throw new InjectorException("Type should not have multiple constructors annotated with @Inject: " + (Object)clazz);
            }
            instance = (T)this.invoke((Constructor)injectableConstructors.get(0));
        }
        this.injectFields(instance);
        this.invokePostConstructs(instance);
        return instance;
    }
    
    @Override
    public <T> T invokePostConstructs(@NonNull final T instance) throws InjectorException {
        if (instance == null) {
            throw new NullPointerException("instance is marked non-null but is null");
        }
        Arrays.stream((Object[])instance.getClass().getDeclaredMethods()).filter(method -> method.getAnnotation((Class)PostConstruct.class) != null).sorted(Comparator.comparingInt(method -> ((PostConstruct)method.getAnnotation((Class)PostConstruct.class)).order())).forEach(method -> {
            try {
                final Object result = this.invoke(instance, method);
                if (result != null) {
                    this.registerInjectable(method.getName(), result);
                }
            }
            catch (final InjectorException exception) {
                throw new InjectorException("Failed to invoke @PostConstruct for instance of " + (Object)instance.getClass(), (Throwable)exception);
            }
        });
        return instance;
    }
    
    @Override
    public <T> T injectFields(@NonNull final T instance) {
        if (instance == null) {
            throw new NullPointerException("instance is marked non-null but is null");
        }
        final Class<?> clazz = instance.getClass();
        final Field[] declaredFields;
        final Field[] fields = declaredFields = clazz.getDeclaredFields();
        for (final Field field : declaredFields) {
            final Inject inject = (Inject)field.getAnnotation((Class)Inject.class);
            if (inject != null) {
                String name = inject.value();
                Optional<? extends Injectable<?>> injectableOptional;
                if (name.isEmpty()) {
                    name = field.getName();
                    injectableOptional = this.getInjectable(name, (Class<Object>)field.getType());
                }
                else {
                    injectableOptional = this.getInjectableExact(name, (Class<Object>)field.getType());
                }
                if (!injectableOptional.isPresent()) {
                    throw new InjectorException("cannot resolve " + (Object)inject + " " + (Object)field.getType() + " [" + field.getName() + "] in instance of " + (Object)clazz);
                }
                final Injectable<?> injectable = (Injectable<?>)injectableOptional.get();
                field.setAccessible(true);
                try {
                    field.set((Object)instance, (Object)injectable.getObject());
                }
                catch (final IllegalAccessException exception) {
                    throw new InjectorException("cannot inject " + (Object)injectable + " to instance of " + (Object)clazz, (Throwable)exception);
                }
            }
        }
        return instance;
    }
    
    @Override
    public Object invoke(@NonNull final Constructor constructor) throws InjectorException {
        if (constructor == null) {
            throw new NullPointerException("constructor is marked non-null but is null");
        }
        constructor.setAccessible(true);
        final Object[] call = this.fillParameters(constructor.getParameters(), true);
        try {
            return constructor.newInstance(call);
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            throw new InjectorException("Error invoking " + (Object)constructor, (Throwable)exception);
        }
    }
    
    @Override
    public Object invoke(@NonNull final Object object, @NonNull final Method method) throws InjectorException {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (method == null) {
            throw new NullPointerException("method is marked non-null but is null");
        }
        method.setAccessible(true);
        final Object[] call = this.fillParameters(method.getParameters(), true);
        try {
            return method.invoke(object, call);
        }
        catch (final Exception exception) {
            throw new InjectorException("Error invoking " + (Object)method, (Throwable)exception);
        }
    }
    
    @Override
    public Object[] fillParameters(@NonNull final Parameter[] parameters, final boolean force) throws InjectorException {
        if (parameters == null) {
            throw new NullPointerException("parameters is marked non-null but is null");
        }
        final Object[] call = new Object[parameters.length];
        for (int i = 0; i < parameters.length; ++i) {
            final Parameter param = parameters[i];
            final Class<?> paramType = param.getType();
            final String name = (param.getAnnotation((Class)Inject.class) != null) ? ((Inject)param.getAnnotation((Class)Inject.class)).value() : "";
            final Optional<? extends Injectable<?>> injectable = this.getInjectable(name, paramType);
            if (!injectable.isPresent()) {
                if (force) {
                    throw new InjectorException("Cannot fill parameters, no injectable of type " + (Object)paramType + " [" + name + "] found");
                }
            }
            else {
                call[i] = paramType.cast(((Injectable)injectable.get()).getObject());
            }
        }
        return call;
    }
    
    private OkaeriInjector(final List<Injectable> injectables, final boolean unsafe) {
        this.injectables = injectables;
        this.unsafe = unsafe;
    }
}
