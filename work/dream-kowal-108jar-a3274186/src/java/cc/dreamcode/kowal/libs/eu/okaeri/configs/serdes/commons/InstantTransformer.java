package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.time.Instant;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class InstantTransformer extends BidirectionalTransformer<String, Instant>
{
    @Override
    public GenericsPair<String, Instant> getPair() {
        return this.genericsPair(String.class, Instant.class);
    }
    
    @Override
    public Instant leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return Instant.parse((CharSequence)data);
    }
    
    @Override
    public String rightToLeft(@NonNull final Instant data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return data.toString();
    }
}
