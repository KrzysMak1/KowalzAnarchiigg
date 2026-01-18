package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import java.math.BigDecimal;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToShortTransformer extends ObjectTransformer<String, Short>
{
    @Override
    public GenericsPair<String, Short> getPair() {
        return this.genericsPair(String.class, Short.class);
    }
    
    @Override
    public Short transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return new BigDecimal(data).shortValueExact();
    }
}
