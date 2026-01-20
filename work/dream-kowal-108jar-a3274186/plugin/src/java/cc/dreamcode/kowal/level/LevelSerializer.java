package cc.dreamcode.kowal.level;

import org.bukkit.Material;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import eu.okaeri.configs.serdes.ObjectSerializer;
import java.util.Map;

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
        data.add("requirements.items", object.getUpgradeItems());
        data.add("requirements.money", object.getMoneyUpgrade());
        data.add("chance", object.getChance());
        data.add("bonus.displayLore", object.getItemLoreDisplay());
        data.add("bonus.hpReduce", object.getHpReduce());
        data.add("bonus.hpReducePercent", object.getHpReducePercent());
    }
    
    @Override
    public Level deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final Double hpReduce = data.get("bonus.hpReduce", Double.class);
        final Double hpReducePercent = data.get("bonus.hpReducePercent", Double.class);
        final Double legacyHpReduce = data.get("hp-reduce", Double.class);
        final Double legacyHpReducePercent = data.get("hp-reduce-percent", Double.class);
        final Map<Material, Integer> requirementsItems = data.getAsMap("requirements.items", Material.class, Integer.class);
        final Double requirementsMoney = data.get("requirements.money", Double.class);
        final String displayLore = data.get("bonus.displayLore", String.class);
        final Map<Material, Integer> legacyItems = data.getAsMap("upgrade-items", Material.class, Integer.class);
        final String legacyDisplayLore = data.get("display-lore", String.class);
        return new Level(
                requirementsItems != null ? requirementsItems : legacyItems,
                data.get("upgrade-items-lore", String.class),
                data.get("cost-lore", String.class),
                requirementsMoney != null ? requirementsMoney : data.get("money-upgrade", Double.class),
                displayLore != null ? displayLore : legacyDisplayLore,
                hpReduce != null ? hpReduce : (legacyHpReduce == null ? 0.0 : legacyHpReduce),
                hpReducePercent != null ? hpReducePercent : (legacyHpReducePercent == null ? 0.0 : legacyHpReducePercent),
                data.get("chance", Integer.class));
    }
}
