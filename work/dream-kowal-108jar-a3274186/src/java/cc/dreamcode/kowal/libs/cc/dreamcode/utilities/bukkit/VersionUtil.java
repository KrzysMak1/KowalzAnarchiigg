package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.bukkit;

import lombok.Generated;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.ParseUtil;
import org.bukkit.Server;
import org.bukkit.Bukkit;
import java.util.Optional;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.ClassUtil;

public final class VersionUtil
{
    public static boolean isSupported(final int version) {
        return (int)getVersion().orElse((Object)(-1)) >= version;
    }
    
    public static boolean isSpigot() {
        return ClassUtil.hasClass("org.spigotmc.SpigotConfig");
    }
    
    public static boolean isPaper() {
        return ClassUtil.hasClass("com.destroystokyo.paper.PaperConfig") || ClassUtil.hasClass("io.papermc.paper.configuration.Configuration");
    }
    
    public static Optional<String> getStringVersion() {
        final String[] nmsVersionSplit = Bukkit.getServer().getClass().getPackage().getName().split("\\.");
        if (nmsVersionSplit.length < 4) {
            return (Optional<String>)Optional.empty();
        }
        final String nmsVersion = nmsVersionSplit[3];
        if (nmsVersion.startsWith("v")) {
            return (Optional<String>)Optional.of((Object)nmsVersion);
        }
        return (Optional<String>)Optional.empty();
    }
    
    public static Optional<Integer> getVersion() {
        final Optional<String> optionalVersion = getStringVersion();
        if (!optionalVersion.isPresent()) {
            try {
                final Method getMinecraftVersion = Server.class.getMethod("getMinecraftVersion", (Class<?>[])new Class[0]);
                final String minecraftVersion = (String)getMinecraftVersion.invoke((Object)Bukkit.getServer(), new Object[0]);
                final String[] minecraftVersionSplit = minecraftVersion.split("\\.");
                if (minecraftVersionSplit.length <= 1) {
                    return (Optional<Integer>)Optional.empty();
                }
                final String minorVersion = minecraftVersion.split("\\.")[1];
                return ParseUtil.parseInteger(minorVersion);
            }
            catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                return (Optional<Integer>)Optional.empty();
            }
        }
        final String version = (String)optionalVersion.get();
        try {
            return ParseUtil.parseInteger(version.substring(1).split("_")[1]);
        }
        catch (final Exception e2) {
            return (Optional<Integer>)Optional.empty();
        }
    }
    
    @Generated
    private VersionUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
