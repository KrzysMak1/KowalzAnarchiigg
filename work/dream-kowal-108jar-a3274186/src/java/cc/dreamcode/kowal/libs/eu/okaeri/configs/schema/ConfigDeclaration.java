package cc.dreamcode.kowal.libs.eu.okaeri.configs.schema;

import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;
import java.util.LinkedHashMap;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Header;
import java.util.Collection;
import java.util.Arrays;
import java.util.ArrayList;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Headers;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Include;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.Names;
import java.util.Map;

public class ConfigDeclaration
{
    private static final Map<Class<?>, ConfigDeclaration> DECLARATION_CACHE;
    private Names nameStrategy;
    private String[] header;
    private Map<String, FieldDeclaration> fieldMap;
    private boolean real;
    private Class<?> type;
    
    public static ConfigDeclaration of(@NonNull final Class<?> clazz, final OkaeriConfig config) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        return of(clazz, (Object)config);
    }
    
    public static ConfigDeclaration of(@NonNull final Class<?> clazz, final Object object) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        final ConfigDeclaration template = (ConfigDeclaration)ConfigDeclaration.DECLARATION_CACHE.computeIfAbsent((Object)clazz, klass -> {
            final ConfigDeclaration declaration = new ConfigDeclaration();
            declaration.setNameStrategy(readNames(klass));
            declaration.setHeader(readHeader(klass));
            declaration.setReal(OkaeriConfig.class.isAssignableFrom(klass));
            declaration.setType(klass);
            return declaration;
        });
        final ConfigDeclaration declaration = new ConfigDeclaration();
        declaration.setNameStrategy(template.getNameStrategy());
        declaration.setHeader(template.getHeader());
        declaration.setReal(template.isReal());
        declaration.setType(template.getType());
        declaration.setFieldMap((Map<String, FieldDeclaration>)readFields(clazz, declaration, object));
        final Include[] array;
        final Include[] subs = array = (Include[])clazz.getDeclaredAnnotationsByType((Class)Include.class);
        for (final Include sub : array) {
            final Map<String, FieldDeclaration> subFields = (Map<String, FieldDeclaration>)readFields(sub.value(), declaration, object);
            subFields.forEach((key, value) -> {
                if (declaration.getFieldMap().containsKey((Object)key)) {
                    return;
                }
                declaration.getFieldMap().put((Object)key, (Object)value);
            });
        }
        return declaration;
    }
    
    public static ConfigDeclaration of(@NonNull final OkaeriConfig config) {
        if (config == null) {
            throw new NullPointerException("config is marked non-null but is null");
        }
        return of(config.getClass(), config);
    }
    
    public static ConfigDeclaration of(@NonNull final Object object) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        return of(object.getClass(), object);
    }
    
    public static ConfigDeclaration of(@NonNull final Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        return of(clazz, null);
    }
    
    private static String[] readHeader(@NonNull final Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        final Headers headers = clazz.getAnnotation(Headers.class);
        if (headers != null) {
            final List<String> headerList = (List<String>)new ArrayList();
            for (final Header header : headers.value()) {
                headerList.addAll((Collection)Arrays.asList((Object[])header.value()));
            }
            return (String[])headerList.toArray((Object[])new String[0]);
        }
        final Header header2 = clazz.getAnnotation(Header.class);
        if (header2 != null) {
            return header2.value();
        }
        return null;
    }
    
    private static Names readNames(@NonNull Class<?> clazz) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        Names names;
        for (names = clazz.getAnnotation(Names.class); names == null; names = clazz.getAnnotation(Names.class)) {
            clazz = clazz.getEnclosingClass();
            if (clazz == null) {
                return null;
            }
        }
        return names;
    }
    
    private static LinkedHashMap<String, FieldDeclaration> readFields(@NonNull final Class<?> clazz, @NonNull final ConfigDeclaration declaration, final Object object) {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        return (LinkedHashMap<String, FieldDeclaration>)Arrays.stream((Object[])clazz.getDeclaredFields()).filter(field -> !field.getName().startsWith("this$")).map(field -> FieldDeclaration.of(declaration, field, object)).filter(Objects::nonNull).collect(Collectors.toMap(FieldDeclaration::getName, field -> field, (u, v) -> {
            throw new IllegalStateException("Duplicate key/field (by name)!\nLeft: " + (Object)u + "\nRight: " + (Object)v);
        }, LinkedHashMap::new));
    }
    
    public Optional<FieldDeclaration> getField(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return (Optional<FieldDeclaration>)Optional.ofNullable((Object)this.fieldMap.get((Object)key));
    }
    
    public GenericsDeclaration getGenericsOrNull(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return (GenericsDeclaration)this.getField(key).map(FieldDeclaration::getType).orElse((Object)null);
    }
    
    public Collection<FieldDeclaration> getFields() {
        return (Collection<FieldDeclaration>)this.fieldMap.values();
    }
    
    public Names getNameStrategy() {
        return this.nameStrategy;
    }
    
    public String[] getHeader() {
        return this.header;
    }
    
    public Map<String, FieldDeclaration> getFieldMap() {
        return this.fieldMap;
    }
    
    public boolean isReal() {
        return this.real;
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    public void setNameStrategy(final Names nameStrategy) {
        this.nameStrategy = nameStrategy;
    }
    
    public void setHeader(final String[] header) {
        this.header = header;
    }
    
    public void setFieldMap(final Map<String, FieldDeclaration> fieldMap) {
        this.fieldMap = fieldMap;
    }
    
    public void setReal(final boolean real) {
        this.real = real;
    }
    
    public void setType(final Class<?> type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConfigDeclaration)) {
            return false;
        }
        final ConfigDeclaration other = (ConfigDeclaration)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isReal() != other.isReal()) {
            return false;
        }
        final Object this$nameStrategy = this.getNameStrategy();
        final Object other$nameStrategy = other.getNameStrategy();
        Label_0078: {
            if (this$nameStrategy == null) {
                if (other$nameStrategy == null) {
                    break Label_0078;
                }
            }
            else if (this$nameStrategy.equals(other$nameStrategy)) {
                break Label_0078;
            }
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getHeader(), (Object[])other.getHeader())) {
            return false;
        }
        final Object this$fieldMap = this.getFieldMap();
        final Object other$fieldMap = other.getFieldMap();
        Label_0131: {
            if (this$fieldMap == null) {
                if (other$fieldMap == null) {
                    break Label_0131;
                }
            }
            else if (this$fieldMap.equals(other$fieldMap)) {
                break Label_0131;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null) {
            if (other$type == null) {
                return true;
            }
        }
        else if (this$type.equals(other$type)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigDeclaration;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isReal() ? 79 : 97);
        final Object $nameStrategy = this.getNameStrategy();
        result = result * 59 + (($nameStrategy == null) ? 43 : $nameStrategy.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getHeader());
        final Object $fieldMap = this.getFieldMap();
        result = result * 59 + (($fieldMap == null) ? 43 : $fieldMap.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ConfigDeclaration(nameStrategy=" + (Object)this.getNameStrategy() + ", header=" + Arrays.deepToString((Object[])this.getHeader()) + ", fieldMap=" + (Object)this.getFieldMap() + ", real=" + this.isReal() + ", type=" + (Object)this.getType() + ")";
    }
    
    static {
        DECLARATION_CACHE = (Map)new ConcurrentHashMap();
    }
}
