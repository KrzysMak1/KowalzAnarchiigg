package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import org.bukkit.potion.PotionEffectType;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringPotionEffectTypeTransformer extends BidirectionalTransformer<String, PotionEffectType>
{
    @Override
    public GenericsPair<String, PotionEffectType> getPair() {
        return this.genericsPair(String.class, PotionEffectType.class);
    }
    
    @Override
    public PotionEffectType leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return PotionEffectType.getByName(data);
    }
    
    @Override
    public String rightToLeft(@NonNull final PotionEffectType data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return data.getName();
    }
}
