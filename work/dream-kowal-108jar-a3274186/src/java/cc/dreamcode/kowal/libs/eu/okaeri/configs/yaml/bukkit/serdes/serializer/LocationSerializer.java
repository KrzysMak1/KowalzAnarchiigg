package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import org.bukkit.World;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.Location;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class LocationSerializer implements ObjectSerializer<Location>
{
    @Override
    public boolean supports(@NonNull final Class<? super Location> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Location.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final Location location, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (location == null) {
            throw new NullPointerException("location is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("world", location.getWorld(), World.class);
        data.add("x", location.getX());
        data.add("y", location.getY());
        data.add("z", location.getZ());
        data.add("yaw", location.getYaw());
        data.add("pitch", location.getPitch());
    }
    
    @Override
    public Location deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final World world = data.get("world", World.class);
        final double x = data.get("x", Double.class);
        final double y = data.get("y", Double.class);
        final double z = data.get("z", Double.class);
        final float yaw = data.get("yaw", Float.class);
        final float pitch = data.get("pitch", Float.class);
        return new Location(world, x, y, z, yaw, pitch);
    }
}
