package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.legacyimpl.NBTLegacyHoverEventSerializer;
import org.bukkit.Bukkit;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import java.util.function.Supplier;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.flattener.ComponentFlattener;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Server;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetComponentFlattener;
import java.util.Collection;

public final class BukkitComponentSerializer
{
    private static final boolean IS_1_16;
    private static final Collection<FacetComponentFlattener.Translator<Server>> TRANSLATORS;
    private static final LegacyComponentSerializer LEGACY_SERIALIZER;
    private static final GsonComponentSerializer GSON_SERIALIZER;
    static final ComponentFlattener FLATTENER;
    
    private BukkitComponentSerializer() {
    }
    
    @NotNull
    public static LegacyComponentSerializer legacy() {
        return BukkitComponentSerializer.LEGACY_SERIALIZER;
    }
    
    @NotNull
    public static GsonComponentSerializer gson() {
        return BukkitComponentSerializer.GSON_SERIALIZER;
    }
    
    static {
        IS_1_16 = (MinecraftReflection.findEnum(Material.class, "NETHERITE_PICKAXE") != null);
        TRANSLATORS = Facet.of(SpigotFacet.Translator::new, CraftBukkitFacet.Translator::new);
        FLATTENER = FacetComponentFlattener.get(Bukkit.getServer(), BukkitComponentSerializer.TRANSLATORS);
        if (BukkitComponentSerializer.IS_1_16) {
            LEGACY_SERIALIZER = LegacyComponentSerializer.builder().hexColors().useUnusualXRepeatedCharacterHexFormat().flattener(BukkitComponentSerializer.FLATTENER).build();
            GSON_SERIALIZER = GsonComponentSerializer.builder().legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get()).build();
        }
        else {
            LEGACY_SERIALIZER = LegacyComponentSerializer.builder().character('§').flattener(BukkitComponentSerializer.FLATTENER).build();
            GSON_SERIALIZER = GsonComponentSerializer.builder().legacyHoverEventSerializer(NBTLegacyHoverEventSerializer.get()).emitLegacyHoverEvent().downsampleColors().build();
        }
    }
}
