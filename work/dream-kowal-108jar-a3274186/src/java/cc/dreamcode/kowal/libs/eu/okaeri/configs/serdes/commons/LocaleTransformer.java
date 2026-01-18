package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.util.Locale;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class LocaleTransformer extends BidirectionalTransformer<String, Locale>
{
    @Override
    public GenericsPair<String, Locale> getPair() {
        return this.genericsPair(String.class, Locale.class);
    }
    
    @Override
    public Locale leftToRight(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return Locale.forLanguageTag(this.normalize(data));
    }
    
    @Override
    public String rightToLeft(@NonNull final Locale data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        return this.normalize(data.toString());
    }
    
    private String normalize(final String localeTag) {
        return localeTag.replace((CharSequence)"_", (CharSequence)"-");
    }
}
