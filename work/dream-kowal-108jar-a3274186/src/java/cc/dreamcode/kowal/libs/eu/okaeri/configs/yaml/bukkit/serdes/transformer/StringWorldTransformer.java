package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer;

import org.bukkit.Bukkit;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import org.bukkit.World;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringWorldTransformer extends BidirectionalTransformer<String, World>
{
    @Override
    public GenericsPair<String, World> getPair() {
        return this.genericsPair(String.class, World.class);
    }
    
    @Override
    public World leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return Bukkit.getWorld(data);
    }
    
    @Override
    public String rightToLeft(@NonNull final World data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return data.getName();
    }
}
