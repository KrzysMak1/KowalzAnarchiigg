package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import java.util.Iterator;
import java.util.List;
import java.lang.invoke.MethodHandles;
import java.util.Comparator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import com.google.gson.JsonElement;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import java.util.concurrent.atomic.AtomicReference;
import java.lang.invoke.MethodHandle;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;

@ApiStatus.Experimental
public final class MinecraftComponentSerializer implements ComponentSerializer<Component, Component, Object>
{
    private static final MinecraftComponentSerializer INSTANCE;
    @Nullable
    private static final Class<?> CLASS_JSON_DESERIALIZER;
    @Nullable
    private static final Class<?> CLASS_CHAT_COMPONENT;
    @Nullable
    private static final Class<?> CLASS_CRAFT_REGISTRY;
    @Nullable
    private static final Class<?> CLASS_REGISTRY_ACCESS;
    @Nullable
    private static final MethodHandle GET_REGISTRY;
    private static final AtomicReference<RuntimeException> INITIALIZATION_ERROR;
    private static final Object MC_TEXT_GSON;
    private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE;
    private static final MethodHandle TEXT_SERIALIZER_SERIALIZE;
    private static final MethodHandle TEXT_SERIALIZER_DESERIALIZE_TREE;
    private static final MethodHandle TEXT_SERIALIZER_SERIALIZE_TREE;
    private static final boolean SUPPORTED;
    
    public static boolean isSupported() {
        return MinecraftComponentSerializer.SUPPORTED;
    }
    
    @NotNull
    public static MinecraftComponentSerializer get() {
        return MinecraftComponentSerializer.INSTANCE;
    }
    
    @NotNull
    @Override
    public Component deserialize(@NotNull final Object input) {
        if (!MinecraftComponentSerializer.SUPPORTED) {
            throw (RuntimeException)MinecraftComponentSerializer.INITIALIZATION_ERROR.get();
        }
        try {
            JsonElement element;
            if (MinecraftComponentSerializer.TEXT_SERIALIZER_SERIALIZE_TREE != null) {
                element = MinecraftComponentSerializer.TEXT_SERIALIZER_SERIALIZE_TREE.invoke(input);
            }
            else {
                if (MinecraftComponentSerializer.MC_TEXT_GSON == null) {
                    return ((ComponentSerializer<I, Component, String>)BukkitComponentSerializer.gson()).deserialize(MinecraftComponentSerializer.TEXT_SERIALIZER_SERIALIZE.invoke(input));
                }
                element = ((Gson)MinecraftComponentSerializer.MC_TEXT_GSON).toJsonTree(input);
            }
            return (Component)BukkitComponentSerializer.gson().serializer().fromJson(element, (Class)Component.class);
        }
        catch (final Throwable error) {
            throw new UnsupportedOperationException(error);
        }
    }
    
    @NotNull
    @Override
    public Object serialize(@NotNull final Component component) {
        if (!MinecraftComponentSerializer.SUPPORTED) {
            throw (RuntimeException)MinecraftComponentSerializer.INITIALIZATION_ERROR.get();
        }
        if (MinecraftComponentSerializer.TEXT_SERIALIZER_DESERIALIZE_TREE != null || MinecraftComponentSerializer.MC_TEXT_GSON != null) {
            final JsonElement json = BukkitComponentSerializer.gson().serializer().toJsonTree((Object)component);
            try {
                if (MinecraftComponentSerializer.TEXT_SERIALIZER_DESERIALIZE_TREE != null) {
                    return MinecraftComponentSerializer.TEXT_SERIALIZER_DESERIALIZE_TREE.invoke(json);
                }
                return ((Gson)MinecraftComponentSerializer.MC_TEXT_GSON).fromJson(json, (Class)MinecraftComponentSerializer.CLASS_CHAT_COMPONENT);
            }
            catch (final Throwable error) {
                throw new UnsupportedOperationException(error);
            }
        }
        try {
            return MinecraftComponentSerializer.TEXT_SERIALIZER_DESERIALIZE.invoke((String)((ComponentSerializer<Component, O, String>)BukkitComponentSerializer.gson()).serialize(component));
        }
        catch (final Throwable error2) {
            throw new UnsupportedOperationException(error2);
        }
    }
    
    static {
        INSTANCE = new MinecraftComponentSerializer();
        CLASS_JSON_DESERIALIZER = MinecraftReflection.findClass("com.goo".concat("gle.gson.JsonDeserializer"));
        CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
        CLASS_CRAFT_REGISTRY = MinecraftReflection.findCraftClass("CraftRegistry");
        CLASS_REGISTRY_ACCESS = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.IRegistryCustom"), MinecraftReflection.findMcClassName("core.RegistryAccess"));
        GET_REGISTRY = MinecraftReflection.findStaticMethod(MinecraftComponentSerializer.CLASS_CRAFT_REGISTRY, "getMinecraftRegistry", MinecraftComponentSerializer.CLASS_REGISTRY_ACCESS, (Class<?>[])new Class[0]);
        INITIALIZATION_ERROR = new AtomicReference((Object)new UnsupportedOperationException());
        Object gson = null;
        MethodHandle textSerializerDeserialize = null;
        MethodHandle textSerializerSerialize = null;
        MethodHandle textSerializerDeserializeTree = null;
        MethodHandle textSerializerSerializeTree = null;
        try {
            if (MinecraftComponentSerializer.CLASS_CHAT_COMPONENT != null) {
                final Object registryAccess = (MinecraftComponentSerializer.GET_REGISTRY != null) ? MinecraftComponentSerializer.GET_REGISTRY.invoke() : null;
                final Class<?> chatSerializerClass = (Class<?>)Arrays.stream((Object[])MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.getClasses()).filter(c -> {
                    if (MinecraftComponentSerializer.CLASS_JSON_DESERIALIZER != null) {
                        return MinecraftComponentSerializer.CLASS_JSON_DESERIALIZER.isAssignableFrom(c);
                    }
                    for (final Class<?> itf : c.getInterfaces()) {
                        if (itf.getSimpleName().equals((Object)"JsonDeserializer")) {
                            return true;
                        }
                    }
                    return false;
                }).findAny().orElse((Object)MinecraftReflection.findNmsClass("ChatSerializer"));
                if (chatSerializerClass != null) {
                    final Field gsonField = (Field)Arrays.stream((Object[])chatSerializerClass.getDeclaredFields()).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getType().equals(Gson.class)).findFirst().orElse((Object)null);
                    if (gsonField != null) {
                        gsonField.setAccessible(true);
                        gson = gsonField.get((Object)null);
                    }
                }
                final List<Class<?>> candidates = (List<Class<?>>)new ArrayList();
                if (chatSerializerClass != null) {
                    candidates.add((Object)chatSerializerClass);
                }
                candidates.addAll((Collection)Arrays.asList((Object[])MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.getClasses()));
                for (final Class<?> serializerClass : candidates) {
                    final Method[] declaredMethods = serializerClass.getDeclaredMethods();
                    final Method deserialize = (Method)Arrays.stream((Object[])declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType())).filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(String.class)).min(Comparator.comparing(Method::getName)).orElse((Object)null);
                    final Method serialize = (Method)Arrays.stream((Object[])declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getReturnType().equals(String.class)).filter(m -> m.getParameterCount() == 1 && MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0])).findFirst().orElse((Object)null);
                    final Method deserializeTree = (Method)Arrays.stream((Object[])declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType())).filter(m -> m.getParameterCount() == 1 && m.getParameterTypes()[0].equals(JsonElement.class)).findFirst().orElse((Object)null);
                    final Method serializeTree = (Method)Arrays.stream((Object[])declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getReturnType().equals(JsonElement.class)).filter(m -> m.getParameterCount() == 1 && MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0])).findFirst().orElse((Object)null);
                    final Method deserializeTreeWithRegistryAccess = (Method)Arrays.stream((Object[])declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.isAssignableFrom(m.getReturnType())).filter(m -> m.getParameterCount() == 2).filter(m -> m.getParameterTypes()[0].equals(JsonElement.class)).filter(m -> m.getParameterTypes()[1].isInstance(registryAccess)).findFirst().orElse((Object)null);
                    final Method serializeTreeWithRegistryAccess = (Method)Arrays.stream((Object[])declaredMethods).filter(m -> Modifier.isStatic(m.getModifiers())).filter(m -> m.getReturnType().equals(JsonElement.class)).filter(m -> m.getParameterCount() == 2).filter(m -> MinecraftComponentSerializer.CLASS_CHAT_COMPONENT.isAssignableFrom(m.getParameterTypes()[0])).filter(m -> m.getParameterTypes()[1].isInstance(registryAccess)).findFirst().orElse((Object)null);
                    if (deserialize != null) {
                        textSerializerDeserialize = MinecraftReflection.lookup().unreflect(deserialize);
                    }
                    if (serialize != null) {
                        textSerializerSerialize = MinecraftReflection.lookup().unreflect(serialize);
                    }
                    if (deserializeTree != null) {
                        textSerializerDeserializeTree = MinecraftReflection.lookup().unreflect(deserializeTree);
                    }
                    else if (deserializeTreeWithRegistryAccess != null) {
                        deserializeTreeWithRegistryAccess.setAccessible(true);
                        textSerializerDeserializeTree = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(deserializeTreeWithRegistryAccess), 1, new Object[] { registryAccess });
                    }
                    if (serializeTree != null) {
                        textSerializerSerializeTree = MinecraftReflection.lookup().unreflect(serializeTree);
                    }
                    else {
                        if (serializeTreeWithRegistryAccess == null) {
                            continue;
                        }
                        serializeTreeWithRegistryAccess.setAccessible(true);
                        textSerializerSerializeTree = MethodHandles.insertArguments(MinecraftReflection.lookup().unreflect(serializeTreeWithRegistryAccess), 1, new Object[] { registryAccess });
                    }
                }
            }
        }
        catch (final Throwable error) {
            MinecraftComponentSerializer.INITIALIZATION_ERROR.set((Object)new UnsupportedOperationException("Error occurred during initialization", error));
        }
        MC_TEXT_GSON = gson;
        TEXT_SERIALIZER_DESERIALIZE = textSerializerDeserialize;
        TEXT_SERIALIZER_SERIALIZE = textSerializerSerialize;
        TEXT_SERIALIZER_DESERIALIZE_TREE = textSerializerDeserializeTree;
        TEXT_SERIALIZER_SERIALIZE_TREE = textSerializerSerializeTree;
        SUPPORTED = (MinecraftComponentSerializer.MC_TEXT_GSON != null || (MinecraftComponentSerializer.TEXT_SERIALIZER_DESERIALIZE != null && MinecraftComponentSerializer.TEXT_SERIALIZER_SERIALIZE != null) || (MinecraftComponentSerializer.TEXT_SERIALIZER_DESERIALIZE_TREE != null && MinecraftComponentSerializer.TEXT_SERIALIZER_SERIALIZE_TREE != null));
    }
}
