package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.resolver;

import lombok.Generated;
import java.util.Arrays;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.OfflinePlayer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;

public class OfflinePlayerTransformer implements ObjectTransformer<OfflinePlayer>
{
    private final Plugin plugin;
    
    @Override
    public Class<?> getGeneric() {
        return OfflinePlayer.class;
    }
    
    @Override
    public Optional<OfflinePlayer> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return (Optional<OfflinePlayer>)Arrays.stream((Object[])this.plugin.getServer().getOfflinePlayers()).filter(offlinePlayer -> offlinePlayer.getName() != null).filter(offlinePlayer -> offlinePlayer.getName().equalsIgnoreCase(input)).findAny();
    }
    
    @Generated
    public OfflinePlayerTransformer(final Plugin plugin) {
        this.plugin = plugin;
    }
}
