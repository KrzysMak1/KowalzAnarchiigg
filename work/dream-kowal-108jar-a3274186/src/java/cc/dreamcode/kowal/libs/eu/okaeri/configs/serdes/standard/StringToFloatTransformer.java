package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToFloatTransformer extends ObjectTransformer<String, Float>
{
    @Override
    public GenericsPair<String, Float> getPair() {
        return this.genericsPair(String.class, Float.class);
    }
    
    @Override
    public Float transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return Float.parseFloat(data);
    }
}
