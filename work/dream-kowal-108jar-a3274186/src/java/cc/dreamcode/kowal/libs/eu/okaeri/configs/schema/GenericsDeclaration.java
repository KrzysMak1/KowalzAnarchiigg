package cc.dreamcode.kowal.libs.eu.okaeri.configs.schema;

import java.util.HashSet;
import java.util.HashMap;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;
import java.util.Objects;
import java.util.Arrays;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Collectors;
import lombok.NonNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;

public class GenericsDeclaration
{
    private static final Map<String, Class<?>> PRIMITIVES;
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER;
    private static final Set<Class<?>> PRIMITIVE_WRAPPERS;
    private Class<?> type;
    private List<GenericsDeclaration> subtype;
    private boolean isEnum;
    
    private GenericsDeclaration(final Class<?> type) {
        this.subtype = new ArrayList<>();
        this.type = type;
        this.isEnum = type.isEnum();
    }
    
    public static boolean isUnboxedCompatibleWithBoxed(@NonNull final Class<?> unboxedClazz, @NonNull final Class<?> boxedClazz) {
        if (unboxedClazz == null) {
            throw new NullPointerException("unboxedClazz is marked non-null but is null");
        }
        if (boxedClazz == null) {
            throw new NullPointerException("boxedClazz is marked non-null but is null");
        }
        final Class<?> primitiveWrapper = (Class<?>)GenericsDeclaration.PRIMITIVE_TO_WRAPPER.get((Object)unboxedClazz);
        return primitiveWrapper == boxedClazz;
    }
    
    public static boolean doBoxTypesMatch(@NonNull final Class<?> clazz1, @NonNull final Class<?> clazz2) {
        if (clazz1 == null) {
            throw new NullPointerException("clazz1 is marked non-null but is null");
        }
        if (clazz2 == null) {
            throw new NullPointerException("clazz2 is marked non-null but is null");
        }
        return isUnboxedCompatibleWithBoxed(clazz1, clazz2) || isUnboxedCompatibleWithBoxed(clazz2, clazz1);
    }
    
    public static GenericsDeclaration of(@NonNull final Object type, @NonNull final List<Object> subtypes) {
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        if (subtypes == null) {
            throw new NullPointerException("subtypes is marked non-null but is null");
        }
        final Class<?> finalType = (type instanceof Class) ? ((Class)type) : type.getClass();
        final GenericsDeclaration declaration = new GenericsDeclaration(finalType);
        declaration.setSubtype(subtypes.stream().map(GenericsDeclaration::of).collect(Collectors.toList()));
        return declaration;
    }
    
    public static GenericsDeclaration of(final Object object) {
        if (object == null) {
            return null;
        }
        if (object instanceof GenericsDeclaration) {
            return (GenericsDeclaration)object;
        }
        if (object instanceof Class) {
            return new GenericsDeclaration((Class<?>)object);
        }
        if (object instanceof Type) {
            return from((Type)object);
        }
        return new GenericsDeclaration(object.getClass());
    }
    
    private static GenericsDeclaration from(final Type type) {
        if (type instanceof ParameterizedType) {
            final ParameterizedType parameterizedType = (ParameterizedType)type;
            final Type rawType = parameterizedType.getRawType();
            final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            if (rawType instanceof Class) {
                final GenericsDeclaration declaration = new GenericsDeclaration((Class<?>)rawType);
                declaration.setSubtype(Arrays.stream((Object[])actualTypeArguments).map(GenericsDeclaration::of).filter(Objects::nonNull).collect(Collectors.toList()));
                return declaration;
            }
        }
        throw new IllegalArgumentException("cannot process type: " + (Object)type + " [" + (Object)type.getClass() + "]");
    }
    
    public GenericsDeclaration getSubtypeAtOrNull(final int index) {
        return (this.subtype == null) ? null : ((index >= this.subtype.size()) ? null : ((GenericsDeclaration)this.subtype.get(index)));
    }
    
    public GenericsDeclaration getSubtypeAtOrThrow(final int index) {
        final GenericsDeclaration subtype = this.getSubtypeAtOrNull(index);
        if (subtype == null) {
            throw new IllegalArgumentException("Cannot resolve subtype with index " + index + " for " + (Object)this);
        }
        return subtype;
    }
    
    public Class<?> wrap() {
        return (Class)GenericsDeclaration.PRIMITIVE_TO_WRAPPER.get((Object)this.type);
    }
    
    public Object unwrapValue(final Object object) {
        if (object instanceof Boolean) {
            return object;
        }
        if (object instanceof Byte) {
            return object;
        }
        if (object instanceof Character) {
            return object;
        }
        if (object instanceof Double) {
            return object;
        }
        if (object instanceof Float) {
            return object;
        }
        if (object instanceof Integer) {
            return object;
        }
        if (object instanceof Long) {
            return object;
        }
        if (object instanceof Short) {
            return object;
        }
        return object;
    }
    
    public boolean isPrimitive() {
        return this.type.isPrimitive();
    }
    
    public boolean isPrimitiveWrapper() {
        return GenericsDeclaration.PRIMITIVE_WRAPPERS.contains((Object)this.type);
    }
    
    public boolean isConfig() {
        return OkaeriConfig.class.isAssignableFrom(this.type);
    }
    
    public boolean hasSubtypes() {
        return this.subtype != null && !this.subtype.isEmpty();
    }
    
    public Class<?> getType() {
        return this.type;
    }
    
    public List<GenericsDeclaration> getSubtype() {
        return this.subtype;
    }
    
    public boolean isEnum() {
        return this.isEnum;
    }
    
    public void setType(final Class<?> type) {
        this.type = type;
    }
    
    public void setSubtype(final List<GenericsDeclaration> subtype) {
        this.subtype = subtype;
    }
    
    public void setEnum(final boolean isEnum) {
        this.isEnum = isEnum;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GenericsDeclaration)) {
            return false;
        }
        final GenericsDeclaration other = (GenericsDeclaration)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isEnum() != other.isEnum()) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0078: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0078;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$subtype = this.getSubtype();
        final Object other$subtype = other.getSubtype();
        if (this$subtype == null) {
            if (other$subtype == null) {
                return true;
            }
        }
        else if (this$subtype.equals(other$subtype)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GenericsDeclaration;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isEnum() ? 79 : 97);
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $subtype = this.getSubtype();
        result = result * 59 + (($subtype == null) ? 43 : $subtype.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "GenericsDeclaration(type=" + (Object)this.getType() + ", subtype=" + (Object)this.getSubtype() + ", isEnum=" + this.isEnum() + ")";
    }
    
    public GenericsDeclaration(final Class<?> type, final List<GenericsDeclaration> subtype, final boolean isEnum) {
        this.subtype = new ArrayList<>();
        this.type = type;
        this.subtype = subtype;
        this.isEnum = isEnum;
    }
    
    static {
        PRIMITIVES = new HashMap<>();
        PRIMITIVE_TO_WRAPPER = new HashMap<>();
        PRIMITIVE_WRAPPERS = new HashSet<>();
        GenericsDeclaration.PRIMITIVES.put(Boolean.TYPE.getName(), Boolean.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Byte.TYPE.getName(), Byte.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Character.TYPE.getName(), Character.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Double.TYPE.getName(), Double.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Float.TYPE.getName(), Float.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Integer.TYPE.getName(), Integer.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Long.TYPE.getName(), Long.TYPE);
        GenericsDeclaration.PRIMITIVES.put(Short.TYPE.getName(), Short.TYPE);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Boolean.TYPE, Boolean.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Byte.TYPE, Byte.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Character.TYPE, Character.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Double.TYPE, Double.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Float.TYPE, Float.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Integer.TYPE, Integer.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Long.TYPE, Long.class);
        GenericsDeclaration.PRIMITIVE_TO_WRAPPER.put(Short.TYPE, Short.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Boolean.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Byte.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Character.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Double.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Float.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Integer.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Long.class);
        GenericsDeclaration.PRIMITIVE_WRAPPERS.add(Short.class);
    }
}
