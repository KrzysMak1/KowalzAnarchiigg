package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;

public class StringToCharacterTransformer extends ObjectTransformer<String, Character>
{
    @Override
    public GenericsPair<String, Character> getPair() {
        return this.genericsPair(String.class, Character.class);
    }
    
    @Override
    public Character transform(@NonNull final String data, @NonNull final SerdesContext serdesContext) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (data.length() > 1) {
            throw new IllegalArgumentException("char '" + data + "' too long: " + data.length() + ">1");
        }
        return data.charAt(0);
    }
}
