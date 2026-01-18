package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.util.regex.Pattern;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class PatternTransformer extends BidirectionalTransformer<String, Pattern>
{
    @Override
    public GenericsPair<String, Pattern> getPair() {
        return this.genericsPair(String.class, Pattern.class);
    }
    
    @Override
    public Pattern leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return Pattern.compile(data);
    }
    
    @Override
    public String rightToLeft(@NonNull final Pattern data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return data.pattern();
    }
}
