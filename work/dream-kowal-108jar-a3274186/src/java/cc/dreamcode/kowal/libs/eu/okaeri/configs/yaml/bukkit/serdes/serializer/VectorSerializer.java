package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.util.Vector;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class VectorSerializer implements ObjectSerializer<Vector>
{
    @Override
    public boolean supports(@NonNull final Class<? super Vector> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Vector.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final Vector object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("x", object.getX());
        data.add("y", object.getY());
        data.add("z", object.getZ());
    }
    
    @Override
    public Vector deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final double x = data.get("x", (Class<Double>)Double.TYPE);
        final double y = data.get("y", (Class<Double>)Double.TYPE);
        final double z = data.get("z", (Class<Double>)Double.TYPE);
        return new Vector(x, y, z);
    }
}
