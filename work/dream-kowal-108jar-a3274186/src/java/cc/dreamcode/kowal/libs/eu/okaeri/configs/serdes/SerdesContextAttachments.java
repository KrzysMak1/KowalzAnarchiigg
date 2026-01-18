package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import java.util.function.BiFunction;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;

public class SerdesContextAttachments extends LinkedHashMap<Class<? extends SerdesContextAttachment>, SerdesContextAttachment>
{
    public SerdesContextAttachment put(final Class<? extends SerdesContextAttachment> key, final SerdesContextAttachment value) {
        if (this.containsKey((Object)key)) {
            throw new IllegalArgumentException("cannot override SerdesContext attachment of type " + (Object)key);
        }
        return (SerdesContextAttachment)super.put((Object)key, (Object)value);
    }
    
    public SerdesContextAttachment putIfAbsent(final Class<? extends SerdesContextAttachment> key, final SerdesContextAttachment value) {
        if (this.containsKey((Object)key)) {
            throw new IllegalArgumentException("cannot override SerdesContext attachment of type " + (Object)key);
        }
        return (SerdesContextAttachment)super.putIfAbsent((Object)key, (Object)value);
    }
    
    public void putAll(final Map<? extends Class<? extends SerdesContextAttachment>, ? extends SerdesContextAttachment> map) {
        for (final Class<? extends SerdesContextAttachment> key : map.keySet()) {
            if (this.containsKey((Object)key)) {
                throw new IllegalArgumentException("cannot override SerdesContext attachment of type " + (Object)key);
            }
        }
        super.putAll((Map)map);
    }
    
    public SerdesContextAttachment computeIfPresent(final Class<? extends SerdesContextAttachment> key, final BiFunction<? super Class<? extends SerdesContextAttachment>, ? super SerdesContextAttachment, ? extends SerdesContextAttachment> remappingFunction) {
        throw new RuntimeException("???");
    }
    
    public SerdesContextAttachments clone() {
        return (SerdesContextAttachments)super.clone();
    }
}
