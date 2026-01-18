package cc.dreamcode.kowal.level;

import org.bukkit.Material;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import eu.okaeri.configs.serdes.ObjectSerializer;

public class LevelSerializer implements ObjectSerializer<Level>
{
    @Override
    public boolean supports(@NonNull final Class<? super Level> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Level.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final Level object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("upgrade-items", object.getUpgradeItems());
        data.add("upgrade-items-lore", object.getUpgradeItemsLore());
        data.add("cost-lore", object.getCostLore());
        data.add("money-upgrade", object.getMoneyUpgrade());
        data.add("display-lore", object.getItemLoreDisplay());
        data.add("hp-reduce", object.getHpReduce());
        data.add("chance", object.getChance());
    }
    
    @Override
    public Level deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        return new Level(data.getAsMap("upgrade-items", Material.class, Integer.class), data.get("upgrade-items-lore", String.class), data.get("cost-lore", String.class), data.get("money-upgrade", Double.class), data.get("display-lore", String.class), data.get("hp-reduce", Double.class), data.get("chance", Integer.class));
    }
}
