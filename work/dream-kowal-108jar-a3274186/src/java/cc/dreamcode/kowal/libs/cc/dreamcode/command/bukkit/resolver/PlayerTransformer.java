package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.resolver;

import lombok.Generated;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;

public class PlayerTransformer implements ObjectTransformer<Player>
{
    private final Plugin plugin;
    
    @Override
    public Class<?> getGeneric() {
        return Player.class;
    }
    
    @Override
    public Optional<Player> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return (Optional<Player>)Optional.ofNullable((Object)this.plugin.getServer().getPlayerExact(input));
    }
    
    @Generated
    public PlayerTransformer(final Plugin plugin) {
        this.plugin = plugin;
    }
}
