package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer.experimental;

import java.util.LinkedHashMap;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import java.util.Objects;
import java.util.Map;
import org.bukkit.configuration.file.YamlConfiguration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.yaml.snakeyaml.Yaml;
import org.bukkit.inventory.ItemStack;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class CraftItemStackSerializer implements ObjectSerializer<ItemStack>
{
    private static final Yaml YAML;
    
    @Override
    public boolean supports(@NonNull final Class<? super ItemStack> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ItemStack.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final ItemStack stack, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (stack == null) {
            throw new NullPointerException("stack is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final YamlConfiguration craftConfig = new YamlConfiguration();
        craftConfig.set("_", (Object)stack);
        final Map<String, Map<String, Object>> root = (Map<String, Map<String, Object>>)CraftItemStackSerializer.YAML.load(craftConfig.saveToString());
        final Map<String, Object> itemMap = (Map<String, Object>)root.get((Object)"_");
        itemMap.remove((Object)"==");
        final Map<String, Object> map = itemMap;
        Objects.requireNonNull((Object)data);
        map.forEach(data::add);
    }
    
    @Override
    public ItemStack deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        try {
            if (data == null) {
                throw new NullPointerException("data is marked non-null but is null");
            }
            if (generics == null) {
                throw new NullPointerException("generics is marked non-null but is null");
            }
            final Map<String, Object> itemMap = (Map<String, Object>)new LinkedHashMap();
            itemMap.put((Object)"==", (Object)"org.bukkit.inventory.ItemStack");
            itemMap.putAll((Map)data.asMap());
            final YamlConfiguration craftConfig = new YamlConfiguration();
            craftConfig.set("_", (Object)itemMap);
            craftConfig.loadFromString(craftConfig.saveToString());
            return craftConfig.getItemStack("_");
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    static {
        YAML = new Yaml();
    }
}
