package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.meta;

import java.util.concurrent.ConcurrentHashMap;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.context.PlaceholderContext;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part.MessageFieldAccessor;
import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.annotation.Placeholder;
import java.util.LinkedHashMap;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.placeholders.schema.resolver.PlaceholderResolver;
import java.util.Map;
import java.lang.invoke.MethodHandles;

public class SchemaMeta
{
    private static final MethodHandles.Lookup LOOKUP;
    private static final Map<Class<?>, SchemaMeta> SCHEMA_CACHE;
    private final Class<?> type;
    private final Map<String, PlaceholderResolver> placeholders;
    
    public static SchemaMeta of(@NonNull final Class<?> clazz) {
        try {
            if (clazz == null) {
                throw new NullPointerException("clazz is marked non-null but is null");
            }
            final SchemaMeta cached = (SchemaMeta)SchemaMeta.SCHEMA_CACHE.get((Object)clazz);
            if (cached != null) {
                return cached;
            }
            final Map<String, PlaceholderResolver> placeholders = (Map<String, PlaceholderResolver>)new LinkedHashMap();
            final Field[] fields = clazz.getDeclaredFields();
            final Method[] methods = clazz.getDeclaredMethods();
            final Placeholder clazzAnnotation = clazz.getAnnotation(Placeholder.class);
            MethodHandle handle = null;
            if (clazzAnnotation != null && clazzAnnotation.scan()) {
                for (final Method method : methods) {
                    Label_0268: {
                        if (Modifier.isPublic(method.getModifiers())) {
                            if (!clazzAnnotation.name().isEmpty()) {
                                throw new RuntimeException("@Placeholder for " + (Object)clazz + " has name set, names are not supported here");
                            }
                            String name = method.getName();
                            if (name.startsWith("get")) {
                                name = name.substring(3);
                            }
                            else {
                                if (!name.startsWith("is")) {
                                    break Label_0268;
                                }
                                name = name.substring(2);
                            }
                            final char[] nameArr = name.toCharArray();
                            nameArr[0] = Character.toLowerCase(nameArr[0]);
                            name = new String(nameArr);
                            final Class<?> returnType = method.getReturnType();
                            handle = toHandle(method);
                            placeholders.put((Object)name, (from, params, context) -> handleholder(handle, from));
                        }
                    }
                }
            }
            for (final Field field : fields) {
                final Class<?> fieldType = field.getType();
                final Placeholder placeholder = (Placeholder)field.getAnnotation((Class)Placeholder.class);
                if (placeholder != null) {
                    final String name2 = placeholder.name().isEmpty() ? field.getName() : placeholder.name();
                    handle = toHandle(field);
                    placeholders.put((Object)name2, (from, params, context) -> handleholder(handle, from));
                }
            }
            for (final Method method : methods) {
                final Placeholder placeholder2 = (Placeholder)method.getAnnotation((Class)Placeholder.class);
                if (placeholder2 != null) {
                    final String name3 = placeholder2.name().isEmpty() ? method.getName() : placeholder2.name();
                    final MethodHandle handle2 = toHandle(method);
                    placeholders.put((Object)name3, (from, params, context) -> handleholder(handle, from));
                }
            }
            final SchemaMeta meta = new SchemaMeta(clazz, placeholders);
            SchemaMeta.SCHEMA_CACHE.put((Object)clazz, (Object)meta);
            return meta;
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private static Object handleholder(final MethodHandle handle, final Object from) {
        try {
            return handle.invoke(from);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private static MethodHandle toHandle(final Method method) {
        try {
            method.setAccessible(true);
            return SchemaMeta.LOOKUP.unreflect(method);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    private static MethodHandle toHandle(final Field field) {
        try {
            field.setAccessible(true);
            return SchemaMeta.LOOKUP.unreflectGetter(field);
        }
        catch (final Throwable $ex) {
            throw $ex;
        }
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    public Map<String, PlaceholderResolver> getPlaceholders() {
        return this.placeholders;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SchemaMeta)) {
            return false;
        }
        final SchemaMeta other = (SchemaMeta)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$placeholders = this.getPlaceholders();
        final Object other$placeholders = other.getPlaceholders();
        if (this$placeholders == null) {
            if (other$placeholders == null) {
                return true;
            }
        }
        else if (this$placeholders.equals(other$placeholders)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SchemaMeta;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $placeholders = this.getPlaceholders();
        result = result * 59 + (($placeholders == null) ? 43 : $placeholders.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SchemaMeta(type=" + (Object)this.getType() + ", placeholders=" + (Object)this.getPlaceholders() + ")";
    }
    
    private SchemaMeta(final Class<?> type, final Map<String, PlaceholderResolver> placeholders) {
        this.type = type;
        this.placeholders = placeholders;
    }
    
    static {
        LOOKUP = MethodHandles.lookup();
        SCHEMA_CACHE = (Map)new ConcurrentHashMap();
    }
}
