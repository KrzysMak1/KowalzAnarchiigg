package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.math.BigInteger;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToBigIntegerTransformer extends ObjectTransformer<String, BigInteger>
{
    @Override
    public GenericsPair<String, BigInteger> getPair() {
        return this.genericsPair(String.class, BigInteger.class);
    }
    
    @Override
    public BigInteger transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return new BigInteger(data);
    }
}
