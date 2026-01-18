package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.suggestion;

import lombok.Generated;
import java.util.stream.Collectors;
import org.bukkit.OfflinePlayer;
import java.util.List;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;

public class AllPlayersSuggestion implements SuggestionSupplier
{
    private final Plugin plugin;
    
    @Override
    public List<String> supply(@NonNull final Class<?> paramType) {
        if (paramType == null) {
            throw new NullPointerException("paramType is marked non-null but is null");
        }
        return (List<String>)this.plugin.getServer().getOnlinePlayers().stream().map(OfflinePlayer::getName).collect(Collectors.toList());
    }
    
    @Generated
    public AllPlayersSuggestion(final Plugin plugin) {
        this.plugin = plugin;
    }
}
