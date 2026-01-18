package cc.dreamcode.kowal.libs.cc.dreamcode.platform;

import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentExtension;
import java.io.File;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentService;

public interface DreamPlatform
{
    void enable(@NonNull final ComponentService componentService);
    
    void disable();
    
    File getDataFolder();
    
    @NonNull
    DreamLogger getDreamLogger();
    
    @NonNull
    DreamVersion getDreamVersion();
    
    @NonNull
    ComponentService getComponentService();
    
     <T> T registerInjectable(@NonNull final String name, @NonNull final T t);
    
     <T> T registerInjectable(@NonNull final String name, final Class<T> type);
    
     <T> T registerInjectable(@NonNull final T t);
    
     <T> T registerInjectable(final Class<T> type);
    
    default void registerExtension(@NonNull final ComponentExtension extension) {
        if (extension == null) {
            throw new NullPointerException("extension is marked non-null but is null");
        }
        this.getComponentService().registerExtension(extension);
    }
    
    default void registerExtension(@NonNull final Class<? extends ComponentExtension> extensionClass) {
        if (extensionClass == null) {
            throw new NullPointerException("extensionClass is marked non-null but is null");
        }
        this.getComponentService().registerExtension(extensionClass);
    }
    
     <T> T createInstance(@NonNull final Class<T> type);
    
     <T> Optional<T> getInject(@NonNull final String name, @NonNull final Class<T> value);
    
     <T> Optional<T> getInject(@NonNull final Class<T> value);
}
