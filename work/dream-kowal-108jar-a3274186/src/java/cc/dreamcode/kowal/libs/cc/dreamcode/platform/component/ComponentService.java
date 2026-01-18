package cc.dreamcode.kowal.libs.cc.dreamcode.platform.component;

import lombok.Generated;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;

public final class ComponentService
{
    private final Injector injector;
    private final List<ComponentClassResolver> classResolvers;
    private final List<ComponentMethodResolver> methodResolvers;
    private final ComponentClassResolver defaultClassResolver;
    private boolean debug;
    
    public ComponentService(final Injector injector) {
        this.classResolvers = new ArrayList<>();
        this.methodResolvers = new ArrayList<>();
        this.debug = true;
        this.injector = injector;
        this.defaultClassResolver = injector.createInstance(RawObjectResolver.class);
    }
    
    public void registerResolver(@NonNull final Class<? extends ComponentClassResolver> classResolver) {
        if (classResolver == null) {
            throw new NullPointerException("classResolver is marked non-null but is null");
        }
        this.classResolvers.removeIf(componentClassResolver -> classResolver.isAssignableFrom(componentClassResolver.getClass()));
        final ComponentClassResolver componentClassResolver = this.injector.createInstance(classResolver);
        this.classResolvers.add(componentClassResolver);
    }
    
    public void registerMethodResolver(@NonNull final Class<? extends ComponentMethodResolver> methodResolver) {
        if (methodResolver == null) {
            throw new NullPointerException("methodResolver is marked non-null but is null");
        }
        this.methodResolvers.removeIf(componentMethodResolver -> methodResolver.isAssignableFrom(componentMethodResolver.getClass()));
        final ComponentMethodResolver componentMethodResolver = this.injector.createInstance(methodResolver);
        this.methodResolvers.add(componentMethodResolver);
    }
    
    public <T> void registerComponent(@NonNull final Class<T> componentClass, final Consumer<T> consumer) {
        try {
            if (componentClass == null) {
                throw new NullPointerException("componentClass is marked non-null but is null");
            }
            final AtomicReference<ComponentClassResolver> reference = new AtomicReference<>(this.defaultClassResolver);
            for (final ComponentClassResolver classResolver : this.classResolvers) {
                if (classResolver.isAssignableFrom(componentClass)) {
                    reference.set(classResolver);
                }
            }
            @SuppressWarnings("unchecked")
            final ComponentClassResolver<T> classResolver = (ComponentClassResolver<T>)reference.get();
            final T t = classResolver.register(this.injector, componentClass, this.debug);
            if (consumer != null) {
                consumer.accept(t);
            }
            for (final Method declaredMethod : componentClass.getDeclaredMethods()) {
                for (final Annotation annotation : declaredMethod.getAnnotations()) {
                    final Class<? extends Annotation> annotationClass = annotation.getClass();
                    this.methodResolvers.stream().filter(resolver -> resolver.isAssignableFrom(annotationClass)).forEach(resolver -> resolver.register(this.injector, annotation, declaredMethod, t, this.debug));
                }
            }
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    public void registerComponent(@NonNull final Class<?> componentClass) {
        if (componentClass == null) {
            throw new NullPointerException("componentClass is marked non-null but is null");
        }
        this.registerComponent(componentClass, null);
    }
    
    public void registerExtension(@NonNull final ComponentExtension componentExtension) {
        if (componentExtension == null) {
            throw new NullPointerException("componentExtension is marked non-null but is null");
        }
        componentExtension.register(this);
    }
    
    public void registerExtension(@NonNull final Class<? extends ComponentExtension> extensionClass) {
        if (extensionClass == null) {
            throw new NullPointerException("extensionClass is marked non-null but is null");
        }
        final ComponentExtension componentExtension = this.injector.createInstance(extensionClass);
        this.registerExtension(componentExtension);
    }
    
    @Generated
    public boolean isDebug() {
        return this.debug;
    }
    
    @Generated
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }
}
