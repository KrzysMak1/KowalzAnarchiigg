package cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.ConfigManager;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.serializer.ItemMetaSerializer;
import org.bukkit.inventory.meta.ItemMeta;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.serializer.ItemStackSerializer;
import org.bukkit.inventory.ItemStack;
import java.io.File;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.SerdesCommons;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.DreamBukkitConfig;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.Injector;
import java.util.stream.Collectors;
import java.lang.reflect.Field;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.exception.PlatformException;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.bukkit.component.configuration.Configuration;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.DreamPlatform;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;
import cc.dreamcode.kowal.libs.cc.dreamcode.platform.component.ComponentClassResolver;

public class ConfigurationResolver implements ComponentClassResolver<OkaeriConfig>
{
    private final DreamPlatform dreamPlatform;
    
    @Inject
    public ConfigurationResolver(final DreamPlatform dreamPlatform) {
        this.dreamPlatform = dreamPlatform;
    }
    
    @Override
    public boolean isAssignableFrom(@NonNull final Class<OkaeriConfig> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return OkaeriConfig.class.isAssignableFrom(type);
    }
    
    @Override
    public String getComponentName() {
        return "configuration";
    }
    
    @Override
    public Map<String, Object> getMetas(@NonNull final OkaeriConfig okaeriConfig) {
        if (okaeriConfig == null) {
            throw new NullPointerException("okaeriConfig is marked non-null but is null");
        }
        final Configuration configuration = okaeriConfig.getClass().getAnnotation(Configuration.class);
        if (configuration == null) {
            throw new PlatformException("OkaeriConfig must have @Configuration annotation.");
        }
        return (Map<String, Object>)new MapBuilder<String, String>().put("path", configuration.child()).put("sub-configs", (String)Arrays.stream((Object[])okaeriConfig.getClass().getDeclaredFields()).filter(field -> OkaeriConfig.class.isAssignableFrom(field.getType())).map(Field::getName).collect(Collectors.joining((CharSequence)", "))).build();
    }
    
    @Override
    public OkaeriConfig resolve(@NonNull final Injector injector, @NonNull final Class<OkaeriConfig> type) {
        if (injector == null) {
            throw new NullPointerException("injector is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        final Configuration configuration = type.getAnnotation(Configuration.class);
        if (configuration == null) {
            throw new PlatformException("OkaeriConfig must have @Configuration annotation.");
        }
        if (!DreamBukkitConfig.class.isAssignableFrom(this.dreamPlatform.getClass())) {
            throw new PlatformException(this.dreamPlatform.getClass().getSimpleName() + " must have DreamBukkitConfig implementation.");
        }
        final DreamBukkitConfig dreamBukkitConfig = (DreamBukkitConfig)this.dreamPlatform;
        return ConfigManager.create(type, it -> {
            it.withConfigurer(new YamlBukkitConfigurer(), new SerdesBukkit(), new SerdesCommons());
            it.withBindFile(new File(this.dreamPlatform.getDataFolder(), configuration.child()));
            it.withRemoveOrphans(configuration.removeOrphans());
            it.withSerdesPack(registry -> {
                registry.registerExclusive(ItemStack.class, new ItemStackSerializer());
                registry.registerExclusive(ItemMeta.class, new ItemMetaSerializer());
                registry.register(dreamBukkitConfig.getConfigSerdesPack());
                return;
            });
            it.saveDefaults();
            it.load(true);
        });
    }
}
