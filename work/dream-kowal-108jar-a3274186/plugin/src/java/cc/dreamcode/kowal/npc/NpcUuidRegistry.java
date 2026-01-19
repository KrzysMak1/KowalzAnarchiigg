package cc.dreamcode.kowal.npc;

import cc.dreamcode.kowal.KowalPlugin;
import cc.dreamcode.kowal.config.PluginConfig;
import eu.okaeri.injector.annotation.Inject;
import lombok.Generated;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class NpcUuidRegistry {
    private final KowalPlugin plugin;
    private final PluginConfig pluginConfig;
    private Set<UUID> npcUuids;

    public void reload() {
        final Set<UUID> parsed = new HashSet<>();
        final PluginConfig.NpcSettings npcSettings = this.pluginConfig.npc;
        if (npcSettings != null) {
            final List<String> configured = npcSettings.uuids;
            if (configured != null) {
                for (final String entry : configured) {
                    if (entry == null || entry.isBlank()) {
                        continue;
                    }
                    try {
                        parsed.add(UUID.fromString(entry.trim()));
                    }
                    catch (final IllegalArgumentException ex) {
                        this.plugin.getLogger().warning("Config: niepoprawny UUID NPC: " + entry);
                    }
                }
            }
        }
        this.npcUuids = Collections.unmodifiableSet(parsed);
    }

    public boolean isNpc(final UUID uuid) {
        if (uuid == null || this.npcUuids == null) {
            return false;
        }
        return this.npcUuids.contains(uuid);
    }

    @Inject
    @Generated
    public NpcUuidRegistry(final KowalPlugin plugin, final PluginConfig pluginConfig) {
        this.plugin = plugin;
        this.pluginConfig = pluginConfig;
        this.npcUuids = Collections.emptySet();
    }
}
