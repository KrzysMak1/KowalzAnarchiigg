package cc.dreamcode.kowal.npc;

import lombok.Generated;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NpcSelectionService {
    private static final long SELECTION_TIMEOUT_MS = 30_000L;
    private final Map<UUID, Long> pendingSelections = new HashMap<>();

    public void requestSelection(final Player player) {
        this.pendingSelections.put(player.getUniqueId(), System.currentTimeMillis() + SELECTION_TIMEOUT_MS);
    }

    public boolean consumeSelection(final Player player) {
        return this.consumeSelection(player.getUniqueId());
    }

    public void clearSelection(final Player player) {
        this.pendingSelections.remove(player.getUniqueId());
    }

    private boolean consumeSelection(final UUID playerUuid) {
        final Long expiresAt = this.pendingSelections.get(playerUuid);
        if (expiresAt == null) {
            return false;
        }
        if (System.currentTimeMillis() > expiresAt) {
            this.pendingSelections.remove(playerUuid);
            return false;
        }
        this.pendingSelections.remove(playerUuid);
        return true;
    }

    @Generated
    public NpcSelectionService() {
    }
}
