package cc.dreamcode.kowal.util;

import cc.dreamcode.utilities.bukkit.nbt.ItemNbtUtil;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public final class UpgradeDataUtil {
    private static final String LEVEL_KEY = "upgrade-level";
    private static final String EFFECT_KEY = "upgrade-effect";
    private static final String DISPLAY_NAME_KEY = "display-name";

    private UpgradeDataUtil() {
    }

    public static int getLevel(final Plugin plugin, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return 0;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            final Integer level = container.get(levelKey(plugin), PersistentDataType.INTEGER);
            if (level != null) {
                return level;
            }
            final String levelText = container.get(levelKey(plugin), PersistentDataType.STRING);
            if (levelText != null) {
                return UpgradeUtil.parseLevel(levelText);
            }
        }
        final Object rawLevel = ItemNbtUtil.getValueByPlugin(plugin, item, LEVEL_KEY).orElse("0");
        return UpgradeUtil.parseLevel(rawLevel);
    }

    public static void setLevel(final Plugin plugin, final ItemStack item, final int level) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(levelKey(plugin), PersistentDataType.INTEGER, level);
            item.setItemMeta(meta);
        }
        ItemNbtUtil.setValue(plugin, item, LEVEL_KEY, String.valueOf(level));
    }

    public static String getEffect(final Plugin plugin, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return "none";
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            final String effect = container.get(effectKey(plugin), PersistentDataType.STRING);
            if (effect != null) {
                return effect;
            }
        }
        return (String)ItemNbtUtil.getValueByPlugin(plugin, item, EFFECT_KEY).orElse("none");
    }

    public static void setEffect(final Plugin plugin, final ItemStack item, final String effect) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(effectKey(plugin), PersistentDataType.STRING, effect);
            item.setItemMeta(meta);
        }
        ItemNbtUtil.setValue(plugin, item, EFFECT_KEY, effect);
    }

    public static Optional<String> getDisplayName(final Plugin plugin, final ItemStack item) {
        if (item == null || item.getType() == Material.AIR) {
            return Optional.empty();
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            final PersistentDataContainer container = meta.getPersistentDataContainer();
            final String displayName = container.get(displayNameKey(plugin), PersistentDataType.STRING);
            if (displayName != null) {
                return Optional.of(displayName);
            }
        }
        return ItemNbtUtil.getValueByPlugin(plugin, item, DISPLAY_NAME_KEY).map(String.class::cast);
    }

    public static void setDisplayName(final Plugin plugin, final ItemStack item, final String displayName) {
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        final ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.getPersistentDataContainer().set(displayNameKey(plugin), PersistentDataType.STRING, displayName);
            item.setItemMeta(meta);
        }
        ItemNbtUtil.setValue(plugin, item, DISPLAY_NAME_KEY, displayName);
    }

    private static NamespacedKey levelKey(final Plugin plugin) {
        return new NamespacedKey(plugin, LEVEL_KEY);
    }

    private static NamespacedKey effectKey(final Plugin plugin) {
        return new NamespacedKey(plugin, EFFECT_KEY);
    }

    private static NamespacedKey displayNameKey(final Plugin plugin) {
        return new NamespacedKey(plugin, DISPLAY_NAME_KEY);
    }
}
