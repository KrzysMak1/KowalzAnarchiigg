package cc.dreamcode.kowal.util;

import cc.dreamcode.utilities.bukkit.StringColorUtil;
import cc.dreamcode.utilities.bukkit.adventure.ColorProcessor;
import cc.dreamcode.utilities.bukkit.builder.ItemBuilder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class MiniMessageUtil {
    private MiniMessageUtil() {
    }

    public static String colorize(final String text) {
        if (text == null) {
            return null;
        }
        return StringColorUtil.fixColor(ColorProcessor.process(text));
    }

    public static String colorize(final String text, final Map<String, ?> placeholders) {
        if (text == null) {
            return null;
        }
        if (placeholders == null || placeholders.isEmpty()) {
            return colorize(text);
        }
        return StringColorUtil.fixColor(ColorProcessor.process(text, toObjectMap(placeholders)));
    }

    public static List<String> colorize(final List<String> lines) {
        if (lines == null) {
            return null;
        }
        final List<String> output = new ArrayList<>();
        for (final String line : lines) {
            if (line == null) {
                continue;
            }
            output.add(colorize(line));
        }
        return output;
    }

    public static List<String> colorize(final List<String> lines, final Map<String, ?> placeholders) {
        if (lines == null) {
            return null;
        }
        final List<String> output = new ArrayList<>();
        for (final String line : lines) {
            if (line == null) {
                continue;
            }
            output.add(colorize(line, placeholders));
        }
        return output;
    }

    public static ItemBuilder applyItemColors(final ItemStack item) {
        return applyItemColors(item, Map.of());
    }

    public static ItemBuilder applyItemColors(final ItemStack item, final Map<String, ?> placeholders) {
        final ItemBuilder builder = ItemBuilder.of(item);
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return builder;
        }
        final String name = meta.getDisplayName();
        if (name != null) {
            builder.setName(colorize(name, placeholders));
        }
        final List<String> lore = meta.getLore();
        if (lore != null) {
            builder.setLore(colorize(lore, placeholders));
        }
        return builder;
    }

    public static ItemBuilder applyItemColors(final ItemBuilder builder, final Map<String, ?> placeholders) {
        final ItemStack item = builder.toItemStack();
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return builder;
        }
        final String name = meta.getDisplayName();
        if (name != null) {
            builder.setName(colorize(name, placeholders));
        }
        final List<String> lore = meta.getLore();
        if (lore != null) {
            builder.setLore(colorize(lore, placeholders));
        }
        return builder;
    }

    private static Map<String, Object> toObjectMap(final Map<String, ?> placeholders) {
        final Map<String, Object> result = new HashMap<>();
        for (final Map.Entry<String, ?> entry : placeholders.entrySet()) {
            result.put(entry.getKey(), entry.getValue() == null ? "" : entry.getValue());
        }
        return result;
    }
}
