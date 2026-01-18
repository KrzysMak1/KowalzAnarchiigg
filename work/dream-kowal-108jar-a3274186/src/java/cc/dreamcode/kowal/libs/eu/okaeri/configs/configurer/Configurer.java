package cc.dreamcode.kowal.libs.eu.okaeri.configs.configurer;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Optional;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.annotation.TargetType;
import java.lang.reflect.Method;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.util.UnsafeUtil;
import java.util.stream.Collectors;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.ConfigManager;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.DeserializationData;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectSerializer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerializationData;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.ConfigDeclaration;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.exception.OkaeriException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.ObjectTransformer;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContext;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.FieldDeclaration;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.schema.GenericsDeclaration;
import java.util.Collections;
import java.util.List;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.OkaeriSerdesPack;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.standard.StandardSerdes;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesRegistry;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.OkaeriConfig;

public abstract class Configurer
{
    private OkaeriConfig parent;
    @NonNull
    private SerdesRegistry registry;
    
    public Configurer() {
        (this.registry = new SerdesRegistry()).register(new StandardSerdes());
    }
    
    public void register(@NonNull final OkaeriSerdesPack pack) {
        if (pack == null) {
            throw new NullPointerException("pack is marked non-null but is null");
        }
        this.registry.register(pack);
    }
    
    public List<String> getExtensions() {
        return (List<String>)Collections.emptyList();
    }
    
    public abstract void setValue(@NonNull final String key, final Object value, final GenericsDeclaration genericType, final FieldDeclaration field);
    
    public abstract void setValueUnsafe(@NonNull final String key, final Object value);
    
    public abstract Object getValue(@NonNull final String key);
    
    public Object getValueUnsafe(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.getValue(key);
    }
    
    public abstract Object remove(@NonNull final String key);
    
    public boolean isToStringObject(@NonNull final Object object, final GenericsDeclaration genericType, final SerdesContext serdesContext) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (!(object instanceof Class)) {
            return object.getClass().isEnum() || this.isToStringObject(object.getClass(), genericType, serdesContext);
        }
        final Class<?> clazzObject = (Class<?>)object;
        if (clazzObject.isEnum()) {
            return true;
        }
        if (genericType == null) {
            return false;
        }
        if (this.registry.canTransform(genericType, GenericsDeclaration.of(String.class))) {
            return true;
        }
        final List<ObjectTransformer> transformersFrom = this.getRegistry().getTransformersFrom(genericType);
        for (final ObjectTransformer stepOneTransformer : transformersFrom) {
            final GenericsDeclaration stepOneTarget = stepOneTransformer.getPair().getTo();
            final ObjectTransformer stepTwoTransformer = this.getRegistry().getTransformer(stepOneTarget, GenericsDeclaration.of(String.class));
            if (stepTwoTransformer != null) {
                return true;
            }
        }
        return false;
    }
    
    @Deprecated
    public boolean isToStringObject(@NonNull final Object object, final GenericsDeclaration genericType) {
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        return this.isToStringObject(object, genericType, SerdesContext.of(this));
    }
    
    public Object simplifyCollection(@NonNull final Collection<?> value, final GenericsDeclaration genericType, @NonNull final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        final List collection = (List)new ArrayList();
        final GenericsDeclaration collectionSubtype = (genericType == null) ? null : genericType.getSubtypeAtOrNull(0);
        for (final Object collectionElement : value) {
            collection.add(this.simplify(collectionElement, collectionSubtype, serdesContext, conservative));
        }
        return collection;
    }
    
    public Object simplifyMap(@NonNull final Map<Object, Object> value, final GenericsDeclaration genericType, @NonNull final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        final Map<Object, Object> map = (Map<Object, Object>)new LinkedHashMap();
        final GenericsDeclaration keyDeclaration = (genericType == null) ? null : genericType.getSubtypeAtOrNull(0);
        final GenericsDeclaration valueDeclaration = (genericType == null) ? null : genericType.getSubtypeAtOrNull(1);
        for (final Map.Entry<Object, Object> entry : value.entrySet()) {
            final Object key = this.simplify(entry.getKey(), keyDeclaration, serdesContext, conservative);
            final Object kValue = this.simplify(entry.getValue(), valueDeclaration, serdesContext, conservative);
            map.put(key, kValue);
        }
        return map;
    }
    
    public Object simplify(final Object value, GenericsDeclaration genericType, @NonNull final SerdesContext serdesContext, final boolean conservative) throws OkaeriException {
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (value == null) {
            return null;
        }
        if (genericType != null && genericType.getType() == Object.class && !genericType.hasSubtypes()) {
            genericType = GenericsDeclaration.of(value);
        }
        final Class<?> serializerType = (genericType != null) ? genericType.getType() : value.getClass();
        final ObjectSerializer serializer = this.registry.getSerializer(serializerType);
        if (serializer == null) {
            if (OkaeriConfig.class.isAssignableFrom(value.getClass())) {
                final OkaeriConfig config = (OkaeriConfig)value;
                return config.asMap(this, conservative);
            }
            if (conservative && (serializerType.isPrimitive() || GenericsDeclaration.of(serializerType).isPrimitiveWrapper())) {
                return value;
            }
            if (serializerType.isPrimitive()) {
                final Class<?> wrappedPrimitive = GenericsDeclaration.of(serializerType).wrap();
                return this.simplify(wrappedPrimitive.cast(value), GenericsDeclaration.of(wrappedPrimitive), serdesContext, conservative);
            }
            if (genericType == null) {
                final GenericsDeclaration valueDeclaration = GenericsDeclaration.of(value);
                if (this.isToStringObject(serializerType, valueDeclaration, serdesContext)) {
                    return this.resolveType(value, genericType, String.class, null, serdesContext);
                }
            }
            if (this.isToStringObject(serializerType, genericType, serdesContext)) {
                return this.resolveType(value, genericType, String.class, null, serdesContext);
            }
            if (value instanceof Collection) {
                return this.simplifyCollection((Collection<?>)value, genericType, serdesContext, conservative);
            }
            if (value instanceof Map) {
                return this.simplifyMap((Map<Object, Object>)value, genericType, serdesContext, conservative);
            }
            if (value instanceof Serializable) {
                final ConfigDeclaration declaration = ConfigDeclaration.of(value);
                final SerializationData data = new SerializationData(this, serdesContext);
                declaration.getFields().forEach(field -> data.add(field.getName(), field.getValue(), field.getType()));
                final LinkedHashMap<Object, Object> serializationMap = (LinkedHashMap<Object, Object>)new LinkedHashMap((Map)data.asMap());
                return this.simplifyMap((Map<Object, Object>)serializationMap, GenericsDeclaration.of(Map.class, (List<Object>)Collections.singletonList((Object)String.class)), serdesContext, conservative);
            }
            throw new OkaeriException("cannot simplify type " + (Object)serializerType + " (" + (Object)genericType + "): '" + value + "' [" + (Object)value.getClass() + "]");
        }
        else {
            final Configurer configurer = (this.getParent() == null) ? this : this.getParent().getConfigurer();
            final SerializationData serializationData = new SerializationData(configurer, serdesContext);
            serializer.serialize(value, serializationData, (genericType == null) ? GenericsDeclaration.of(value) : genericType);
            final Map<Object, Object> serializationMap2 = (Map<Object, Object>)new LinkedHashMap((Map)serializationData.asMap());
            if (!serializationMap2.containsKey((Object)"$$__value__$$")) {
                return this.simplifyMap(serializationMap2, GenericsDeclaration.of(Map.class, (List<Object>)Collections.singletonList((Object)String.class)), serdesContext, conservative);
            }
            if (serializationMap2.size() == 1) {
                return serializationMap2.get((Object)"$$__value__$$");
            }
            throw new OkaeriException("magic value key is not allowed with other keys (" + (Object)serializationMap2.keySet() + ") in the SerializationData for " + (Object)serializerType + " (" + (Object)genericType + "): '" + value + "' [" + (Object)value.getClass() + "]");
        }
    }
    
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
    
    public <T> T resolveType(final Object object, final GenericsDeclaration genericSource, @NonNull final Class<T> targetClazz, final GenericsDeclaration genericTarget, @NonNull final SerdesContext serdesContext) throws OkaeriException {
        if (targetClazz == null) {
            throw new NullPointerException("targetClazz is marked non-null but is null");
        }
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (object == null) {
            return null;
        }
        final GenericsDeclaration source = (genericSource == null) ? GenericsDeclaration.of(object) : genericSource;
        GenericsDeclaration target = (genericTarget == null) ? GenericsDeclaration.of(targetClazz) : genericTarget;
        if (target.isPrimitive()) {
            target = GenericsDeclaration.of(target.wrap());
        }
        final ObjectSerializer objectSerializer = this.registry.getSerializer(targetClazz);
        if (objectSerializer != null) {
            final Configurer configurer = (this.getParent() == null) ? this : this.getParent().getConfigurer();
            final DeserializationData deserializationData = (object instanceof Map) ? new DeserializationData((Map<String, Object>)object, configurer, serdesContext) : new DeserializationData((Map<String, Object>)Collections.singletonMap((Object)"$$__value__$$", object), configurer, serdesContext);
            final Object deserialized = objectSerializer.deserialize(deserializationData, target);
            return targetClazz.cast(deserialized);
        }
        if (OkaeriConfig.class.isAssignableFrom(targetClazz)) {
            final OkaeriConfig config = ConfigManager.createUnsafe((Class<OkaeriConfig>)targetClazz);
            final Map configMap = this.resolveType(object, source, Map.class, GenericsDeclaration.of(Map.class, (List<Object>)Arrays.asList(new Object[] { String.class, Object.class })), serdesContext);
            config.setConfigurer(new InMemoryWrappedConfigurer(this, (Map<String, Object>)configMap));
            return (T)config.update();
        }
        if (genericTarget != null) {
            final Class<T> localTargetClazz = (Class<T>)this.resolveTargetBaseType(serdesContext, target, source);
            if (object instanceof Collection && Collection.class.isAssignableFrom(localTargetClazz)) {
                final Collection<?> sourceList = (Collection<?>)object;
                final Collection<Object> targetList = (Collection<Object>)this.createInstance(localTargetClazz);
                final GenericsDeclaration listDeclaration = genericTarget.getSubtypeAtOrNull(0);
                for (final Object item : sourceList) {
                    final Object converted = this.resolveType(item, GenericsDeclaration.of(item), listDeclaration.getType(), listDeclaration, serdesContext);
                    targetList.add(converted);
                }
                return localTargetClazz.cast(targetList);
            }
            if (object instanceof Map && Map.class.isAssignableFrom(localTargetClazz)) {
                final Map<Object, Object> values = (Map<Object, Object>)object;
                final GenericsDeclaration keyDeclaration = genericTarget.getSubtypeAtOrNull(0);
                final GenericsDeclaration valueDeclaration = genericTarget.getSubtypeAtOrNull(1);
                final Map<Object, Object> map = (Map<Object, Object>)this.createInstance(localTargetClazz);
                for (final Map.Entry<Object, Object> entry : values.entrySet()) {
                    final Object key = this.resolveType(entry.getKey(), GenericsDeclaration.of(entry.getKey()), keyDeclaration.getType(), keyDeclaration, serdesContext);
                    final Object value = this.resolveType(entry.getValue(), GenericsDeclaration.of(entry.getValue()), valueDeclaration.getType(), valueDeclaration, serdesContext);
                    map.put(key, value);
                }
                return localTargetClazz.cast(map);
            }
        }
        final ObjectTransformer transformer = this.registry.getTransformer(source, target);
        if (transformer == null) {
            final Class<?> objectClazz = object.getClass();
            try {
                if (object instanceof String && target.isEnum()) {
                    final String strObject = (String)object;
                    try {
                        final Method enumMethod = targetClazz.getMethod("valueOf", String.class);
                        final Object enumValue = enumMethod.invoke((Object)null, new Object[] { strObject });
                        if (enumValue != null) {
                            return targetClazz.cast(enumValue);
                        }
                    }
                    catch (final InvocationTargetException ignored) {
                        final Enum[] array;
                        final Enum[] enumValues = array = (Enum[])targetClazz.getEnumConstants();
                        for (final Enum value2 : array) {
                            if (strObject.equalsIgnoreCase(value2.name())) {
                                return targetClazz.cast(value2);
                            }
                        }
                    }
                    final String enumValuesStr = (String)Arrays.stream((Object[])targetClazz.getEnumConstants()).map(item -> ((Enum)item).name()).collect(Collectors.joining((CharSequence)", "));
                    throw new IllegalArgumentException("no enum value for name " + strObject + " (available: " + enumValuesStr + ")");
                }
                if (source.isEnum() && targetClazz == String.class) {
                    final Method enumMethod2 = objectClazz.getMethod("name", (Class<?>[])new Class[0]);
                    return targetClazz.cast(enumMethod2.invoke(object, new Object[0]));
                }
            }
            catch (final NoSuchMethodException | IllegalAccessException | InvocationTargetException exception) {
                throw new OkaeriException("failed to resolve enum " + (Object)object.getClass() + " <> " + (Object)targetClazz, (Throwable)exception);
            }
            if (targetClazz.isPrimitive() && GenericsDeclaration.doBoxTypesMatch(targetClazz, objectClazz)) {
                final GenericsDeclaration primitiveDeclaration = GenericsDeclaration.of(object);
                return (T)primitiveDeclaration.unwrapValue(object);
            }
            if (targetClazz.isPrimitive() || GenericsDeclaration.of(targetClazz).isPrimitiveWrapper()) {
                final Object simplified = this.simplify(object, GenericsDeclaration.of(objectClazz), serdesContext, false);
                return (T)this.resolveType(simplified, GenericsDeclaration.of(simplified), (Class<Object>)targetClazz, GenericsDeclaration.of(targetClazz), serdesContext);
            }
            final List<ObjectTransformer> transformersFrom = this.getRegistry().getTransformersFrom(source);
            for (final ObjectTransformer stepOneTransformer : transformersFrom) {
                final GenericsDeclaration stepOneTarget = stepOneTransformer.getPair().getTo();
                final ObjectTransformer stepTwoTransformer = this.getRegistry().getTransformer(stepOneTarget, target);
                if (stepTwoTransformer != null) {
                    final Object transformed = stepOneTransformer.transform(object, serdesContext);
                    final Object doubleTransformed = stepTwoTransformer.transform(transformed, serdesContext);
                    return targetClazz.cast(doubleTransformed);
                }
            }
            if (object instanceof Map && Serializable.class.isAssignableFrom(targetClazz)) {
                final T serializableInstance = UnsafeUtil.allocateInstance(targetClazz);
                final ConfigDeclaration declaration = ConfigDeclaration.of(targetClazz, serializableInstance);
                final Map serializableMap = this.resolveType(object, source, Map.class, GenericsDeclaration.of(Map.class, (List<Object>)Arrays.asList(new Object[] { String.class, Object.class })), serdesContext);
                for (final FieldDeclaration field : declaration.getFields()) {
                    final Object serializedValue = serializableMap.get((Object)field.getName());
                    if (serializedValue == null) {
                        continue;
                    }
                    final Object deserializedValue = this.resolveType(serializedValue, GenericsDeclaration.of(serializedValue), field.getType().getType(), field.getType(), SerdesContext.of(this, field));
                    try {
                        field.getField().set((Object)serializableInstance, deserializedValue);
                    }
                    catch (final IllegalAccessException exception2) {
                        throw new OkaeriException("cannot set field of serializable " + (Object)field, (Throwable)exception2);
                    }
                }
                return serializableInstance;
            }
            try {
                return targetClazz.cast(object);
            }
            catch (final ClassCastException exception3) {
                throw new OkaeriException("cannot resolve " + (Object)object.getClass() + " to " + (Object)targetClazz + " (" + (Object)source + " => " + (Object)target + "): " + object, (Throwable)exception3);
            }
        }
        if (targetClazz.isPrimitive()) {
            final Object transformed2 = transformer.transform(object, serdesContext);
            return (T)GenericsDeclaration.of(targetClazz).unwrapValue(transformed2);
        }
        return targetClazz.cast(transformer.transform(object, serdesContext));
    }
    
    public Class<?> resolveTargetBaseType(@NonNull final SerdesContext serdesContext, @NonNull final GenericsDeclaration target, @NonNull final GenericsDeclaration source) {
        if (serdesContext == null) {
            throw new NullPointerException("serdesContext is marked non-null but is null");
        }
        if (target == null) {
            throw new NullPointerException("target is marked non-null but is null");
        }
        if (source == null) {
            throw new NullPointerException("source is marked non-null but is null");
        }
        final FieldDeclaration serdesContextField = serdesContext.getField();
        final Class<?> targetType = target.getType();
        if (serdesContextField != null && !serdesContextField.getType().equals(target)) {
            return targetType;
        }
        if (serdesContextField == null) {
            return targetType;
        }
        final Optional<TargetType> targetTypeAnnotation = serdesContextField.getAnnotation(TargetType.class);
        if (targetTypeAnnotation.isPresent()) {
            return ((TargetType)targetTypeAnnotation.get()).value();
        }
        return targetType;
    }
    
    public Object createInstance(@NonNull final Class<?> clazz) throws OkaeriException {
        if (clazz == null) {
            throw new NullPointerException("clazz is marked non-null but is null");
        }
        try {
            if (Collection.class.isAssignableFrom(clazz)) {
                if (clazz == Set.class) {
                    return new LinkedHashSet();
                }
                if (clazz == List.class) {
                    return new ArrayList();
                }
                try {
                    return clazz.newInstance();
                }
                catch (final InstantiationException exception) {
                    throw new OkaeriException("cannot create instance of " + (Object)clazz + " (tip: provide implementation (e.g. ArrayList) for types with no default constructor using @TargetType annotation)", (Throwable)exception);
                }
            }
            if (Map.class.isAssignableFrom(clazz)) {
                if (clazz == Map.class) {
                    return new LinkedHashMap();
                }
                try {
                    return clazz.newInstance();
                }
                catch (final InstantiationException exception) {
                    throw new OkaeriException("cannot create instance of " + (Object)clazz + " (tip: provide implementation (e.g. LinkedHashMap) for types with no default constructor using @TargetType annotation)", (Throwable)exception);
                }
            }
            throw new OkaeriException("cannot create instance of " + (Object)clazz);
        }
        catch (final Exception exception2) {
            throw new OkaeriException("failed to create instance of " + (Object)clazz, (Throwable)exception2);
        }
    }
    
    public boolean keyExists(@NonNull final String key) {
        if (key == null) {
            throw new NullPointerException("key is marked non-null but is null");
        }
        return this.getValue(key) != null;
    }
    
    public boolean isValid(@NonNull final FieldDeclaration declaration, final Object value) {
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        return true;
    }
    
    public List<String> getAllKeys() {
        return (List<String>)this.getParent().getDeclaration().getFields().stream().map(FieldDeclaration::getName).collect(Collectors.toList());
    }
    
    public Set<String> sort(@NonNull final ConfigDeclaration declaration) {
        if (declaration == null) {
            throw new NullPointerException("declaration is marked non-null but is null");
        }
        final Map<String, Object> reordered = (Map<String, Object>)declaration.getFields().stream().collect(LinkedHashMap::new, (map, field) -> {
            final Object oldValue = this.getValueUnsafe(field.getName());
            this.remove(field.getName());
            map.put((Object)field.getName(), oldValue);
        }, HashMap::putAll);
        final Set<String> orphans = (Set<String>)new LinkedHashSet((Collection)this.getAllKeys());
        reordered.forEach(this::setValueUnsafe);
        return orphans;
    }
    
    public abstract void write(@NonNull final OutputStream outputStream, @NonNull final ConfigDeclaration declaration) throws Exception;
    
    public abstract void load(@NonNull final InputStream inputStream, @NonNull final ConfigDeclaration declaration) throws Exception;
    
    public OkaeriConfig getParent() {
        return this.parent;
    }
    
    public void setParent(final OkaeriConfig parent) {
        this.parent = parent;
    }
    
    public void setRegistry(@NonNull final SerdesRegistry registry) {
        if (registry == null) {
            throw new NullPointerException("registry is marked non-null but is null");
        }
        this.registry = registry;
    }
    
    @NonNull
    public SerdesRegistry getRegistry() {
        return this.registry;
    }
}
