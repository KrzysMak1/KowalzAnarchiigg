package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.math.BigDecimal;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToBigDecimalTransformer extends ObjectTransformer<String, BigDecimal>
{
    @Override
    public GenericsPair<String, BigDecimal> getPair() {
        return this.genericsPair(String.class, BigDecimal.class);
    }
    
    @Override
    public BigDecimal transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return new BigDecimal(data);
    }
}
