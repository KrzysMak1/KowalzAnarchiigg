package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.transformer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsPair;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Tag;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.BidirectionalTransformer;

public class StringTagTransformer extends BidirectionalTransformer<String, Tag>
{
    private final Map<String, Tag> tagMap;
    
    public StringTagTransformer() {
        this.tagMap = (Map<String, Tag>)new HashMap();
        try {
            for (final Field field : Tag.class.getFields()) {
                if (Tag.class.isAssignableFrom(field.getType())) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        final Tag tag = (Tag)field.get((Object)null);
                        if (tag != null) {
                            this.tagMap.put((Object)tag.getKey().toString(), (Object)tag);
                        }
                    }
                }
            }
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    @Override
    public GenericsPair<String, Tag> getPair() {
        return this.genericsPair(String.class, Tag.class);
    }
    
    @Override
    public Tag leftToRight(@NonNull final String data, @NonNull final SerdesContext context) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (context == null) {
            throw new NullPointerException("context is marked non-null but is null");
        }
        final Tag tag = (Tag)this.tagMap.get((Object)data);
        if (tag == null) {
            throw new IllegalArgumentException("Unknown tag: " + data);
        }
        return tag;
    }
    
    @Override
    public String rightToLeft(final Tag data, @NonNull final SerdesContext context) {
        if (context == null) {
            throw new NullPointerException("context is marked non-null but is null");
        }
        return data.getKey().toString();
    }
}
