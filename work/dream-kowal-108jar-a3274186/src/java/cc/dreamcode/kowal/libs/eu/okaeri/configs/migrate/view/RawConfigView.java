package cc.dreamcode.kowal.libs.eu.okaeri.configs.migrate.view;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;

public class RawConfigView
{
    private final OkaeriConfig config;
    private String nestedSeparator;
    
    public boolean exists(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        final Map<String, Object> document = this.config.asMap(this.config.getConfigurer(), true);
        return this.valueExists(document, key);
    }
    
    public Object get(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        final Map<String, Object> document = this.config.asMap(this.config.getConfigurer(), true);
        return this.valueExtract(document, key);
    }
    
    public Object set(@NonNull final String key, final Object value) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        final Map<String, Object> document = this.config.asMap(this.config.getConfigurer(), true);
        final Object old = this.valuePut(document, key, value);
        this.config.load(document);
        return old;
    }
    
    public Object remove(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        final Map<String, Object> document = this.config.asMap(this.config.getConfigurer(), true);
        final Object old = this.valueRemove(document, key);
        if (key.split(this.nestedSeparator).length == 1) {
            this.config.getConfigurer().remove(key);
        }
        this.config.load(document);
        return old;
    }
    
    protected boolean valueExists(Map document, final String path) {
        final String[] split = path.split(this.nestedSeparator);
        for (int i = 0; i < split.length; ++i) {
            final String part = split[i];
            if (i == split.length - 1) {
                return document.containsKey((Object)part);
            }
            final Object element = document.get((Object)part);
            if (!(element instanceof Map)) {
                return false;
            }
            document = (Map)element;
        }
        return false;
    }
    
    protected Object valueExtract(Map document, final String path) {
        final String[] split = path.split(this.nestedSeparator);
        for (int i = 0; i < split.length; ++i) {
            final String part = split[i];
            final Object element = document.get((Object)part);
            if (i == split.length - 1) {
                return element;
            }
            if (!(element instanceof Map)) {
                final String elementStr = (element == null) ? "null" : element.getClass().getSimpleName();
                throw new IllegalArgumentException("Cannot extract '" + path + "': not deep enough (ended at index " + i + " [" + part + ":" + elementStr + "])");
            }
            document = (Map)element;
        }
        return null;
    }
    
    protected Object valuePut(Map document, final String path, final Object value) {
        final String[] split = path.split(this.nestedSeparator);
        for (int i = 0; i < split.length; ++i) {
            final String part = split[i];
            if (i == split.length - 1) {
                return document.put((Object)part, value);
            }
            final Object element = document.get((Object)part);
            if (element instanceof Map) {
                document = (Map)element;
            }
            else {
                if (element != null) {
                    final String elementStr = element.getClass().getSimpleName();
                    throw new IllegalArgumentException("Cannot insert '" + path + "': type conflict (ended at index " + i + " [" + part + ":" + elementStr + "])");
                }
                final Map map = (Map)new LinkedHashMap();
                document.put((Object)part, (Object)map);
                document = map;
            }
        }
        throw new IllegalArgumentException("Cannot put '" + path + "'");
    }
    
    protected Object valueRemove(Map document, final String path) {
        final String[] split = path.split(this.nestedSeparator);
        for (int i = 0; i < split.length; ++i) {
            final String part = split[i];
            if (i == split.length - 1) {
                return document.remove((Object)part);
            }
            final Object element = document.get((Object)part);
            if (!(element instanceof Map)) {
                return null;
            }
            document = (Map)element;
        }
        return null;
    }
    
    public RawConfigView(final OkaeriConfig config, final String nestedSeparator) {
        this.nestedSeparator = "\\.";
        this.config = config;
        this.nestedSeparator = nestedSeparator;
    }
    
    public RawConfigView(final OkaeriConfig config) {
        this.nestedSeparator = "\\.";
        this.config = config;
    }
}
