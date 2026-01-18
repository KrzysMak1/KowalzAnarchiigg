package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToDoubleTransformer extends ObjectTransformer<String, Double>
{
    @Override
    public GenericsPair<String, Double> getPair() {
        return this.genericsPair(String.class, Double.class);
    }
    
    @Override
    public Double transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return Double.parseDouble(data);
    }
}
