package cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import java.util.Map;

public class InMemoryWrappedConfigurer extends WrappedConfigurer
{
    private final Map<String, Object> map;
    
    public InMemoryWrappedConfigurer(@NonNull final Configurer configurer, @NonNull final Map<String, Object> map) {
        super(configurer);
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        if (map == null) {
            throw new NullPointerException("map is marked non-null but is null");
        }
        this.map = map;
    }
    
    @Override
    public List<String> getAllKeys() {
        return (List<String>)Collections.unmodifiableList((List)new ArrayList((Collection)this.map.keySet()));
    }
    
    @Override
    public boolean keyExists(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.map.containsKey((Object)key);
    }
    
    @Override
    public Object getValue(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.map.get((Object)key);
    }
    
    @Override
    public <T> T getValue(@NonNull final String key, @NonNull final Class<T> clazz, final GenericsDeclaration genericType, @NonNull final SerdesContext serdesContext) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        final Object value = this.getValue(key);
        if (value == null) {
            return null;
        }
        return this.resolveType(value, GenericsDeclaration.of(value), clazz, genericType, serdesContext);
    }
    
    @Override
    public void setValue(@NonNull final String key, final Object value, final GenericsDeclaration type, final FieldDeclaration field) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.map.put((Object)key, value);
    }
    
    @Override
    public void setValueUnsafe(@NonNull final String key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.map.put((Object)key, value);
    }
}
