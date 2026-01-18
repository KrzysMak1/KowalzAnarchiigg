package cc.dreamcode.kowal.effect;

import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import eu.okaeri.configs.serdes.ObjectSerializer;

public class EffectSerializer implements ObjectSerializer<Effect>
{
    @Override
    public boolean supports(@NonNull final Class<? super Effect> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return Effect.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final Effect object, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("lore-display", object.getLore());
        data.add("amplifier-or-chance", object.getAmplifierChance());
    }
    
    @Override
    public Effect deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        return new Effect(data.get("lore-display", String.class), data.get("amplifier-or-chance", Integer.class));
    }
}
