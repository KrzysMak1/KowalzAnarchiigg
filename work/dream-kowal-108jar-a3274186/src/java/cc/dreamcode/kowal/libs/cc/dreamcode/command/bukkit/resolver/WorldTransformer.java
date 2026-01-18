package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.resolver;

import lombok.Generated;
import java.util.Optional;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import org.bukkit.World;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;

public class WorldTransformer implements ObjectTransformer<World>
{
    private final Plugin plugin;
    
    @Override
    public Class<?> getGeneric() {
        return World.class;
    }
    
    @Override
    public Optional<World> transform(@NonNull final Class<?> type, @NonNull final String input) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        return (Optional<World>)Optional.ofNullable((Object)this.plugin.getServer().getWorld(input));
    }
    
    @Generated
    public WorldTransformer(final Plugin plugin) {
        this.plugin = plugin;
    }
}
