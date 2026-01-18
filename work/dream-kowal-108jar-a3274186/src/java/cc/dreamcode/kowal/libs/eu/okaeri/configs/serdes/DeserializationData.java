package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Collections;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;
import lombok.NonNull;
import java.util.Map;

public class DeserializationData
{
    @NonNull
    private Map<String, Object> data;
    @NonNull
    private Configurer configurer;
    @NonNull
    private SerdesContext context;
    
    public Map<String, Object> asMap() {
        return (Map<String, Object>)Collections.unmodifiableMap((Map)this.data);
    }
    
    public boolean isValue() {
        return this.containsKey("$$__value__$$");
    }
    
    public Object getValueRaw() {
        return this.getRaw("$$__value__$$");
    }
    
    public <T> T getValueDirect(@NonNull final GenericsDeclaration genericType) {
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        return this.getDirect("$$__value__$$", genericType);
    }
    
    public <T> T getValue(@NonNull final Class<T> valueType) {
        if (valueType == null) {
            throw new NullPointerException("valueType is marked non-null but is null");
        }
        return this.get("$$__value__$$", valueType);
    }
    
    public <T> Collection<T> getValueAsCollection(@NonNull final GenericsDeclaration genericType) {
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        return this.getAsCollection("$$__value__$$", genericType);
    }
    
    public <T> List<T> getValueAsList(@NonNull final Class<T> listValueType) {
        if (listValueType == null) {
            throw new NullPointerException("listValueType is marked non-null but is null");
        }
        return this.getAsList("$$__value__$$", listValueType);
    }
    
    public boolean containsKey(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.data.containsKey((Object)key);
    }
    
    public Object getRaw(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (!this.isValue() && "$$__value__$$".equals(key)) {
            return this.asMap();
        }
        return this.data.get(key);
    }
    
    public <T> T getDirect(@NonNull final String key, @NonNull final GenericsDeclaration genericType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        final Object object = this.getRaw(key);
        return (T)this.configurer.resolveType(object, null, genericType.getType(), genericType, SerdesContext.of(this.configurer));
    }
    
    public <T> T get(@NonNull final String key, @NonNull final Class<T> valueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (valueType == null) {
            throw new NullPointerException("valueType is marked non-null but is null");
        }
        final Object object = this.getRaw(key);
        return (T)this.configurer.resolveType(object, null, valueType, null, SerdesContext.of(this.configurer));
    }
    
    public <T> Collection<T> getAsCollection(@NonNull final String key, @NonNull final GenericsDeclaration genericType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        if (!Collection.class.isAssignableFrom(genericType.getType())) {
            throw new IllegalArgumentException("genericType.type must be a superclass of Collection: " + (Object)genericType);
        }
        return (Collection<T>)this.getDirect(key, genericType);
    }
    
    public <T> List<T> getAsList(@NonNull final String key, @NonNull final Class<T> listValueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (listValueType == null) {
            throw new NullPointerException("listValueType is marked non-null but is null");
        }
        final GenericsDeclaration genericType = GenericsDeclaration.of(List.class, (List<Object>)Collections.singletonList((Object)listValueType));
        return (List<T>)this.getAsCollection(key, genericType);
    }
    
    public <K, V> Map<K, V> getAsMap(@NonNull final String key, @NonNull final Class<K> mapKeyType, @NonNull final Class<V> mapValueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (mapKeyType == null) {
            throw new NullPointerException("mapKeyType is marked non-null but is null");
        }
        if (mapValueType == null) {
            throw new NullPointerException("mapValueType is marked non-null but is null");
        }
        final GenericsDeclaration genericType = GenericsDeclaration.of(Map.class, (List<Object>)Arrays.asList(new Object[] { mapKeyType, mapValueType }));
        return (Map<K, V>)this.getDirect(key, genericType);
    }
    
    public <K, V> Map<K, V> getAsMap(@NonNull final String key, @NonNull final GenericsDeclaration genericType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        if (!Map.class.isAssignableFrom(genericType.getType())) {
            throw new IllegalArgumentException("genericType.type must be a superclass of Map: " + (Object)genericType);
        }
        return (Map<K, V>)this.getDirect(key, genericType);
    }
    
    public DeserializationData(@NonNull final Map<String, Object> data, @NonNull final Configurer configurer, @NonNull final SerdesContext context) {
        if (data == null) {
            throw new NullPointerException("data is marked non-null but is null");
        }
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        if (context == null) {
            throw new NullPointerException("context is marked non-null but is null");
        }
        this.data = data;
        this.configurer = configurer;
        this.context = context;
    }
    
    @NonNull
    public Configurer getConfigurer() {
        return this.configurer;
    }
    
    @NonNull
    public SerdesContext getContext() {
        return this.context;
    }
}
