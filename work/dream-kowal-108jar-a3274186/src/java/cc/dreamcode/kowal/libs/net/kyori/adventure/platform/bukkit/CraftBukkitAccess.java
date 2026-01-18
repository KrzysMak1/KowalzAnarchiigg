package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import org.bukkit.inventory.ItemStack;
import java.util.List;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import java.util.Optional;
import java.lang.invoke.MethodHandle;
import org.jetbrains.annotations.Nullable;

final class CraftBukkitAccess
{
    @Nullable
    static final Class<?> CLASS_CHAT_COMPONENT;
    @Nullable
    static final Class<?> CLASS_REGISTRY;
    @Nullable
    static final Class<?> CLASS_SERVER_LEVEL;
    @Nullable
    static final Class<?> CLASS_LEVEL;
    @Nullable
    static final Class<?> CLASS_REGISTRY_ACCESS;
    @Nullable
    static final Class<?> CLASS_RESOURCE_KEY;
    @Nullable
    static final Class<?> CLASS_RESOURCE_LOCATION;
    @Nullable
    static final Class<?> CLASS_NMS_ENTITY;
    @Nullable
    static final Class<?> CLASS_BUILT_IN_REGISTRIES;
    @Nullable
    static final Class<?> CLASS_HOLDER;
    @Nullable
    static final Class<?> CLASS_WRITABLE_REGISTRY;
    
    private CraftBukkitAccess() {
    }
    
    static {
        CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
        CLASS_REGISTRY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IRegistry"), MinecraftReflection.findMcClassName("core.IRegistry"), MinecraftReflection.findMcClassName("core.Registry"));
        CLASS_SERVER_LEVEL = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("server.level.WorldServer"), MinecraftReflection.findMcClassName("server.level.ServerLevel"));
        CLASS_LEVEL = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("world.level.World"), MinecraftReflection.findMcClassName("world.level.Level"));
        CLASS_REGISTRY_ACCESS = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.IRegistryCustom"), MinecraftReflection.findMcClassName("core.RegistryAccess"));
        CLASS_RESOURCE_KEY = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("resources.ResourceKey"));
        CLASS_RESOURCE_LOCATION = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("MinecraftKey"), MinecraftReflection.findMcClassName("resources.MinecraftKey"), MinecraftReflection.findMcClassName("resources.ResourceLocation"));
        CLASS_NMS_ENTITY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Entity"), MinecraftReflection.findMcClassName("world.entity.Entity"));
        CLASS_BUILT_IN_REGISTRIES = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.registries.BuiltInRegistries"));
        CLASS_HOLDER = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("core.Holder"));
        CLASS_WRITABLE_REGISTRY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IRegistryWritable"), MinecraftReflection.findMcClassName("core.IRegistryWritable"), MinecraftReflection.findMcClassName("core.WritableRegistry"));
    }
    
    static final class Chat1_19_3
    {
        @Nullable
        static final MethodHandle NEW_RESOURCE_LOCATION;
        @Nullable
        static final MethodHandle RESOURCE_KEY_CREATE;
        @Nullable
        static final MethodHandle SERVER_PLAYER_GET_LEVEL;
        @Nullable
        static final MethodHandle SERVER_LEVEL_GET_REGISTRY_ACCESS;
        @Nullable
        static final MethodHandle LEVEL_GET_REGISTRY_ACCESS;
        @Nullable
        static final MethodHandle ACTUAL_GET_REGISTRY_ACCESS;
        @Nullable
        static final MethodHandle REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL;
        @Nullable
        static final MethodHandle REGISTRY_GET_OPTIONAL;
        @Nullable
        static final MethodHandle REGISTRY_GET_HOLDER;
        @Nullable
        static final MethodHandle REGISTRY_GET_ID;
        @Nullable
        static final MethodHandle DISGUISED_CHAT_PACKET_CONSTRUCTOR;
        @Nullable
        static final MethodHandle CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR;
        @Nullable
        static final MethodHandle CHAT_TYPE_BOUND_CONSTRUCTOR;
        static final Object CHAT_TYPE_RESOURCE_KEY;
        
        private Chat1_19_3() {
        }
        
        static boolean isSupported() {
            return Chat1_19_3.ACTUAL_GET_REGISTRY_ACCESS != null && Chat1_19_3.REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL != null && Chat1_19_3.REGISTRY_GET_OPTIONAL != null && (Chat1_19_3.CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR != null || Chat1_19_3.CHAT_TYPE_BOUND_CONSTRUCTOR != null) && Chat1_19_3.DISGUISED_CHAT_PACKET_CONSTRUCTOR != null && Chat1_19_3.CHAT_TYPE_RESOURCE_KEY != null;
        }
        
        static {
            NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CraftBukkitAccess.CLASS_RESOURCE_LOCATION, String.class, String.class);
            RESOURCE_KEY_CREATE = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_RESOURCE_KEY, 9, "create", CraftBukkitAccess.CLASS_RESOURCE_KEY, CraftBukkitAccess.CLASS_RESOURCE_KEY, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            SERVER_PLAYER_GET_LEVEL = MinecraftReflection.searchMethod(CraftBukkitFacet.CRAFT_PLAYER_GET_HANDLE.type().returnType(), 1, "getLevel", CraftBukkitAccess.CLASS_SERVER_LEVEL, (Class<?>[])new Class[0]);
            SERVER_LEVEL_GET_REGISTRY_ACCESS = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_SERVER_LEVEL, 1, "registryAccess", CraftBukkitAccess.CLASS_REGISTRY_ACCESS, (Class<?>[])new Class[0]);
            LEVEL_GET_REGISTRY_ACCESS = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_LEVEL, 1, "registryAccess", CraftBukkitAccess.CLASS_REGISTRY_ACCESS, (Class<?>[])new Class[0]);
            ACTUAL_GET_REGISTRY_ACCESS = ((Chat1_19_3.SERVER_LEVEL_GET_REGISTRY_ACCESS == null) ? Chat1_19_3.LEVEL_GET_REGISTRY_ACCESS : Chat1_19_3.SERVER_LEVEL_GET_REGISTRY_ACCESS);
            REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY_ACCESS, 1, "registry", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_KEY);
            REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "getOptional", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            REGISTRY_GET_HOLDER = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "getHolder", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            REGISTRY_GET_ID = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "getId", Integer.TYPE, Object.class);
            MethodHandle boundNetworkConstructor = null;
            MethodHandle boundConstructor = null;
            MethodHandle disguisedChatPacketConstructor = null;
            Object chatTypeResourceKey = null;
            try {
                Class<?> classChatTypeBoundNetwork = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatType$BoundNetwork"));
                if (classChatTypeBoundNetwork == null) {
                    final Class<?> parentClass = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatMessageType"));
                    if (parentClass != null) {
                        for (final Class<?> childClass : parentClass.getClasses()) {
                            boundNetworkConstructor = MinecraftReflection.findConstructor(childClass, Integer.TYPE, CraftBukkitAccess.CLASS_CHAT_COMPONENT, CraftBukkitAccess.CLASS_CHAT_COMPONENT);
                            if (boundNetworkConstructor != null) {
                                classChatTypeBoundNetwork = childClass;
                                break;
                            }
                        }
                    }
                }
                Class<?> classChatTypeBound = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatType$BoundNetwork"));
                if (classChatTypeBound == null) {
                    final Class<?> parentClass2 = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.chat.ChatMessageType"));
                    if (parentClass2 != null) {
                        for (final Class<?> childClass2 : parentClass2.getClasses()) {
                            boundConstructor = MinecraftReflection.findConstructor(childClass2, CraftBukkitAccess.CLASS_HOLDER, CraftBukkitAccess.CLASS_CHAT_COMPONENT, Optional.class);
                            if (boundConstructor != null) {
                                classChatTypeBound = childClass2;
                                break;
                            }
                        }
                    }
                }
                final Class<?> disguisedChatPacketClass = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.protocol.game.ClientboundDisguisedChatPacket"));
                if (disguisedChatPacketClass != null) {
                    if (classChatTypeBoundNetwork != null) {
                        disguisedChatPacketConstructor = MinecraftReflection.findConstructor(disguisedChatPacketClass, CraftBukkitAccess.CLASS_CHAT_COMPONENT, classChatTypeBoundNetwork);
                    }
                    else if (classChatTypeBound != null) {
                        disguisedChatPacketConstructor = MinecraftReflection.findConstructor(disguisedChatPacketClass, CraftBukkitAccess.CLASS_CHAT_COMPONENT, classChatTypeBound);
                    }
                }
                if (Chat1_19_3.NEW_RESOURCE_LOCATION != null && Chat1_19_3.RESOURCE_KEY_CREATE != null) {
                    final MethodHandle createRegistryKey = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_RESOURCE_KEY, 9, "createRegistryKey", CraftBukkitAccess.CLASS_RESOURCE_KEY, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
                    if (createRegistryKey != null) {
                        chatTypeResourceKey = createRegistryKey.invoke(Chat1_19_3.NEW_RESOURCE_LOCATION.invoke("minecraft", "chat_type"));
                    }
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to initialize 1.19.3 chat support", new Object[0]);
            }
            DISGUISED_CHAT_PACKET_CONSTRUCTOR = disguisedChatPacketConstructor;
            CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR = boundNetworkConstructor;
            CHAT_TYPE_BOUND_CONSTRUCTOR = boundConstructor;
            CHAT_TYPE_RESOURCE_KEY = chatTypeResourceKey;
        }
    }
    
    static final class EntitySound
    {
        @Nullable
        static final Class<?> CLASS_CLIENTBOUND_ENTITY_SOUND;
        @Nullable
        static final Class<?> CLASS_SOUND_SOURCE;
        @Nullable
        static final Class<?> CLASS_SOUND_EVENT;
        @Nullable
        static final MethodHandle SOUND_SOURCE_GET_NAME;
        
        private EntitySound() {
        }
        
        static boolean isSupported() {
            return EntitySound.SOUND_SOURCE_GET_NAME != null;
        }
        
        static {
            CLASS_CLIENTBOUND_ENTITY_SOUND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutEntitySound"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutEntitySound"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundSoundEntityPacket"));
            CLASS_SOUND_SOURCE = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("SoundCategory"), MinecraftReflection.findMcClassName("sounds.SoundCategory"), MinecraftReflection.findMcClassName("sounds.SoundSource"));
            CLASS_SOUND_EVENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("SoundEffect"), MinecraftReflection.findMcClassName("sounds.SoundEffect"), MinecraftReflection.findMcClassName("sounds.SoundEvent"));
            MethodHandle soundSourceGetName = null;
            if (EntitySound.CLASS_SOUND_SOURCE != null) {
                for (final Method method : EntitySound.CLASS_SOUND_SOURCE.getDeclaredMethods()) {
                    if (method.getReturnType().equals(String.class) && method.getParameterCount() == 0 && !"name".equals((Object)method.getName()) && Modifier.isPublic(method.getModifiers())) {
                        try {
                            soundSourceGetName = MinecraftReflection.lookup().unreflect(method);
                        }
                        catch (final IllegalAccessException ex) {}
                        break;
                    }
                }
            }
            SOUND_SOURCE_GET_NAME = soundSourceGetName;
        }
    }
    
    static final class EntitySound_1_19_3
    {
        @Nullable
        static final MethodHandle NEW_RESOURCE_LOCATION;
        @Nullable
        static final MethodHandle REGISTRY_GET_OPTIONAL;
        @Nullable
        static final MethodHandle REGISTRY_WRAP_AS_HOLDER;
        @Nullable
        static final MethodHandle SOUND_EVENT_CREATE_VARIABLE_RANGE;
        @Nullable
        static final MethodHandle NEW_CLIENTBOUND_ENTITY_SOUND;
        @Nullable
        static final Object SOUND_EVENT_REGISTRY;
        
        private EntitySound_1_19_3() {
        }
        
        static boolean isSupported() {
            return EntitySound_1_19_3.NEW_CLIENTBOUND_ENTITY_SOUND != null && EntitySound_1_19_3.SOUND_EVENT_REGISTRY != null && EntitySound_1_19_3.NEW_RESOURCE_LOCATION != null && EntitySound_1_19_3.REGISTRY_GET_OPTIONAL != null && EntitySound_1_19_3.REGISTRY_WRAP_AS_HOLDER != null && EntitySound_1_19_3.SOUND_EVENT_CREATE_VARIABLE_RANGE != null;
        }
        
        static {
            NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CraftBukkitAccess.CLASS_RESOURCE_LOCATION, String.class, String.class);
            REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "getOptional", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            REGISTRY_WRAP_AS_HOLDER = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "wrapAsHolder", CraftBukkitAccess.CLASS_HOLDER, Object.class);
            SOUND_EVENT_CREATE_VARIABLE_RANGE = MinecraftReflection.searchMethod(EntitySound.CLASS_SOUND_EVENT, 9, "createVariableRangeEvent", EntitySound.CLASS_SOUND_EVENT, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            NEW_CLIENTBOUND_ENTITY_SOUND = MinecraftReflection.findConstructor(EntitySound.CLASS_CLIENTBOUND_ENTITY_SOUND, CraftBukkitAccess.CLASS_HOLDER, EntitySound.CLASS_SOUND_SOURCE, CraftBukkitAccess.CLASS_NMS_ENTITY, Float.TYPE, Float.TYPE, Long.TYPE);
            Object soundEventRegistry = null;
            try {
                final Field soundEventRegistryField = MinecraftReflection.findField(CraftBukkitAccess.CLASS_BUILT_IN_REGISTRIES, CraftBukkitAccess.CLASS_REGISTRY, "SOUND_EVENT");
                if (soundEventRegistryField != null) {
                    soundEventRegistry = soundEventRegistryField.get((Object)null);
                }
                else if (CraftBukkitAccess.CLASS_BUILT_IN_REGISTRIES != null && EntitySound_1_19_3.REGISTRY_GET_OPTIONAL != null && EntitySound_1_19_3.NEW_RESOURCE_LOCATION != null) {
                    Object rootRegistry = null;
                    for (final Field field : CraftBukkitAccess.CLASS_BUILT_IN_REGISTRIES.getDeclaredFields()) {
                        final int mask = 26;
                        if ((field.getModifiers() & 0x1A) == 0x1A && field.getType().equals(CraftBukkitAccess.CLASS_WRITABLE_REGISTRY)) {
                            field.setAccessible(true);
                            rootRegistry = field.get((Object)null);
                            break;
                        }
                    }
                    if (rootRegistry != null) {
                        soundEventRegistry = EntitySound_1_19_3.REGISTRY_GET_OPTIONAL.invoke(rootRegistry, EntitySound_1_19_3.NEW_RESOURCE_LOCATION.invoke("minecraft", "sound_event")).orElse((Object)null);
                    }
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to initialize EntitySound_1_19_3 CraftBukkit facet", new Object[0]);
            }
            SOUND_EVENT_REGISTRY = soundEventRegistry;
        }
    }
    
    static final class Book_1_20_5
    {
        static final Class<?> CLASS_CRAFT_ITEMSTACK;
        static final Class<?> CLASS_MC_ITEMSTACK;
        static final Class<?> CLASS_MC_DATA_COMPONENT_TYPE;
        static final Class<?> CLASS_MC_BOOK_CONTENT;
        static final Class<?> CLASS_MC_FILTERABLE;
        static final Class<?> CLASS_CRAFT_REGISTRY;
        static final MethodHandle CREATE_FILTERABLE;
        static final MethodHandle GET_REGISTRY;
        static final MethodHandle CREATE_REGISTRY_KEY;
        static final MethodHandle NEW_RESOURCE_LOCATION;
        static final MethodHandle NEW_BOOK_CONTENT;
        static final MethodHandle REGISTRY_GET_OPTIONAL;
        static final Class<?> CLASS_ENUM_HAND;
        static final Object HAND_MAIN;
        static final MethodHandle MC_ITEMSTACK_SET;
        static final MethodHandle CRAFT_ITEMSTACK_NMS_COPY;
        static final MethodHandle CRAFT_ITEMSTACK_CRAFT_MIRROR;
        static final Object WRITTEN_BOOK_COMPONENT_TYPE;
        static final Class<?> PACKET_OPEN_BOOK;
        static final MethodHandle NEW_PACKET_OPEN_BOOK;
        
        static boolean isSupported() {
            return Book_1_20_5.WRITTEN_BOOK_COMPONENT_TYPE != null && Book_1_20_5.CREATE_FILTERABLE != null && Book_1_20_5.NEW_BOOK_CONTENT != null && Book_1_20_5.CRAFT_ITEMSTACK_NMS_COPY != null && Book_1_20_5.MC_ITEMSTACK_SET != null && Book_1_20_5.CRAFT_ITEMSTACK_CRAFT_MIRROR != null && Book_1_20_5.NEW_PACKET_OPEN_BOOK != null && Book_1_20_5.HAND_MAIN != null;
        }
        
        static {
            CLASS_CRAFT_ITEMSTACK = MinecraftReflection.findCraftClass("inventory.CraftItemStack");
            CLASS_MC_ITEMSTACK = MinecraftReflection.findMcClass("world.item.ItemStack");
            CLASS_MC_DATA_COMPONENT_TYPE = MinecraftReflection.findMcClass("core.component.DataComponentType");
            CLASS_MC_BOOK_CONTENT = MinecraftReflection.findMcClass("world.item.component.WrittenBookContent");
            CLASS_MC_FILTERABLE = MinecraftReflection.findMcClass("server.network.Filterable");
            CLASS_CRAFT_REGISTRY = MinecraftReflection.findCraftClass("CraftRegistry");
            CREATE_FILTERABLE = MinecraftReflection.searchMethod(Book_1_20_5.CLASS_MC_FILTERABLE, 9, "passThrough", Book_1_20_5.CLASS_MC_FILTERABLE, Object.class);
            GET_REGISTRY = MinecraftReflection.findStaticMethod(Book_1_20_5.CLASS_CRAFT_REGISTRY, "getMinecraftRegistry", CraftBukkitAccess.CLASS_REGISTRY, CraftBukkitAccess.CLASS_RESOURCE_KEY);
            CREATE_REGISTRY_KEY = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_RESOURCE_KEY, 9, "createRegistryKey", CraftBukkitAccess.CLASS_RESOURCE_KEY, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CraftBukkitAccess.CLASS_RESOURCE_LOCATION, String.class, String.class);
            NEW_BOOK_CONTENT = MinecraftReflection.findConstructor(Book_1_20_5.CLASS_MC_BOOK_CONTENT, Book_1_20_5.CLASS_MC_FILTERABLE, String.class, Integer.TYPE, List.class, Boolean.TYPE);
            REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "getOptional", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            CLASS_ENUM_HAND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("EnumHand"), MinecraftReflection.findMcClassName("world.EnumHand"), MinecraftReflection.findMcClassName("world.InteractionHand"));
            HAND_MAIN = MinecraftReflection.findEnum(Book_1_20_5.CLASS_ENUM_HAND, "MAIN_HAND", 0);
            MC_ITEMSTACK_SET = MinecraftReflection.searchMethod(Book_1_20_5.CLASS_MC_ITEMSTACK, 1, "set", Object.class, Book_1_20_5.CLASS_MC_DATA_COMPONENT_TYPE, Object.class);
            CRAFT_ITEMSTACK_NMS_COPY = MinecraftReflection.findStaticMethod(Book_1_20_5.CLASS_CRAFT_ITEMSTACK, "asNMSCopy", Book_1_20_5.CLASS_MC_ITEMSTACK, ItemStack.class);
            CRAFT_ITEMSTACK_CRAFT_MIRROR = MinecraftReflection.findStaticMethod(Book_1_20_5.CLASS_CRAFT_ITEMSTACK, "asCraftMirror", Book_1_20_5.CLASS_CRAFT_ITEMSTACK, Book_1_20_5.CLASS_MC_ITEMSTACK);
            PACKET_OPEN_BOOK = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutOpenBook"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundOpenBookPacket"));
            NEW_PACKET_OPEN_BOOK = MinecraftReflection.findConstructor(Book_1_20_5.PACKET_OPEN_BOOK, Book_1_20_5.CLASS_ENUM_HAND);
            Object componentTypeRegistry = null;
            Object componentType = null;
            try {
                if (Book_1_20_5.GET_REGISTRY != null && Book_1_20_5.CREATE_REGISTRY_KEY != null && Book_1_20_5.NEW_RESOURCE_LOCATION != null && Book_1_20_5.REGISTRY_GET_OPTIONAL != null) {
                    final Object registryKey = Book_1_20_5.CREATE_REGISTRY_KEY.invoke(Book_1_20_5.NEW_RESOURCE_LOCATION.invoke("minecraft", "data_component_type"));
                    try {
                        componentTypeRegistry = Book_1_20_5.GET_REGISTRY.invoke(registryKey);
                    }
                    catch (final Exception ex) {}
                    if (componentTypeRegistry != null) {
                        componentType = Book_1_20_5.REGISTRY_GET_OPTIONAL.invoke(componentTypeRegistry, Book_1_20_5.NEW_RESOURCE_LOCATION.invoke("minecraft", "written_book_content")).orElse((Object)null);
                    }
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to initialize Book_1_20_5 CraftBukkit facet", new Object[0]);
            }
            WRITTEN_BOOK_COMPONENT_TYPE = componentType;
        }
    }
}
