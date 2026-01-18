package cc.dreamcode.kowal.libs.eu.okaeri.configs;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import java.util.stream.Stream;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;
import java.util.Optional;
import java.util.Objects;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.util.UnsafeUtil;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import lombok.NonNull;

public final class ConfigManager
{
    public static <T extends OkaeriConfig> T create(@NonNull final Class<T> clazz) throws OkaeriException {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        T config;
        try {
            config = clazz.newInstance();
        }
        catch (final InstantiationException | IllegalAccessException exception) {
            throw new OkaeriException("cannot create " + clazz.getSimpleName() + " instance: make sure default constructor is available or if subconfig use new instead");
        }
        return initialize(config);
    }
    
    public static <T extends OkaeriConfig> T createUnsafe(@NonNull final Class<T> clazz) throws OkaeriException {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        return initialize((T)UnsafeUtil.allocateInstance((Class<T>)clazz));
    }
    
    public static <T extends OkaeriConfig> T create(@NonNull final Class<T> clazz, @NonNull final OkaeriConfigInitializer initializer) throws OkaeriException {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        if (initializer == null) {
            throw new NullPointerException("initializer is marked non-null but is null");
        }
        final T config = create(clazz);
        try {
            initializer.apply(config);
        }
        catch (final Exception exception) {
            if (config.getConfigurer() != null) {
                throw new OkaeriException("failed to initialize " + clazz.getName() + " [" + (Object)config.getConfigurer().getClass() + "]", (Throwable)exception);
            }
            throw new OkaeriException("failed to initialize " + clazz.getName(), (Throwable)exception);
        }
        return config;
    }
    
    public static <T extends OkaeriConfig> T transformCopy(@NonNull final OkaeriConfig config, @NonNull final Class<T> into) throws OkaeriException {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (into == null) {
            throw new NullPointerException("into is marked non-null but is null");
        }
        final T copy = createUnsafe(into);
        final Configurer configurer = config.getConfigurer();
        copy.withConfigurer(configurer);
        final ConfigDeclaration copyDeclaration = copy.getDeclaration();
        if (config.getBindFile() != null) {
            copy.withBindFile(config.getBindFile());
        }
        final Stream stream = configurer.getAllKeys().stream();
        final ConfigDeclaration configDeclaration = copyDeclaration;
        Objects.requireNonNull((Object)configDeclaration);
        stream.map(configDeclaration::getField).filter(Optional::isPresent).map(Optional::get).forEach(field -> {
            Object value = configurer.getValue(field.getName());
            final GenericsDeclaration generics = GenericsDeclaration.of(value);
            if (value != null && (field.getType().getType() != value.getClass() || (!generics.isPrimitiveWrapper() && !generics.isPrimitive()))) {
                value = configurer.resolveType(value, generics, field.getType().getType(), field.getType(), SerdesContext.of(configurer, field));
            }
            field.updateValue(value);
        });
        return copy;
    }
    
    public static <T extends OkaeriConfig> T deepCopy(@NonNull final OkaeriConfig config, @NonNull final Configurer newConfigurer, @NonNull final Class<T> into) throws OkaeriException {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        if (newConfigurer == null) {
            throw new NullPointerException("newConfigurer is marked non-null but is null");
        }
        if (into == null) {
            throw new NullPointerException("into is marked non-null but is null");
        }
        final T copy = createUnsafe(into);
        copy.withConfigurer(newConfigurer, config.getConfigurer().getRegistry().allSerdes());
        copy.withBindFile(config.getBindFile());
        copy.load(config.saveToString());
        return copy;
    }
    
    public static <T extends OkaeriConfig> T initialize(@NonNull final T config) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        config.updateDeclaration();
        return config;
    }
    
    private ConfigManager() {
    }
}
