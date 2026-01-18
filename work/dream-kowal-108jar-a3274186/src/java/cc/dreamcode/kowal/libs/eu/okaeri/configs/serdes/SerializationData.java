package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes;

import java.util.LinkedHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Collections;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer.Configurer;

public class SerializationData
{
    @NonNull
    private final Configurer configurer;
    @NonNull
    private final SerdesContext context;
    private Map<String, Object> data;
    
    public void clear() {
        this.data.clear();
    }
    
    public Map<String, Object> asMap() {
        return (Map<String, Object>)Collections.unmodifiableMap((Map)this.data);
    }
    
    public void setValueRaw(final Object value) {
        this.clear();
        this.addRaw("$$__value__$$", value);
    }
    
    public void setValue(final Object value) {
        this.clear();
        this.add("$$__value__$$", value);
    }
    
    public void setValue(final Object value, @NonNull final GenericsDeclaration genericType) {
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        this.add("$$__value__$$", value, genericType);
    }
    
    public <T> void setValue(final Object value, @NonNull final Class<T> valueType) {
        if (valueType == null) {
            throw new NullPointerException("valueType is marked non-null but is null");
        }
        this.add("$$__value__$$", value, valueType);
    }
    
    public void setValueCollection(final Collection<?> collection, @NonNull final GenericsDeclaration genericType) {
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        this.addCollection("$$__value__$$", collection, genericType);
    }
    
    public <T> void setValueCollection(final Collection<?> collection, @NonNull final Class<T> collectionValueType) {
        if (collectionValueType == null) {
            throw new NullPointerException("collectionValueType is marked non-null but is null");
        }
        this.addCollection("$$__value__$$", collection, collectionValueType);
    }
    
    public <T> void setValueArray(final T[] array, @NonNull final Class<T> arrayValueType) {
        if (arrayValueType == null) {
            throw new NullPointerException("arrayValueType is marked non-null but is null");
        }
        this.addArray("$$__value__$$", array, arrayValueType);
    }
    
    public void addRaw(@NonNull final String key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        this.data.put(key, value);
    }
    
    public void add(@NonNull final String key, Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        value = this.configurer.simplify(value, null, SerdesContext.of(this.configurer), true);
        this.addRaw(key, value);
    }
    
    public void add(@NonNull final String key, Object value, @NonNull final GenericsDeclaration genericType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        value = this.configurer.simplify(value, genericType, SerdesContext.of(this.configurer), true);
        this.addRaw(key, value);
    }
    
    public <T> void add(@NonNull final String key, final Object value, @NonNull final Class<T> valueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (valueType == null) {
            throw new NullPointerException("valueType is marked non-null but is null");
        }
        final GenericsDeclaration genericType = GenericsDeclaration.of(valueType);
        this.add(key, value, genericType);
    }
    
    public void addCollection(@NonNull final String key, final Collection<?> collection, @NonNull final GenericsDeclaration genericType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        final Object object = this.configurer.simplifyCollection(collection, genericType, SerdesContext.of(this.configurer), true);
        this.addRaw(key, object);
    }
    
    public <T> void addCollection(@NonNull final String key, final Collection<?> collection, @NonNull final Class<T> collectionValueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (collectionValueType == null) {
            throw new NullPointerException("collectionValueType is marked non-null but is null");
        }
        final GenericsDeclaration genericType = GenericsDeclaration.of(collection, (List<Object>)Collections.singletonList((Object)collectionValueType));
        this.addCollection(key, collection, genericType);
    }
    
    public <T> void addArray(@NonNull final String key, final T[] array, @NonNull final Class<T> arrayValueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (arrayValueType == null) {
            throw new NullPointerException("arrayValueType is marked non-null but is null");
        }
        this.addCollection(key, (Collection<?>)((array == null) ? null : Arrays.asList((Object[])array)), arrayValueType);
    }
    
    public void addAsMap(@NonNull final String key, final Map<?, ?> map, @NonNull final GenericsDeclaration genericType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (genericType == null) {
            throw new NullPointerException("genericType is marked non-null but is null");
        }
        final Object object = this.configurer.simplifyMap((Map<Object, Object>)map, genericType, SerdesContext.of(this.configurer), true);
        this.addRaw(key, object);
    }
    
    public <K, V> void addAsMap(@NonNull final String key, final Map<K, V> map, @NonNull final Class<K> mapKeyType, @NonNull final Class<V> mapValueType) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (mapKeyType == null) {
            throw new NullPointerException("mapKeyType is marked non-null but is null");
        }
        if (mapValueType == null) {
            throw new NullPointerException("mapValueType is marked non-null but is null");
        }
        final GenericsDeclaration genericType = GenericsDeclaration.of(map, (List<Object>)Arrays.asList(new Object[] { mapKeyType, mapValueType }));
        this.addAsMap(key, map, genericType);
    }
    
    public void addFormatted(@NonNull final String key, @NonNull final String format, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        if (format == null) {
            throw new NullPointerException("format is marked non-null but is null");
        }
        if (value == null) {
            this.addRaw(key, null);
            return;
        }
        this.add(key, String.format(format, new Object[] { value }));
    }
    
    public SerializationData(@NonNull final Configurer configurer, @NonNull final SerdesContext context) {
        this.data = (Map<String, Object>)new LinkedHashMap();
        if (configurer == null) {
            throw new NullPointerException("configurer is marked non-null but is null");
        }
        if (context == null) {
            throw new NullPointerException("context is marked non-null but is null");
        }
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
