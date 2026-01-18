package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.serializer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import org.bukkit.potion.PotionEffectType;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import org.bukkit.potion.PotionEffect;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;

public class PotionEffectSerializer implements ObjectSerializer<PotionEffect>
{
    @Override
    public boolean supports(@NonNull final Class<? super PotionEffect> type) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return PotionEffect.class.isAssignableFrom(type);
    }
    
    @Override
    public void serialize(@NonNull final PotionEffect potionEffect, @NonNull final SerializationData data, @NonNull final GenericsDeclaration generics) {
        if (potionEffect == null) {
            throw new NullPointerException("potionEffect is marked non-null but is null");
        }
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        data.add("amplifier", potionEffect.getAmplifier());
        data.add("duration", potionEffect.getDuration());
        data.add("type", potionEffect.getType(), PotionEffectType.class);
    }
    
    @Override
    public PotionEffect deserialize(@NonNull final DeserializationData data, @NonNull final GenericsDeclaration generics) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (generics == null) {
            throw new NullPointerException("generics is marked non-null but is null");
        }
        final int amplifier = data.get("amplifier", Integer.class);
        final int duration = data.get("duration", Integer.class);
        final PotionEffectType potionEffectType = data.get("type", PotionEffectType.class);
        return new PotionEffect(potionEffectType, duration, amplifier);
    }
}
