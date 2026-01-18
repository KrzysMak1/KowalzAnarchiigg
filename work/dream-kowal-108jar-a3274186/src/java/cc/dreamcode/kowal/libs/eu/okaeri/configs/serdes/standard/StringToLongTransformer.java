package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import java.math.BigDecimal;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToLongTransformer extends ObjectTransformer<String, Long>
{
    @Override
    public GenericsPair<String, Long> getPair() {
        return this.genericsPair(String.class, Long.class);
    }
    
    @Override
    public Long transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return new BigDecimal(data).longValueExact();
    }
}
