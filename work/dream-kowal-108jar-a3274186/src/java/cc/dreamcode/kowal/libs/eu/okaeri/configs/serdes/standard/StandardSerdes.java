package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.serializable.ConfigSerializableSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;

public class StandardSerdes implements OkaeriSerdesPack
{
    @Override
    public void register(@NonNull final SerdesRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is marked non-null but is null");
        }
        registry.register(new ObjectToStringTransformer());
        registry.register(new StringToStringTransformer());
        registry.registerWithReversedToString(new StringToBigDecimalTransformer());
        registry.registerWithReversedToString(new StringToBigIntegerTransformer());
        registry.registerWithReversedToString(new StringToBooleanTransformer());
        registry.registerWithReversedToString(new StringToByteTransformer());
        registry.registerWithReversedToString(new StringToCharacterTransformer());
        registry.registerWithReversedToString(new StringToDoubleTransformer());
        registry.registerWithReversedToString(new StringToFloatTransformer());
        registry.registerWithReversedToString(new StringToIntegerTransformer());
        registry.registerWithReversedToString(new StringToLongTransformer());
        registry.registerWithReversedToString(new StringToShortTransformer());
        registry.registerWithReversedToString(new StringToUuidTransformer());
        registry.register(new ConfigSerializableSerializer());
    }
}
