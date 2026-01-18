package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.serializable;

import java.lang.reflect.Method;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class ConfigSerializableSerializer implements ObjectSerializer<ConfigSerializable>
{
    @Override
    public boolean supports(@NonNull final Class<? super ConfigSerializable> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return ConfigSerializable.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final ConfigSerializable object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        object.serialize(data, generics);
    }
    
    @Override
    public ConfigSerializable deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        try {
            if (data == null) {
                throw new NullPointerException("data is marked non-null but is null");
            }
            if (generics == null) {
                throw new NullPointerException("generics is marked non-null but is null");
            }
            Method deserializeMethod;
            try {
                deserializeMethod = generics.getType().getMethod("deserialize", DeserializationData.class, GenericsDeclaration.class);
            }
            catch (final NoSuchMethodException exception) {
                throw new RuntimeException("public static " + generics.getType().getSimpleName() + " deserialize(DeserializationData, GenericsDeclaration) method missing in ConfigSerializable " + (Object)generics.getType(), (Throwable)exception);
            }
            return (ConfigSerializable)deserializeMethod.invoke((Object)null, new Object[] { data, generics });
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
}
