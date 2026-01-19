package cc.dreamcode.kowal.controller;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import cc.dreamcode.kowal.menu.KowalMenu;
import eu.okaeri.injector.annotation.Inject;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import lombok.Generated;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class FancyNpcController implements Listener {
    private static final Set<String> META_KEYS = Set.of("fancynpcs-id", "fancy-npc-id", "fancy-npcs-id");
    private static final List<String> NPC_MANAGER_METHODS = List.of("getNpcByEntity", "getByEntity", "getNpc", "getNpcByEntityId");
    private static final List<String> NPC_ID_METHODS = List.of("getId", "getName", "getNpcId");
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;

    @EventHandler(ignoreCancelled = true)
    public void onNpcInteract(final PlayerInteractEntityEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!this.isFancyNpcEnabled()) {
            return;
        }
        final String configuredId = this.pluginConfig.fancyNpcId;
        if (configuredId == null || configuredId.isBlank()) {
            return;
        }
        final Optional<String> npcId = this.resolveFancyNpcId(event.getRightClicked());
        if (npcId.isEmpty() || !npcId.get().equalsIgnoreCase(configuredId.trim())) {
            return;
        }
        event.setCancelled(true);
        final Player player = event.getPlayer();
        final KowalMenu menu = this.plugin.createInstance(KowalMenu.class);
        menu.build(player).open(player);
    }

    private boolean isFancyNpcEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("FancyNpcs");
    }

    private Optional<String> resolveFancyNpcId(final Entity entity) {
        final Optional<String> apiId = this.resolveFromApi(entity);
        if (apiId.isPresent()) {
            return apiId;
        }
        final Optional<String> metaId = this.resolveFromMetadata(entity);
        if (metaId.isPresent()) {
            return metaId;
        }
        final Optional<String> pdcId = this.resolveFromPersistentData(entity);
        if (pdcId.isPresent()) {
            return pdcId;
        }
        return this.resolveFromScoreboardTags(entity);
    }

    private Optional<String> resolveFromApi(final Entity entity) {
        try {
            final Class<?> fancyNpcsClass = Class.forName("de.oliver.fancynpcs.api.FancyNpcs");
            final Method getInstance = fancyNpcsClass.getMethod("getInstance");
            final Object fancyNpcs = getInstance.invoke(null);
            final Method getNpcManager = fancyNpcs.getClass().getMethod("getNpcManager");
            final Object manager = getNpcManager.invoke(fancyNpcs);
            final Object npc = resolveNpc(manager, entity);
            if (npc == null) {
                return Optional.empty();
            }
            return resolveNpcId(npc);
        }
        catch (ReflectiveOperationException ex) {
            return Optional.empty();
        }
        catch (RuntimeException ex) {
            this.plugin.getLogger().log(Level.FINE, "FancyNPC api lookup failed.", ex);
            return Optional.empty();
        }
    }

    private Object resolveNpc(final Object manager, final Entity entity) throws ReflectiveOperationException {
        for (final String methodName : NPC_MANAGER_METHODS) {
            try {
                final Method method = manager.getClass().getMethod(methodName, Entity.class);
                return method.invoke(manager, entity);
            }
            catch (NoSuchMethodException ignored) {
                continue;
            }
        }
        return null;
    }

    private Optional<String> resolveNpcId(final Object npc) throws ReflectiveOperationException {
        for (final String methodName : NPC_ID_METHODS) {
            try {
                final Method method = npc.getClass().getMethod(methodName);
                final Object value = method.invoke(npc);
                if (value != null) {
                    return Optional.of(String.valueOf(value));
                }
            }
            catch (NoSuchMethodException ignored) {
                continue;
            }
        }
        return Optional.empty();
    }

    private Optional<String> resolveFromMetadata(final Entity entity) {
        for (final String key : META_KEYS) {
            if (entity.hasMetadata(key)) {
                for (final MetadataValue value : entity.getMetadata(key)) {
                    if (value != null && value.value() != null) {
                        return Optional.of(String.valueOf(value.value()));
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Optional<String> resolveFromPersistentData(final Entity entity) {
        final PersistentDataContainer container = entity.getPersistentDataContainer();
        for (final NamespacedKey key : container.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase("fancynpcs")) {
                continue;
            }
            final String id = readPersistentId(container, key);
            if (id != null && !id.isBlank()) {
                return Optional.of(id);
            }
        }
        final List<String> knownKeys = List.of(
                "fancynpcs:id",
                "fancynpcs:npc-id",
                "fancynpcs:npc_id",
                "fancynpcs:internal-id"
        );
        for (final String keyString : knownKeys) {
            final NamespacedKey key = NamespacedKey.fromString(keyString);
            if (key == null) {
                continue;
            }
            final String id = readPersistentId(container, key);
            if (id != null && !id.isBlank()) {
                return Optional.of(id);
            }
        }
        return Optional.empty();
    }

    private String readPersistentId(final PersistentDataContainer container, final NamespacedKey key) {
        final String textId = container.get(key, PersistentDataType.STRING);
        if (textId != null) {
            return textId;
        }
        final Integer intId = container.get(key, PersistentDataType.INTEGER);
        if (intId != null) {
            return String.valueOf(intId);
        }
        return null;
    }

    private Optional<String> resolveFromScoreboardTags(final Entity entity) {
        for (final String tag : entity.getScoreboardTags()) {
            final String lower = tag.toLowerCase(Locale.ROOT);
            if (lower.startsWith("fancynpcs:") || lower.startsWith("fancy-npc:") || lower.startsWith("fancynpc:")) {
                return Optional.of(tag.substring(tag.indexOf(':') + 1));
            }
        }
        return Optional.empty();
    }

    @Inject
    @Generated
    public FancyNpcController(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
    }
}
