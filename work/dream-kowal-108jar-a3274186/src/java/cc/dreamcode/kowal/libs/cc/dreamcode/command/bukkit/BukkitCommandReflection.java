package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit;

import lombok.Generated;
import java.lang.reflect.Field;
import org.bukkit.plugin.SimplePluginManager;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.Server;

public final class BukkitCommandReflection
{
    public static SimpleCommandMap getSimpleCommandMap(final Server server) {
        final SimplePluginManager spm = (SimplePluginManager)server.getPluginManager();
        Field f = null;
        try {
            f = SimplePluginManager.class.getDeclaredField("commandMap");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        assert f != null;
        f.setAccessible(true);
        try {
            return (SimpleCommandMap)f.get((Object)spm);
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Generated
    private BukkitCommandReflection() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
