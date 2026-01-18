package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import java.util.Arrays;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetComponentFlattener;
import org.bukkit.Server;
import java.util.LinkedList;
import org.bukkit.event.HandlerList;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import java.util.HashSet;
import org.bukkit.entity.Wither;
import org.bukkit.plugin.Plugin;
import java.util.Set;
import org.bukkit.World;
import org.bukkit.event.Listener;
import java.lang.invoke.LambdaMetafactory;
import java.util.function.Function;
import java.lang.invoke.MethodType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import java.util.Collection;
import java.lang.reflect.InvocationTargetException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.OutputStream;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.BinaryTagIO;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.BinaryTag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.StringBinaryTag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.ListBinaryTag;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.BinaryTagTypes;
import cc.dreamcode.kowal.libs.net.kyori.adventure.nbt.CompoundBinaryTag;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.entity.Entity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound;
import java.util.Map;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.TextComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.MessageType;
import java.util.Optional;
import java.util.NoSuchElementException;
import cc.dreamcode.kowal.libs.net.kyori.adventure.chat.ChatType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandles;
import java.util.UUID;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import java.lang.reflect.Modifier;
import org.jetbrains.annotations.Nullable;
import org.bukkit.entity.Player;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetBase;
import org.bukkit.command.CommandSender;

class CraftBukkitFacet<V extends CommandSender> extends FacetBase<V>
{
    private static final Class<?> CLASS_NMS_ENTITY;
    private static final Class<?> CLASS_CRAFT_ENTITY;
    private static final MethodHandle CRAFT_ENTITY_GET_HANDLE;
    @Nullable
    static final Class<? extends Player> CLASS_CRAFT_PLAYER;
    @Nullable
    static final MethodHandle CRAFT_PLAYER_GET_HANDLE;
    @Nullable
    private static final MethodHandle ENTITY_PLAYER_GET_CONNECTION;
    @Nullable
    private static final MethodHandle PLAYER_CONNECTION_SEND_PACKET;
    private static final boolean SUPPORTED;
    @Nullable
    private static final Class<?> CLASS_CHAT_COMPONENT;
    @Nullable
    private static final Class<?> CLASS_MESSAGE_TYPE;
    @Nullable
    private static final Object MESSAGE_TYPE_CHAT;
    @Nullable
    private static final Object MESSAGE_TYPE_SYSTEM;
    @Nullable
    private static final Object MESSAGE_TYPE_ACTIONBAR;
    @Nullable
    private static final MethodHandle LEGACY_CHAT_PACKET_CONSTRUCTOR;
    @Nullable
    private static final MethodHandle CHAT_PACKET_CONSTRUCTOR;
    @Nullable
    private static final Class<?> CLASS_TITLE_PACKET;
    @Nullable
    private static final Class<?> CLASS_TITLE_ACTION;
    private static final MethodHandle CONSTRUCTOR_TITLE_MESSAGE;
    @Nullable
    private static final MethodHandle CONSTRUCTOR_TITLE_TIMES;
    @Nullable
    private static final Object TITLE_ACTION_TITLE;
    @Nullable
    private static final Object TITLE_ACTION_SUBTITLE;
    @Nullable
    private static final Object TITLE_ACTION_ACTIONBAR;
    @Nullable
    private static final Object TITLE_ACTION_CLEAR;
    @Nullable
    private static final Object TITLE_ACTION_RESET;
    
    protected CraftBukkitFacet(@Nullable final Class<? extends V> viewerClass) {
        super(viewerClass);
    }
    
    @Override
    public boolean isSupported() {
        return super.isSupported() && CraftBukkitFacet.SUPPORTED;
    }
    
    static {
        CLASS_NMS_ENTITY = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Entity"), MinecraftReflection.findMcClassName("world.entity.Entity"));
        CLASS_CRAFT_ENTITY = MinecraftReflection.findCraftClass("entity.CraftEntity");
        CRAFT_ENTITY_GET_HANDLE = MinecraftReflection.findMethod(CraftBukkitFacet.CLASS_CRAFT_ENTITY, "getHandle", CraftBukkitFacet.CLASS_NMS_ENTITY, (Class<?>[])new Class[0]);
        CLASS_CRAFT_PLAYER = MinecraftReflection.findCraftClass("entity.CraftPlayer", Player.class);
        final Class<?> craftPlayerClass = MinecraftReflection.findCraftClass("entity.CraftPlayer");
        final Class<?> packetClass = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Packet"), MinecraftReflection.findMcClassName("network.protocol.Packet"));
        MethodHandle craftPlayerGetHandle = null;
        MethodHandle entityPlayerGetConnection = null;
        MethodHandle playerConnectionSendPacket = null;
        if (craftPlayerClass != null && packetClass != null) {
            try {
                final Method getHandleMethod = craftPlayerClass.getMethod("getHandle", (Class<?>[])new Class[0]);
                final Class<?> entityPlayerClass = getHandleMethod.getReturnType();
                craftPlayerGetHandle = MinecraftReflection.lookup().unreflect(getHandleMethod);
                final Field playerConnectionField = MinecraftReflection.findField(entityPlayerClass, "playerConnection", "connection");
                Class<?> playerConnectionClass = null;
                if (playerConnectionField != null) {
                    entityPlayerGetConnection = MinecraftReflection.lookup().unreflectGetter(playerConnectionField);
                    playerConnectionClass = playerConnectionField.getType();
                }
                else {
                    final Class<?> serverGamePacketListenerImpl = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PlayerConnection"), MinecraftReflection.findMcClassName("server.network.PlayerConnection"), MinecraftReflection.findMcClassName("server.network.ServerGamePacketListenerImpl"));
                    for (final Field field : entityPlayerClass.getDeclaredFields()) {
                        final int modifiers = field.getModifiers();
                        if (Modifier.isPublic(modifiers) && !Modifier.isFinal(modifiers) && (serverGamePacketListenerImpl == null || field.getType().equals(serverGamePacketListenerImpl))) {
                            entityPlayerGetConnection = MinecraftReflection.lookup().unreflectGetter(field);
                            playerConnectionClass = field.getType();
                        }
                    }
                }
                final Class<?> serverCommonPacketListenerImpl = MinecraftReflection.findClass(MinecraftReflection.findMcClassName("server.network.ServerCommonPacketListenerImpl"));
                if (serverCommonPacketListenerImpl != null) {
                    playerConnectionClass = serverCommonPacketListenerImpl;
                }
                playerConnectionSendPacket = MinecraftReflection.searchMethod(playerConnectionClass, 1, new String[] { "sendPacket", "send" }, Void.TYPE, packetClass);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to initialize CraftBukkit sendPacket", new Object[0]);
            }
        }
        CRAFT_PLAYER_GET_HANDLE = craftPlayerGetHandle;
        ENTITY_PLAYER_GET_CONNECTION = entityPlayerGetConnection;
        PLAYER_CONNECTION_SEND_PACKET = playerConnectionSendPacket;
        SUPPORTED = (Knob.isEnabled("craftbukkit", true) && MinecraftComponentSerializer.isSupported() && CraftBukkitFacet.CRAFT_PLAYER_GET_HANDLE != null && CraftBukkitFacet.ENTITY_PLAYER_GET_CONNECTION != null && CraftBukkitFacet.PLAYER_CONNECTION_SEND_PACKET != null);
        CLASS_CHAT_COMPONENT = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.IChatBaseComponent"), MinecraftReflection.findMcClassName("network.chat.Component"));
        CLASS_MESSAGE_TYPE = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("ChatMessageType"), MinecraftReflection.findMcClassName("network.chat.ChatMessageType"), MinecraftReflection.findMcClassName("network.chat.ChatType"));
        if (CraftBukkitFacet.CLASS_MESSAGE_TYPE != null && !CraftBukkitFacet.CLASS_MESSAGE_TYPE.isEnum()) {
            MESSAGE_TYPE_CHAT = 0;
            MESSAGE_TYPE_SYSTEM = 1;
            MESSAGE_TYPE_ACTIONBAR = 2;
        }
        else {
            MESSAGE_TYPE_CHAT = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_MESSAGE_TYPE, "CHAT", 0);
            MESSAGE_TYPE_SYSTEM = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_MESSAGE_TYPE, "SYSTEM", 1);
            MESSAGE_TYPE_ACTIONBAR = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_MESSAGE_TYPE, "GAME_INFO", 2);
        }
        MethodHandle legacyChatPacketConstructor = null;
        MethodHandle chatPacketConstructor = null;
        try {
            if (CraftBukkitFacet.CLASS_CHAT_COMPONENT != null) {
                final Class<?> chatPacketClass = MinecraftReflection.needClass(MinecraftReflection.findNmsClassName("PacketPlayOutChat"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutChat"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundChatPacket"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundSystemChatPacket"));
                if (CraftBukkitFacet.MESSAGE_TYPE_CHAT == Integer.valueOf(0)) {
                    chatPacketConstructor = MinecraftReflection.findConstructor(chatPacketClass, CraftBukkitFacet.CLASS_CHAT_COMPONENT, Boolean.TYPE);
                }
                if (chatPacketConstructor == null) {
                    chatPacketConstructor = MinecraftReflection.findConstructor(chatPacketClass, CraftBukkitFacet.CLASS_CHAT_COMPONENT, Integer.TYPE);
                }
                if (chatPacketConstructor == null) {
                    chatPacketConstructor = MinecraftReflection.findConstructor(chatPacketClass, CraftBukkitFacet.CLASS_CHAT_COMPONENT);
                }
                if (chatPacketConstructor == null) {
                    if (CraftBukkitFacet.CLASS_MESSAGE_TYPE != null) {
                        chatPacketConstructor = MinecraftReflection.findConstructor(chatPacketClass, CraftBukkitFacet.CLASS_CHAT_COMPONENT, CraftBukkitFacet.CLASS_MESSAGE_TYPE, UUID.class);
                    }
                }
                else if (CraftBukkitFacet.MESSAGE_TYPE_CHAT == Integer.valueOf(0)) {
                    if (chatPacketConstructor.type().parameterType(1).equals(Boolean.TYPE)) {
                        chatPacketConstructor = MethodHandles.insertArguments(chatPacketConstructor, 1, new Object[] { Boolean.FALSE });
                        chatPacketConstructor = MethodHandles.dropArguments(chatPacketConstructor, 1, new Class[] { Integer.class, UUID.class });
                    }
                    else {
                        chatPacketConstructor = MethodHandles.dropArguments(chatPacketConstructor, 2, new Class[] { UUID.class });
                    }
                }
                else {
                    chatPacketConstructor = MethodHandles.dropArguments(chatPacketConstructor, 1, new Class[] { (CraftBukkitFacet.CLASS_MESSAGE_TYPE == null) ? Object.class : CraftBukkitFacet.CLASS_MESSAGE_TYPE, UUID.class });
                }
                legacyChatPacketConstructor = MinecraftReflection.findConstructor(chatPacketClass, CraftBukkitFacet.CLASS_CHAT_COMPONENT, Byte.TYPE);
                if (legacyChatPacketConstructor == null) {
                    legacyChatPacketConstructor = MinecraftReflection.findConstructor(chatPacketClass, CraftBukkitFacet.CLASS_CHAT_COMPONENT, Integer.TYPE);
                }
            }
        }
        catch (final Throwable error2) {
            Knob.logError(error2, "Failed to initialize ClientboundChatPacket constructor", new Object[0]);
        }
        CHAT_PACKET_CONSTRUCTOR = chatPacketConstructor;
        LEGACY_CHAT_PACKET_CONSTRUCTOR = legacyChatPacketConstructor;
        CLASS_TITLE_PACKET = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutTitle"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutTitle"));
        CLASS_TITLE_ACTION = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutTitle$EnumTitleAction"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutTitle$EnumTitleAction"));
        CONSTRUCTOR_TITLE_MESSAGE = MinecraftReflection.findConstructor(CraftBukkitFacet.CLASS_TITLE_PACKET, CraftBukkitFacet.CLASS_TITLE_ACTION, CraftBukkitFacet.CLASS_CHAT_COMPONENT);
        CONSTRUCTOR_TITLE_TIMES = MinecraftReflection.findConstructor(CraftBukkitFacet.CLASS_TITLE_PACKET, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        TITLE_ACTION_TITLE = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_TITLE_ACTION, "TITLE", 0);
        TITLE_ACTION_SUBTITLE = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_TITLE_ACTION, "SUBTITLE", 1);
        TITLE_ACTION_ACTIONBAR = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_TITLE_ACTION, "ACTIONBAR");
        TITLE_ACTION_CLEAR = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_TITLE_ACTION, "CLEAR");
        TITLE_ACTION_RESET = MinecraftReflection.findEnum(CraftBukkitFacet.CLASS_TITLE_ACTION, "RESET");
    }
    
    static class PacketFacet<V extends CommandSender> extends CraftBukkitFacet<V> implements Facet.Message<V, Object>
    {
        protected PacketFacet() {
            super((Class<? extends CommandSender>)PacketFacet.CLASS_CRAFT_PLAYER);
        }
        
        public void sendPacket(@NotNull final Player player, @Nullable final Object packet) {
            if (packet == null) {
                return;
            }
            try {
                CraftBukkitFacet.PLAYER_CONNECTION_SEND_PACKET.invoke(CraftBukkitFacet.ENTITY_PLAYER_GET_CONNECTION.invoke(PacketFacet.CRAFT_PLAYER_GET_HANDLE.invoke(player)), packet);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke CraftBukkit sendPacket: %s", packet);
            }
        }
        
        public void sendMessage(@NotNull final V player, @Nullable final Object packet) {
            this.sendPacket((Player)player, packet);
        }
        
        @Nullable
        @Override
        public Object createMessage(@NotNull final V viewer, @NotNull final Component message) {
            try {
                return MinecraftComponentSerializer.get().serialize(message);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to serialize net.minecraft.server IChatBaseComponent: %s", message);
                return null;
            }
        }
    }
    
    static class Chat1_19_3 extends Chat
    {
        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitAccess.Chat1_19_3.isSupported();
        }
        
        @Override
        public void sendMessage(@NotNull final CommandSender viewer, @NotNull final Identity source, @NotNull final Object message, @NotNull final Object type) {
            if (!(type instanceof ChatType.Bound)) {
                super.sendMessage(viewer, source, message, type);
            }
            else {
                final ChatType.Bound bound = (ChatType.Bound)type;
                try {
                    final Object nameComponent = this.createMessage(viewer, bound.name());
                    final Object targetComponent = (bound.target() != null) ? this.createMessage(viewer, bound.target()) : null;
                    final Object registryAccess = CraftBukkitAccess.Chat1_19_3.ACTUAL_GET_REGISTRY_ACCESS.invoke(CraftBukkitAccess.Chat1_19_3.SERVER_PLAYER_GET_LEVEL.invoke(Chat1_19_3.CRAFT_PLAYER_GET_HANDLE.invoke(viewer)));
                    final Object chatTypeRegistry = CraftBukkitAccess.Chat1_19_3.REGISTRY_ACCESS_GET_REGISTRY_OPTIONAL.invoke(registryAccess, CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_RESOURCE_KEY).orElseThrow(NoSuchElementException::new);
                    final Object typeResourceLocation = CraftBukkitAccess.Chat1_19_3.NEW_RESOURCE_LOCATION.invoke(bound.type().key().namespace(), bound.type().key().value());
                    Object boundNetwork;
                    if (CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR != null) {
                        final Object chatTypeObject = CraftBukkitAccess.Chat1_19_3.REGISTRY_GET_OPTIONAL.invoke(chatTypeRegistry, typeResourceLocation).orElseThrow(NoSuchElementException::new);
                        final int networkId = CraftBukkitAccess.Chat1_19_3.REGISTRY_GET_ID.invoke(chatTypeRegistry, chatTypeObject);
                        if (networkId < 0) {
                            throw new IllegalArgumentException("Could not get a valid network id from " + type);
                        }
                        boundNetwork = CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_BOUND_NETWORK_CONSTRUCTOR.invoke(networkId, nameComponent, targetComponent);
                    }
                    else {
                        final Object chatTypeHolder = CraftBukkitAccess.Chat1_19_3.REGISTRY_GET_HOLDER.invoke(chatTypeRegistry, typeResourceLocation).orElseThrow(NoSuchElementException::new);
                        boundNetwork = CraftBukkitAccess.Chat1_19_3.CHAT_TYPE_BOUND_CONSTRUCTOR.invoke(chatTypeHolder, nameComponent, Optional.ofNullable(targetComponent));
                    }
                    this.sendMessage(viewer, CraftBukkitAccess.Chat1_19_3.DISGUISED_CHAT_PACKET_CONSTRUCTOR.invoke(message, boundNetwork));
                }
                catch (final Throwable error) {
                    Knob.logError(error, "Failed to send a 1.19.3+ message: %s %s", message, type);
                }
            }
        }
    }
    
    static class Chat extends PacketFacet<CommandSender> implements Facet.Chat<CommandSender, Object>
    {
        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitFacet.CHAT_PACKET_CONSTRUCTOR != null;
        }
        
        @Override
        public void sendMessage(@NotNull final CommandSender viewer, @NotNull final Identity source, @NotNull final Object message, @NotNull final Object type) {
            final Object messageType = (type == MessageType.CHAT) ? CraftBukkitFacet.MESSAGE_TYPE_CHAT : CraftBukkitFacet.MESSAGE_TYPE_SYSTEM;
            try {
                this.sendMessage(viewer, CraftBukkitFacet.CHAT_PACKET_CONSTRUCTOR.invoke(message, messageType, source.uuid()));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke PacketPlayOutChat constructor: %s %s", message, messageType);
            }
        }
    }
    
    static class ActionBar_1_17 extends PacketFacet<Player> implements Facet.ActionBar<Player, Object>
    {
        @Nullable
        private static final Class<?> CLASS_SET_ACTION_BAR_TEXT_PACKET;
        @Nullable
        private static final MethodHandle CONSTRUCTOR_ACTION_BAR;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && ActionBar_1_17.CONSTRUCTOR_ACTION_BAR != null;
        }
        
        @Nullable
        @Override
        public Object createMessage(@NotNull final Player viewer, @NotNull final Component message) {
            try {
                return ActionBar_1_17.CONSTRUCTOR_ACTION_BAR.invoke(super.createMessage(viewer, message));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke PacketPlayOutTitle constructor: %s", message);
                return null;
            }
        }
        
        static {
            CLASS_SET_ACTION_BAR_TEXT_PACKET = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetActionBarTextPacket");
            CONSTRUCTOR_ACTION_BAR = MinecraftReflection.findConstructor(ActionBar_1_17.CLASS_SET_ACTION_BAR_TEXT_PACKET, CraftBukkitFacet.CLASS_CHAT_COMPONENT);
        }
    }
    
    static class ActionBar extends PacketFacet<Player> implements Facet.ActionBar<Player, Object>
    {
        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitFacet.TITLE_ACTION_ACTIONBAR != null;
        }
        
        @Nullable
        @Override
        public Object createMessage(@NotNull final Player viewer, @NotNull final Component message) {
            try {
                return CraftBukkitFacet.CONSTRUCTOR_TITLE_MESSAGE.invoke(CraftBukkitFacet.TITLE_ACTION_ACTIONBAR, super.createMessage(viewer, message));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke PacketPlayOutTitle constructor: %s", message);
                return null;
            }
        }
    }
    
    static class ActionBarLegacy extends PacketFacet<Player> implements Facet.ActionBar<Player, Object>
    {
        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitFacet.LEGACY_CHAT_PACKET_CONSTRUCTOR != null;
        }
        
        @Nullable
        @Override
        public Object createMessage(@NotNull final Player viewer, @NotNull final Component message) {
            final TextComponent legacyMessage = Component.text(BukkitComponentSerializer.legacy().serialize(message));
            try {
                return CraftBukkitFacet.LEGACY_CHAT_PACKET_CONSTRUCTOR.invoke(super.createMessage(viewer, legacyMessage), (byte)2);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke PacketPlayOutChat constructor: %s", legacyMessage);
                return null;
            }
        }
    }
    
    private interface PartialEntitySound extends Facet.EntitySound<Player, Object>
    {
        public static final Map<String, Object> MC_SOUND_SOURCE_BY_NAME = new ConcurrentHashMap();
        
        default Object createForSelf(final Player viewer, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound) {
            return this.createForEntity(sound, (Entity)viewer);
        }
        
        default Object createForEmitter(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Emitter emitter) {
            Entity entity;
            if (emitter instanceof BukkitEmitter) {
                entity = ((BukkitEmitter)emitter).entity;
            }
            else {
                if (!(emitter instanceof Entity)) {
                    return null;
                }
                entity = (Entity)emitter;
            }
            return this.createForEntity(sound, entity);
        }
        
        default Object toNativeEntity(final Entity entity) throws Throwable {
            if (!CraftBukkitFacet.CLASS_CRAFT_ENTITY.isInstance(entity)) {
                return null;
            }
            return CraftBukkitFacet.CRAFT_ENTITY_GET_HANDLE.invoke(entity);
        }
        
        default Object toVanilla(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source source) throws Throwable {
            if (PartialEntitySound.MC_SOUND_SOURCE_BY_NAME.isEmpty()) {
                for (final Object enumConstant : CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE.getEnumConstants()) {
                    PartialEntitySound.MC_SOUND_SOURCE_BY_NAME.put((Object)CraftBukkitAccess.EntitySound.SOUND_SOURCE_GET_NAME.invoke(enumConstant), enumConstant);
                }
            }
            return PartialEntitySound.MC_SOUND_SOURCE_BY_NAME.get((Object)cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound.Source.NAMES.key(source));
        }
        
        Object createForEntity(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, final Entity entity);
    }
    
    static class EntitySound_1_19_3 extends PacketFacet<Player> implements PartialEntitySound
    {
        @Override
        public boolean isSupported() {
            return CraftBukkitAccess.EntitySound_1_19_3.isSupported() && super.isSupported();
        }
        
        @Override
        public Object createForEntity(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, final Entity entity) {
            try {
                final Object resLoc = CraftBukkitAccess.EntitySound_1_19_3.NEW_RESOURCE_LOCATION.invoke(sound.name().namespace(), sound.name().value());
                final Optional<?> possibleSoundEvent = (Optional<?>)CraftBukkitAccess.EntitySound_1_19_3.REGISTRY_GET_OPTIONAL.invoke(CraftBukkitAccess.EntitySound_1_19_3.SOUND_EVENT_REGISTRY, resLoc);
                Object soundEvent;
                if (possibleSoundEvent.isPresent()) {
                    soundEvent = possibleSoundEvent.get();
                }
                else {
                    soundEvent = CraftBukkitAccess.EntitySound_1_19_3.SOUND_EVENT_CREATE_VARIABLE_RANGE.invoke(resLoc);
                }
                final Object soundEventHolder = CraftBukkitAccess.EntitySound_1_19_3.REGISTRY_WRAP_AS_HOLDER.invoke(CraftBukkitAccess.EntitySound_1_19_3.SOUND_EVENT_REGISTRY, soundEvent);
                final long seed = sound.seed().orElseGet(() -> ThreadLocalRandom.current().nextLong());
                return CraftBukkitAccess.EntitySound_1_19_3.NEW_CLIENTBOUND_ENTITY_SOUND.invoke(soundEventHolder, this.toVanilla(sound.source()), this.toNativeEntity(entity), sound.volume(), sound.pitch(), seed);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to send sound tracking an entity", new Object[0]);
                return null;
            }
        }
        
        @Override
        public void playSound(@NotNull final Player viewer, final Object packet) {
            this.sendPacket(viewer, packet);
        }
    }
    
    static class EntitySound extends PacketFacet<Player> implements PartialEntitySound
    {
        private static final Class<?> CLASS_CLIENTBOUND_CUSTOM_SOUND;
        private static final Class<?> CLASS_VEC3;
        private static final MethodHandle NEW_CLIENTBOUND_ENTITY_SOUND;
        private static final MethodHandle NEW_CLIENTBOUND_CUSTOM_SOUND;
        private static final MethodHandle NEW_VEC3;
        private static final MethodHandle NEW_RESOURCE_LOCATION;
        private static final MethodHandle REGISTRY_GET_OPTIONAL;
        private static final Object REGISTRY_SOUND_EVENT;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && EntitySound.NEW_CLIENTBOUND_ENTITY_SOUND != null && EntitySound.NEW_RESOURCE_LOCATION != null && EntitySound.REGISTRY_SOUND_EVENT != null && EntitySound.REGISTRY_GET_OPTIONAL != null && CraftBukkitFacet.CRAFT_ENTITY_GET_HANDLE != null && CraftBukkitAccess.EntitySound.isSupported();
        }
        
        @Override
        public Object createForEntity(final cc.dreamcode.kowal.libs.net.kyori.adventure.sound.Sound sound, final Entity entity) {
            try {
                final Object nmsEntity = this.toNativeEntity(entity);
                if (nmsEntity == null) {
                    return null;
                }
                final Object soundCategory = this.toVanilla(sound.source());
                if (soundCategory == null) {
                    return null;
                }
                final Object nameRl = EntitySound.NEW_RESOURCE_LOCATION.invoke(sound.name().namespace(), sound.name().value());
                final Optional<?> event = (Optional<?>)EntitySound.REGISTRY_GET_OPTIONAL.invoke(EntitySound.REGISTRY_SOUND_EVENT, nameRl);
                final long seed = sound.seed().orElseGet(() -> ThreadLocalRandom.current().nextLong());
                if (event.isPresent()) {
                    return EntitySound.NEW_CLIENTBOUND_ENTITY_SOUND.invoke(event.get(), soundCategory, nmsEntity, sound.volume(), sound.pitch(), seed);
                }
                if (EntitySound.NEW_CLIENTBOUND_CUSTOM_SOUND != null && EntitySound.NEW_VEC3 != null) {
                    final Location loc = entity.getLocation();
                    return EntitySound.NEW_CLIENTBOUND_CUSTOM_SOUND.invoke(nameRl, soundCategory, EntitySound.NEW_VEC3.invoke(loc.getX(), loc.getY(), loc.getZ()), sound.volume(), sound.pitch(), seed);
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to send sound tracking an entity", new Object[0]);
            }
            return null;
        }
        
        @Override
        public void playSound(@NotNull final Player viewer, final Object message) {
            this.sendPacket(viewer, message);
        }
        
        static {
            CLASS_CLIENTBOUND_CUSTOM_SOUND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutCustomSoundEffect"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundCustomSoundPacket"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutCustomSoundEffect"));
            CLASS_VEC3 = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("Vec3D"), MinecraftReflection.findMcClassName("world.phys.Vec3D"), MinecraftReflection.findMcClassName("world.phys.Vec3"));
            NEW_VEC3 = MinecraftReflection.findConstructor(EntitySound.CLASS_VEC3, Double.TYPE, Double.TYPE, Double.TYPE);
            NEW_RESOURCE_LOCATION = MinecraftReflection.findConstructor(CraftBukkitAccess.CLASS_RESOURCE_LOCATION, String.class, String.class);
            REGISTRY_GET_OPTIONAL = MinecraftReflection.searchMethod(CraftBukkitAccess.CLASS_REGISTRY, 1, "getOptional", Optional.class, CraftBukkitAccess.CLASS_RESOURCE_LOCATION);
            MethodHandle entitySoundPacketConstructor = MinecraftReflection.findConstructor(CraftBukkitAccess.EntitySound.CLASS_CLIENTBOUND_ENTITY_SOUND, CraftBukkitAccess.EntitySound.CLASS_SOUND_EVENT, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, CraftBukkitFacet.CLASS_NMS_ENTITY, Float.TYPE, Float.TYPE, Long.TYPE);
            if (entitySoundPacketConstructor == null) {
                entitySoundPacketConstructor = MinecraftReflection.findConstructor(CraftBukkitAccess.EntitySound.CLASS_CLIENTBOUND_ENTITY_SOUND, CraftBukkitAccess.EntitySound.CLASS_SOUND_EVENT, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, CraftBukkitFacet.CLASS_NMS_ENTITY, Float.TYPE, Float.TYPE);
                if (entitySoundPacketConstructor != null) {
                    entitySoundPacketConstructor = MethodHandles.dropArguments(entitySoundPacketConstructor, 5, new Class[] { Long.TYPE });
                }
            }
            NEW_CLIENTBOUND_ENTITY_SOUND = entitySoundPacketConstructor;
            MethodHandle customSoundPacketConstructor = MinecraftReflection.findConstructor(EntitySound.CLASS_CLIENTBOUND_CUSTOM_SOUND, CraftBukkitAccess.CLASS_RESOURCE_LOCATION, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, EntitySound.CLASS_VEC3, Float.TYPE, Float.TYPE, Long.TYPE);
            if (customSoundPacketConstructor == null) {
                customSoundPacketConstructor = MinecraftReflection.findConstructor(EntitySound.CLASS_CLIENTBOUND_CUSTOM_SOUND, CraftBukkitAccess.CLASS_RESOURCE_LOCATION, CraftBukkitAccess.EntitySound.CLASS_SOUND_SOURCE, EntitySound.CLASS_VEC3, Float.TYPE, Float.TYPE);
                if (customSoundPacketConstructor != null) {
                    customSoundPacketConstructor = MethodHandles.dropArguments(customSoundPacketConstructor, 5, new Class[] { Long.TYPE });
                }
            }
            NEW_CLIENTBOUND_CUSTOM_SOUND = customSoundPacketConstructor;
            Object registrySoundEvent = null;
            if (CraftBukkitAccess.CLASS_REGISTRY != null) {
                try {
                    final Field soundEventField = MinecraftReflection.findField(CraftBukkitAccess.CLASS_REGISTRY, "SOUND_EVENT");
                    if (soundEventField != null) {
                        registrySoundEvent = soundEventField.get((Object)null);
                    }
                    else {
                        Object rootRegistry = null;
                        for (final Field field : CraftBukkitAccess.CLASS_REGISTRY.getDeclaredFields()) {
                            final int mask = 28;
                            if ((field.getModifiers() & 0x1C) == 0x1C && field.getType().equals(CraftBukkitAccess.CLASS_WRITABLE_REGISTRY)) {
                                field.setAccessible(true);
                                rootRegistry = field.get((Object)null);
                                break;
                            }
                        }
                        if (rootRegistry != null) {
                            registrySoundEvent = EntitySound.REGISTRY_GET_OPTIONAL.invoke(rootRegistry, EntitySound.NEW_RESOURCE_LOCATION.invoke("minecraft", "sound_event")).orElse((Object)null);
                        }
                    }
                }
                catch (final Throwable thr) {
                    Knob.logError(thr, "Failed to initialize EntitySound CraftBukkit facet", new Object[0]);
                }
            }
            REGISTRY_SOUND_EVENT = registrySoundEvent;
        }
    }
    
    static class Title_1_17 extends PacketFacet<Player> implements Facet.Title<Player, Object, List<Object>, List<?>>
    {
        private static final Class<?> PACKET_SET_TITLE;
        private static final Class<?> PACKET_SET_SUBTITLE;
        private static final Class<?> PACKET_SET_TITLE_ANIMATION;
        private static final Class<?> PACKET_CLEAR_TITLES;
        private static final MethodHandle CONSTRUCTOR_SET_TITLE;
        private static final MethodHandle CONSTRUCTOR_SET_SUBTITLE;
        private static final MethodHandle CONSTRUCTOR_SET_TITLE_ANIMATION;
        private static final MethodHandle CONSTRUCTOR_CLEAR_TITLES;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Title_1_17.CONSTRUCTOR_SET_TITLE != null && Title_1_17.CONSTRUCTOR_SET_SUBTITLE != null && Title_1_17.CONSTRUCTOR_SET_TITLE_ANIMATION != null && Title_1_17.CONSTRUCTOR_CLEAR_TITLES != null;
        }
        
        @NotNull
        @Override
        public List<Object> createTitleCollection() {
            return (List<Object>)new ArrayList();
        }
        
        @Override
        public void contributeTitle(@NotNull final List<Object> coll, @NotNull final Object title) {
            try {
                coll.add(Title_1_17.CONSTRUCTOR_SET_TITLE.invoke(title));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke title packet constructor", new Object[0]);
            }
        }
        
        @Override
        public void contributeSubtitle(@NotNull final List<Object> coll, @NotNull final Object subtitle) {
            try {
                coll.add(Title_1_17.CONSTRUCTOR_SET_SUBTITLE.invoke(subtitle));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke subtitle packet constructor", new Object[0]);
            }
        }
        
        @Override
        public void contributeTimes(@NotNull final List<Object> coll, final int inTicks, final int stayTicks, final int outTicks) {
            try {
                coll.add(Title_1_17.CONSTRUCTOR_SET_TITLE_ANIMATION.invoke(inTicks, stayTicks, outTicks));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke title animations packet constructor", new Object[0]);
            }
        }
        
        @Nullable
        @Override
        public List<?> completeTitle(@NotNull final List<Object> coll) {
            return coll;
        }
        
        @Override
        public void showTitle(@NotNull final Player viewer, @NotNull final List<?> packets) {
            for (final Object packet : packets) {
                this.sendMessage(viewer, packet);
            }
        }
        
        @Override
        public void clearTitle(@NotNull final Player viewer) {
            try {
                if (Title_1_17.CONSTRUCTOR_CLEAR_TITLES != null) {
                    this.sendPacket(viewer, Title_1_17.CONSTRUCTOR_CLEAR_TITLES.invoke(false));
                }
                else {
                    viewer.sendTitle("", "", -1, -1, -1);
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to clear title", new Object[0]);
            }
        }
        
        @Override
        public void resetTitle(@NotNull final Player viewer) {
            try {
                if (Title_1_17.CONSTRUCTOR_CLEAR_TITLES != null) {
                    this.sendPacket(viewer, Title_1_17.CONSTRUCTOR_CLEAR_TITLES.invoke(true));
                }
                else {
                    viewer.resetTitle();
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to clear title", new Object[0]);
            }
        }
        
        static {
            PACKET_SET_TITLE = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetTitleTextPacket");
            PACKET_SET_SUBTITLE = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetSubtitleTextPacket");
            PACKET_SET_TITLE_ANIMATION = MinecraftReflection.findMcClass("network.protocol.game.ClientboundSetTitlesAnimationPacket");
            PACKET_CLEAR_TITLES = MinecraftReflection.findMcClass("network.protocol.game.ClientboundClearTitlesPacket");
            CONSTRUCTOR_SET_TITLE = MinecraftReflection.findConstructor(Title_1_17.PACKET_SET_TITLE, CraftBukkitFacet.CLASS_CHAT_COMPONENT);
            CONSTRUCTOR_SET_SUBTITLE = MinecraftReflection.findConstructor(Title_1_17.PACKET_SET_SUBTITLE, CraftBukkitFacet.CLASS_CHAT_COMPONENT);
            CONSTRUCTOR_SET_TITLE_ANIMATION = MinecraftReflection.findConstructor(Title_1_17.PACKET_SET_TITLE_ANIMATION, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            CONSTRUCTOR_CLEAR_TITLES = MinecraftReflection.findConstructor(Title_1_17.PACKET_CLEAR_TITLES, Boolean.TYPE);
        }
    }
    
    static class Title extends PacketFacet<Player> implements Facet.Title<Player, Object, List<Object>, List<?>>
    {
        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitFacet.CONSTRUCTOR_TITLE_MESSAGE != null && CraftBukkitFacet.CONSTRUCTOR_TITLE_TIMES != null;
        }
        
        @NotNull
        @Override
        public List<Object> createTitleCollection() {
            return (List<Object>)new ArrayList();
        }
        
        @Override
        public void contributeTitle(@NotNull final List<Object> coll, @NotNull final Object title) {
            try {
                coll.add(CraftBukkitFacet.CONSTRUCTOR_TITLE_MESSAGE.invoke(CraftBukkitFacet.TITLE_ACTION_TITLE, title));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke title packet constructor", new Object[0]);
            }
        }
        
        @Override
        public void contributeSubtitle(@NotNull final List<Object> coll, @NotNull final Object subtitle) {
            try {
                coll.add(CraftBukkitFacet.CONSTRUCTOR_TITLE_MESSAGE.invoke(CraftBukkitFacet.TITLE_ACTION_SUBTITLE, subtitle));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke subtitle packet constructor", new Object[0]);
            }
        }
        
        @Override
        public void contributeTimes(@NotNull final List<Object> coll, final int inTicks, final int stayTicks, final int outTicks) {
            try {
                coll.add(CraftBukkitFacet.CONSTRUCTOR_TITLE_TIMES.invoke(inTicks, stayTicks, outTicks));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to invoke title animations packet constructor", new Object[0]);
            }
        }
        
        @Nullable
        @Override
        public List<?> completeTitle(@NotNull final List<Object> coll) {
            return coll;
        }
        
        @Override
        public void showTitle(@NotNull final Player viewer, @NotNull final List<?> packets) {
            for (final Object packet : packets) {
                this.sendMessage(viewer, packet);
            }
        }
        
        @Override
        public void clearTitle(@NotNull final Player viewer) {
            try {
                if (CraftBukkitFacet.TITLE_ACTION_CLEAR != null) {
                    this.sendPacket(viewer, CraftBukkitFacet.CONSTRUCTOR_TITLE_MESSAGE.invoke(CraftBukkitFacet.TITLE_ACTION_CLEAR, (Void)null));
                }
                else {
                    viewer.sendTitle("", "", -1, -1, -1);
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to clear title", new Object[0]);
            }
        }
        
        @Override
        public void resetTitle(@NotNull final Player viewer) {
            try {
                if (CraftBukkitFacet.TITLE_ACTION_RESET != null) {
                    this.sendPacket(viewer, CraftBukkitFacet.CONSTRUCTOR_TITLE_MESSAGE.invoke(CraftBukkitFacet.TITLE_ACTION_RESET, (Void)null));
                }
                else {
                    viewer.resetTitle();
                }
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to clear title", new Object[0]);
            }
        }
    }
    
    static final class Book_1_20_5 extends PacketFacet<Player> implements Facet.Book<Player, Object, ItemStack>
    {
        @Override
        public boolean isSupported() {
            return super.isSupported() && CraftBukkitAccess.Book_1_20_5.isSupported();
        }
        
        @Nullable
        @Override
        public ItemStack createBook(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<Object> pages) {
            try {
                final ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                final List<Object> pageList = (List<Object>)new ArrayList();
                for (final Object page : pages) {
                    pageList.add(CraftBukkitAccess.Book_1_20_5.CREATE_FILTERABLE.invoke(page));
                }
                final Object bookContent = CraftBukkitAccess.Book_1_20_5.NEW_BOOK_CONTENT.invoke(CraftBukkitAccess.Book_1_20_5.CREATE_FILTERABLE.invoke(title), author, 0, (List)pageList, true);
                final Object stack = CraftBukkitAccess.Book_1_20_5.CRAFT_ITEMSTACK_NMS_COPY.invoke(item);
                CraftBukkitAccess.Book_1_20_5.MC_ITEMSTACK_SET.invoke(stack, CraftBukkitAccess.Book_1_20_5.WRITTEN_BOOK_COMPONENT_TYPE, bookContent);
                return CraftBukkitAccess.Book_1_20_5.CRAFT_ITEMSTACK_CRAFT_MIRROR.invoke(stack);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to apply written_book_content component to ItemStack", new Object[0]);
                return null;
            }
        }
        
        @Override
        public void openBook(@NotNull final Player viewer, @NotNull final ItemStack book) {
            final PlayerInventory inventory = viewer.getInventory();
            final ItemStack current = inventory.getItemInHand();
            try {
                inventory.setItemInHand(book);
                this.sendMessage(viewer, CraftBukkitAccess.Book_1_20_5.NEW_PACKET_OPEN_BOOK.invoke(CraftBukkitAccess.Book_1_20_5.HAND_MAIN));
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to send openBook packet: %s", book);
            }
            finally {
                inventory.setItemInHand(current);
            }
        }
    }
    
    protected abstract static class AbstractBook extends PacketFacet<Player> implements Facet.Book<Player, Object, ItemStack>
    {
        protected static final int HAND_MAIN = 0;
        private static final Material BOOK_TYPE;
        private static final ItemStack BOOK_STACK;
        private static final String BOOK_TITLE = "title";
        private static final String BOOK_AUTHOR = "author";
        private static final String BOOK_PAGES = "pages";
        private static final String BOOK_RESOLVED = "resolved";
        private static final Class<?> CLASS_NBT_TAG_COMPOUND;
        private static final Class<?> CLASS_NBT_IO;
        private static final MethodHandle NBT_IO_DESERIALIZE;
        private static final Class<?> CLASS_CRAFT_ITEMSTACK;
        private static final Class<?> CLASS_MC_ITEMSTACK;
        private static final MethodHandle MC_ITEMSTACK_SET_TAG;
        private static final MethodHandle CRAFT_ITEMSTACK_NMS_COPY;
        private static final MethodHandle CRAFT_ITEMSTACK_CRAFT_MIRROR;
        
        protected abstract void sendOpenPacket(@NotNull final Player viewer) throws Throwable;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && AbstractBook.NBT_IO_DESERIALIZE != null && AbstractBook.MC_ITEMSTACK_SET_TAG != null && AbstractBook.CRAFT_ITEMSTACK_CRAFT_MIRROR != null && AbstractBook.CRAFT_ITEMSTACK_NMS_COPY != null && AbstractBook.BOOK_STACK != null;
        }
        
        @NotNull
        @Override
        public String createMessage(@NotNull final Player viewer, @NotNull final Component message) {
            return ((ComponentSerializer<Component, O, String>)BukkitComponentSerializer.gson()).serialize(message);
        }
        
        @NotNull
        @Override
        public ItemStack createBook(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<Object> pages) {
            return this.applyTag(AbstractBook.BOOK_STACK, tagFor(title, author, pages));
        }
        
        @Deprecated
        @Override
        public void openBook(@NotNull final Player viewer, @NotNull final ItemStack book) {
            final PlayerInventory inventory = viewer.getInventory();
            final ItemStack current = inventory.getItemInHand();
            try {
                inventory.setItemInHand(book);
                this.sendOpenPacket(viewer);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to send openBook packet: %s", book);
            }
            finally {
                inventory.setItemInHand(current);
            }
        }
        
        private static CompoundBinaryTag tagFor(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<Object> pages) {
            final ListBinaryTag.Builder<StringBinaryTag> builder = ListBinaryTag.builder(BinaryTagTypes.STRING);
            for (final Object page : pages) {
                builder.add(StringBinaryTag.of((String)page));
            }
            return CompoundBinaryTag.builder().putString("title", title).putString("author", author).put("pages", builder.build()).putByte("resolved", (byte)1).build();
        }
        
        @NotNull
        private Object createTag(@NotNull final CompoundBinaryTag tag) throws IOException {
            final TrustedByteArrayOutputStream output = new TrustedByteArrayOutputStream();
            BinaryTagIO.writer().write(tag, (OutputStream)output);
            try (final DataInputStream dis = new DataInputStream(output.toInputStream())) {
                return AbstractBook.NBT_IO_DESERIALIZE.invoke(dis);
            }
            catch (final Throwable err) {
                throw new IOException(err);
            }
        }
        
        private ItemStack applyTag(@NotNull final ItemStack input, final CompoundBinaryTag binTag) {
            if (AbstractBook.CRAFT_ITEMSTACK_NMS_COPY == null || AbstractBook.MC_ITEMSTACK_SET_TAG == null || AbstractBook.CRAFT_ITEMSTACK_CRAFT_MIRROR == null) {
                return input;
            }
            try {
                final Object stack = AbstractBook.CRAFT_ITEMSTACK_NMS_COPY.invoke(input);
                final Object tag = this.createTag(binTag);
                AbstractBook.MC_ITEMSTACK_SET_TAG.invoke(stack, tag);
                return AbstractBook.CRAFT_ITEMSTACK_CRAFT_MIRROR.invoke(stack);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to apply NBT tag to ItemStack: %s %s", input, binTag);
                return input;
            }
        }
        
        static {
            BOOK_TYPE = (Material)MinecraftReflection.findEnum(Material.class, "WRITTEN_BOOK");
            BOOK_STACK = ((AbstractBook.BOOK_TYPE == null) ? null : new ItemStack(AbstractBook.BOOK_TYPE));
            CLASS_NBT_TAG_COMPOUND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("NBTTagCompound"), MinecraftReflection.findMcClassName("nbt.CompoundTag"), MinecraftReflection.findMcClassName("nbt.NBTTagCompound"));
            CLASS_NBT_IO = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("NBTCompressedStreamTools"), MinecraftReflection.findMcClassName("nbt.NbtIo"), MinecraftReflection.findMcClassName("nbt.NBTCompressedStreamTools"));
            MethodHandle nbtIoDeserialize = null;
            if (AbstractBook.CLASS_NBT_IO != null) {
                for (final Method method : AbstractBook.CLASS_NBT_IO.getDeclaredMethods()) {
                    Label_0223: {
                        if (Modifier.isStatic(method.getModifiers()) && method.getReturnType().equals(AbstractBook.CLASS_NBT_TAG_COMPOUND) && method.getParameterCount() == 1) {
                            final Class<?> firstParam = method.getParameterTypes()[0];
                            if (!firstParam.equals(DataInputStream.class)) {
                                if (!firstParam.equals(DataInput.class)) {
                                    break Label_0223;
                                }
                            }
                            try {
                                nbtIoDeserialize = MinecraftReflection.lookup().unreflect(method);
                            }
                            catch (final IllegalAccessException ex) {}
                            break;
                        }
                    }
                }
            }
            NBT_IO_DESERIALIZE = nbtIoDeserialize;
            CLASS_CRAFT_ITEMSTACK = MinecraftReflection.findCraftClass("inventory.CraftItemStack");
            CLASS_MC_ITEMSTACK = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("ItemStack"), MinecraftReflection.findMcClassName("world.item.ItemStack"));
            MC_ITEMSTACK_SET_TAG = MinecraftReflection.searchMethod(AbstractBook.CLASS_MC_ITEMSTACK, 1, "setTag", Void.TYPE, AbstractBook.CLASS_NBT_TAG_COMPOUND);
            CRAFT_ITEMSTACK_NMS_COPY = MinecraftReflection.findStaticMethod(AbstractBook.CLASS_CRAFT_ITEMSTACK, "asNMSCopy", AbstractBook.CLASS_MC_ITEMSTACK, ItemStack.class);
            CRAFT_ITEMSTACK_CRAFT_MIRROR = MinecraftReflection.findStaticMethod(AbstractBook.CLASS_CRAFT_ITEMSTACK, "asCraftMirror", AbstractBook.CLASS_CRAFT_ITEMSTACK, AbstractBook.CLASS_MC_ITEMSTACK);
        }
        
        private static final class TrustedByteArrayOutputStream extends ByteArrayOutputStream
        {
            public InputStream toInputStream() {
                return (InputStream)new ByteArrayInputStream(this.buf, 0, this.count);
            }
        }
    }
    
    static final class BookPost1_13 extends AbstractBook
    {
        private static final Class<?> CLASS_ENUM_HAND;
        private static final Object HAND_MAIN;
        private static final Class<?> PACKET_OPEN_BOOK;
        private static final MethodHandle NEW_PACKET_OPEN_BOOK;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && BookPost1_13.HAND_MAIN != null && BookPost1_13.NEW_PACKET_OPEN_BOOK != null;
        }
        
        @Override
        protected void sendOpenPacket(@NotNull final Player viewer) throws Throwable {
            this.sendMessage(viewer, BookPost1_13.NEW_PACKET_OPEN_BOOK.invoke(BookPost1_13.HAND_MAIN));
        }
        
        static {
            CLASS_ENUM_HAND = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("EnumHand"), MinecraftReflection.findMcClassName("world.EnumHand"), MinecraftReflection.findMcClassName("world.InteractionHand"));
            HAND_MAIN = MinecraftReflection.findEnum(BookPost1_13.CLASS_ENUM_HAND, "MAIN_HAND", 0);
            PACKET_OPEN_BOOK = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutOpenBook"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutOpenBook"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundOpenBookPacket"));
            NEW_PACKET_OPEN_BOOK = MinecraftReflection.findConstructor(BookPost1_13.PACKET_OPEN_BOOK, BookPost1_13.CLASS_ENUM_HAND);
        }
    }
    
    static final class Book1_13 extends AbstractBook
    {
        private static final Class<?> CLASS_BYTE_BUF;
        private static final Class<?> CLASS_PACKET_CUSTOM_PAYLOAD;
        private static final Class<?> CLASS_FRIENDLY_BYTE_BUF;
        private static final Class<?> CLASS_RESOURCE_LOCATION;
        private static final Object PACKET_TYPE_BOOK_OPEN;
        private static final MethodHandle NEW_PACKET_CUSTOM_PAYLOAD;
        private static final MethodHandle NEW_FRIENDLY_BYTE_BUF;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Book1_13.CLASS_BYTE_BUF != null && Book1_13.NEW_PACKET_CUSTOM_PAYLOAD != null && Book1_13.PACKET_TYPE_BOOK_OPEN != null;
        }
        
        @Override
        protected void sendOpenPacket(@NotNull final Player viewer) throws Throwable {
            final ByteBuf data = Unpooled.buffer();
            data.writeByte(0);
            final Object packetByteBuf = Book1_13.NEW_FRIENDLY_BYTE_BUF.invoke(data);
            this.sendMessage(viewer, Book1_13.NEW_PACKET_CUSTOM_PAYLOAD.invoke(Book1_13.PACKET_TYPE_BOOK_OPEN, packetByteBuf));
        }
        
        static {
            CLASS_BYTE_BUF = MinecraftReflection.findClass("io.netty.buffer.ByteBuf");
            CLASS_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findNmsClass("PacketPlayOutCustomPayload");
            CLASS_FRIENDLY_BYTE_BUF = MinecraftReflection.findNmsClass("PacketDataSerializer");
            CLASS_RESOURCE_LOCATION = MinecraftReflection.findNmsClass("MinecraftKey");
            NEW_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findConstructor(Book1_13.CLASS_PACKET_CUSTOM_PAYLOAD, Book1_13.CLASS_RESOURCE_LOCATION, Book1_13.CLASS_FRIENDLY_BYTE_BUF);
            NEW_FRIENDLY_BYTE_BUF = MinecraftReflection.findConstructor(Book1_13.CLASS_FRIENDLY_BYTE_BUF, Book1_13.CLASS_BYTE_BUF);
            Object packetType = null;
            if (Book1_13.CLASS_RESOURCE_LOCATION != null) {
                try {
                    packetType = Book1_13.CLASS_RESOURCE_LOCATION.getConstructor(String.class).newInstance(new Object[] { "minecraft:book_open" });
                }
                catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {}
            }
            PACKET_TYPE_BOOK_OPEN = packetType;
        }
    }
    
    static final class BookPre1_13 extends AbstractBook
    {
        private static final String PACKET_TYPE_BOOK_OPEN = "MC|BOpen";
        private static final Class<?> CLASS_BYTE_BUF;
        private static final Class<?> CLASS_PACKET_CUSTOM_PAYLOAD;
        private static final Class<?> CLASS_PACKET_DATA_SERIALIZER;
        private static final MethodHandle NEW_PACKET_CUSTOM_PAYLOAD;
        private static final MethodHandle NEW_PACKET_BYTE_BUF;
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && BookPre1_13.CLASS_BYTE_BUF != null && BookPre1_13.CLASS_PACKET_CUSTOM_PAYLOAD != null && BookPre1_13.NEW_PACKET_CUSTOM_PAYLOAD != null;
        }
        
        @Override
        protected void sendOpenPacket(@NotNull final Player viewer) throws Throwable {
            final ByteBuf data = Unpooled.buffer();
            data.writeByte(0);
            final Object packetByteBuf = BookPre1_13.NEW_PACKET_BYTE_BUF.invoke(data);
            this.sendMessage(viewer, BookPre1_13.NEW_PACKET_CUSTOM_PAYLOAD.invoke("MC|BOpen", packetByteBuf));
        }
        
        static {
            CLASS_BYTE_BUF = MinecraftReflection.findClass("io.netty.buffer.ByteBuf");
            CLASS_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findNmsClass("PacketPlayOutCustomPayload");
            CLASS_PACKET_DATA_SERIALIZER = MinecraftReflection.findNmsClass("PacketDataSerializer");
            NEW_PACKET_CUSTOM_PAYLOAD = MinecraftReflection.findConstructor(BookPre1_13.CLASS_PACKET_CUSTOM_PAYLOAD, String.class, BookPre1_13.CLASS_PACKET_DATA_SERIALIZER);
            NEW_PACKET_BYTE_BUF = MinecraftReflection.findConstructor(BookPre1_13.CLASS_PACKET_DATA_SERIALIZER, BookPre1_13.CLASS_BYTE_BUF);
        }
    }
    
    static final class BossBar extends BukkitFacet.BossBar
    {
        private static final Class<?> CLASS_CRAFT_BOSS_BAR;
        private static final Class<?> CLASS_BOSS_BAR_ACTION;
        private static final Object BOSS_BAR_ACTION_TITLE;
        private static final MethodHandle CRAFT_BOSS_BAR_HANDLE;
        private static final MethodHandle NMS_BOSS_BATTLE_SET_NAME;
        private static final MethodHandle NMS_BOSS_BATTLE_SEND_UPDATE;
        
        private BossBar(@NotNull final Collection<Player> viewers) {
            super(viewers);
        }
        
        @Override
        public void bossBarNameChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
            try {
                final Object handle = BossBar.CRAFT_BOSS_BAR_HANDLE.invoke(this.bar);
                final Object text = MinecraftComponentSerializer.get().serialize(newName);
                BossBar.NMS_BOSS_BATTLE_SET_NAME.invoke(handle, text);
                BossBar.NMS_BOSS_BATTLE_SEND_UPDATE.invoke(handle, BossBar.BOSS_BAR_ACTION_TITLE);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to set CraftBossBar name: %s %s", this.bar, newName);
                super.bossBarNameChanged(bar, oldName, newName);
            }
        }
        
        static {
            CLASS_CRAFT_BOSS_BAR = MinecraftReflection.findCraftClass("boss.CraftBossBar");
            Class<?> classBossBarAction = null;
            Object bossBarActionTitle = null;
            classBossBarAction = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutBoss$Action"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutBoss$Action"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundBossEventPacket$Operation"));
            if (classBossBarAction == null || !classBossBarAction.isEnum()) {
                classBossBarAction = null;
                final Class<?> packetClass = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutBoss"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutBoss"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundBossEventPacket"));
                final Class<?> bossEventClass = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("BossBattle"), MinecraftReflection.findMcClassName("world.BossBattle"), MinecraftReflection.findMcClassName("world.BossEvent"));
                if (packetClass != null && bossEventClass != null) {
                    try {
                        final MethodType methodType = MethodType.methodType((Class)packetClass, (Class)bossEventClass);
                        String methodName;
                        try {
                            packetClass.getDeclaredMethod("createUpdateNamePacket", bossEventClass);
                            methodName = "createUpdateNamePacket";
                        }
                        catch (final NoSuchMethodException ignored) {
                            methodName = "c";
                        }
                        final MethodHandle factoryMethod = MinecraftReflection.lookup().findStatic((Class)packetClass, methodName, methodType);
                        bossBarActionTitle = LambdaMetafactory.metafactory(MinecraftReflection.lookup(), "apply", MethodType.methodType((Class)Function.class), methodType.generic(), factoryMethod, methodType).getTarget().invoke();
                        classBossBarAction = Function.class;
                    }
                    catch (final Throwable error) {
                        Knob.logError(error, "Failed to initialize CraftBossBar constructor", new Object[0]);
                    }
                }
            }
            else {
                bossBarActionTitle = MinecraftReflection.findEnum(classBossBarAction, "UPDATE_NAME", 3);
            }
            CLASS_BOSS_BAR_ACTION = classBossBarAction;
            BOSS_BAR_ACTION_TITLE = bossBarActionTitle;
            MethodHandle craftBossBarHandle = null;
            MethodHandle nmsBossBattleSetName = null;
            MethodHandle nmsBossBattleSendUpdate = null;
            if (BossBar.CLASS_CRAFT_BOSS_BAR != null && CraftBukkitFacet.CLASS_CHAT_COMPONENT != null && BossBar.BOSS_BAR_ACTION_TITLE != null) {
                try {
                    final Field craftBossBarHandleField = MinecraftReflection.needField(BossBar.CLASS_CRAFT_BOSS_BAR, "handle");
                    craftBossBarHandle = MinecraftReflection.lookup().unreflectGetter(craftBossBarHandleField);
                    final Class<?> nmsBossBattleType = craftBossBarHandleField.getType();
                    for (final Field field : nmsBossBattleType.getFields()) {
                        if (field.getType().equals(CraftBukkitFacet.CLASS_CHAT_COMPONENT)) {
                            nmsBossBattleSetName = MinecraftReflection.lookup().unreflectSetter(field);
                            break;
                        }
                    }
                    nmsBossBattleSendUpdate = MinecraftReflection.findMethod(nmsBossBattleType, new String[] { "sendUpdate", "a", "broadcast" }, Void.TYPE, BossBar.CLASS_BOSS_BAR_ACTION);
                }
                catch (final Throwable error2) {
                    Knob.logError(error2, "Failed to initialize CraftBossBar constructor", new Object[0]);
                }
            }
            CRAFT_BOSS_BAR_HANDLE = craftBossBarHandle;
            NMS_BOSS_BATTLE_SET_NAME = nmsBossBattleSetName;
            NMS_BOSS_BATTLE_SEND_UPDATE = nmsBossBattleSendUpdate;
        }
        
        public static class Builder extends CraftBukkitFacet<Player> implements Facet.BossBar.Builder<Player, CraftBukkitFacet.BossBar>
        {
            protected Builder() {
                super((Class<? extends CommandSender>)Player.class);
            }
            
            @Override
            public boolean isSupported() {
                return super.isSupported() && CraftBukkitFacet.BossBar.CLASS_CRAFT_BOSS_BAR != null && CraftBukkitFacet.BossBar.CRAFT_BOSS_BAR_HANDLE != null && CraftBukkitFacet.BossBar.NMS_BOSS_BATTLE_SET_NAME != null && CraftBukkitFacet.BossBar.NMS_BOSS_BATTLE_SEND_UPDATE != null;
            }
            
            @Override
            public CraftBukkitFacet.BossBar createBossBar(@NotNull final Collection<Player> viewers) {
                return new CraftBukkitFacet.BossBar((Collection)viewers);
            }
        }
    }
    
    static class FakeEntity<E extends Entity> extends PacketFacet<Player> implements Facet.FakeEntity<Player, Location>, Listener
    {
        private static final Class<? extends World> CLASS_CRAFT_WORLD;
        private static final Class<?> CLASS_NMS_LIVING_ENTITY;
        private static final Class<?> CLASS_DATA_WATCHER;
        private static final MethodHandle CRAFT_WORLD_CREATE_ENTITY;
        private static final MethodHandle NMS_ENTITY_GET_BUKKIT_ENTITY;
        private static final MethodHandle NMS_ENTITY_GET_DATA_WATCHER;
        private static final MethodHandle NMS_ENTITY_SET_LOCATION;
        private static final MethodHandle NMS_ENTITY_SET_INVISIBLE;
        private static final MethodHandle DATA_WATCHER_WATCH;
        private static final Class<?> CLASS_SPAWN_LIVING_PACKET;
        private static final MethodHandle NEW_SPAWN_LIVING_PACKET;
        private static final Class<?> CLASS_ENTITY_DESTROY_PACKET;
        private static final MethodHandle NEW_ENTITY_DESTROY_PACKET;
        private static final Class<?> CLASS_ENTITY_METADATA_PACKET;
        private static final MethodHandle NEW_ENTITY_METADATA_PACKET;
        private static final Class<?> CLASS_ENTITY_TELEPORT_PACKET;
        private static final MethodHandle NEW_ENTITY_TELEPORT_PACKET;
        private static final Class<?> CLASS_ENTITY_WITHER;
        private static final Class<?> CLASS_WORLD;
        private static final Class<?> CLASS_WORLD_SERVER;
        private static final MethodHandle CRAFT_WORLD_GET_HANDLE;
        private static final MethodHandle NEW_ENTITY_WITHER;
        private static final boolean SUPPORTED;
        private final E entity;
        private final Object entityHandle;
        protected final Set<Player> viewers;
        
        protected FakeEntity(@NotNull final Class<E> entityClass, @NotNull final Location location) {
            this((Plugin)BukkitAudience.PLUGIN.get(), entityClass, location);
        }
        
        protected FakeEntity(@NotNull final Plugin plugin, @NotNull final Class<E> entityClass, @NotNull final Location location) {
            E entity = null;
            Object handle = null;
            if (FakeEntity.SUPPORTED) {
                try {
                    if (FakeEntity.CRAFT_WORLD_CREATE_ENTITY != null) {
                        final Object nmsEntity = FakeEntity.CRAFT_WORLD_CREATE_ENTITY.invoke(location.getWorld(), location, (Class)entityClass);
                        entity = (E)FakeEntity.NMS_ENTITY_GET_BUKKIT_ENTITY.invoke(nmsEntity);
                    }
                    else if (Wither.class.isAssignableFrom(entityClass) && FakeEntity.NEW_ENTITY_WITHER != null) {
                        final Object nmsEntity = FakeEntity.NEW_ENTITY_WITHER.invoke(FakeEntity.CRAFT_WORLD_GET_HANDLE.invoke(location.getWorld()));
                        entity = (E)FakeEntity.NMS_ENTITY_GET_BUKKIT_ENTITY.invoke(nmsEntity);
                    }
                    if (CraftBukkitFacet.CLASS_CRAFT_ENTITY.isInstance(entity)) {
                        handle = CraftBukkitFacet.CRAFT_ENTITY_GET_HANDLE.invoke((Entity)entity);
                    }
                }
                catch (final Throwable error) {
                    Knob.logError(error, "Failed to create fake entity: %s", entityClass.getSimpleName());
                }
            }
            this.entity = entity;
            this.entityHandle = handle;
            this.viewers = (Set<Player>)new HashSet();
            if (this.isSupported()) {
                plugin.getServer().getPluginManager().registerEvents((Listener)this, plugin);
            }
        }
        
        public boolean isSupported() {
            return super.isSupported() && this.entity != null && this.entityHandle != null;
        }
        
        @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
        public void onPlayerMove(final PlayerMoveEvent event) {
            final Player viewer = event.getPlayer();
            if (this.viewers.contains((Object)viewer)) {
                this.teleport(viewer, this.createPosition(viewer));
            }
        }
        
        @Nullable
        public Object createSpawnPacket() {
            if (this.entity instanceof LivingEntity) {
                try {
                    return FakeEntity.NEW_SPAWN_LIVING_PACKET.invoke(this.entityHandle);
                }
                catch (final Throwable error) {
                    Knob.logError(error, "Failed to create spawn packet: %s", this.entity);
                }
            }
            return null;
        }
        
        @Nullable
        public Object createDespawnPacket() {
            try {
                return FakeEntity.NEW_ENTITY_DESTROY_PACKET.invoke(this.entity.getEntityId());
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to create despawn packet: %s", this.entity);
                return null;
            }
        }
        
        @Nullable
        public Object createMetadataPacket() {
            try {
                final Object dataWatcher = FakeEntity.NMS_ENTITY_GET_DATA_WATCHER.invoke(this.entityHandle);
                return FakeEntity.NEW_ENTITY_METADATA_PACKET.invoke(this.entity.getEntityId(), dataWatcher, false);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to create update metadata packet: %s", this.entity);
                return null;
            }
        }
        
        @Nullable
        public Object createLocationPacket() {
            try {
                return FakeEntity.NEW_ENTITY_TELEPORT_PACKET.invoke(this.entityHandle);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to create teleport packet: %s", this.entity);
                return null;
            }
        }
        
        public void broadcastPacket(@Nullable final Object packet) {
            for (final Player viewer : this.viewers) {
                this.sendPacket(viewer, packet);
            }
        }
        
        @NotNull
        public Location createPosition(@NotNull final Player viewer) {
            return viewer.getLocation();
        }
        
        @NotNull
        public Location createPosition(final double x, final double y, final double z) {
            return new Location((World)null, x, y, z);
        }
        
        @Override
        public void teleport(@NotNull final Player viewer, @Nullable final Location position) {
            if (position == null) {
                this.viewers.remove((Object)viewer);
                this.sendPacket(viewer, this.createDespawnPacket());
                return;
            }
            if (!this.viewers.contains((Object)viewer)) {
                this.sendPacket(viewer, this.createSpawnPacket());
                this.viewers.add((Object)viewer);
            }
            try {
                FakeEntity.NMS_ENTITY_SET_LOCATION.invoke(this.entityHandle, position.getX(), position.getY(), position.getZ(), position.getPitch(), position.getYaw());
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to set entity location: %s %s", this.entity, position);
            }
            this.sendPacket(viewer, this.createLocationPacket());
        }
        
        @Override
        public void metadata(final int position, @NotNull final Object data) {
            if (FakeEntity.DATA_WATCHER_WATCH != null) {
                try {
                    final Object dataWatcher = FakeEntity.NMS_ENTITY_GET_DATA_WATCHER.invoke(this.entityHandle);
                    FakeEntity.DATA_WATCHER_WATCH.invoke(dataWatcher, position, data);
                }
                catch (final Throwable error) {
                    Knob.logError(error, "Failed to set entity metadata: %s %s=%s", this.entity, position, data);
                }
                this.broadcastPacket(this.createMetadataPacket());
            }
        }
        
        @Override
        public void invisible(final boolean invisible) {
            if (FakeEntity.NMS_ENTITY_SET_INVISIBLE != null) {
                try {
                    FakeEntity.NMS_ENTITY_SET_INVISIBLE.invoke(this.entityHandle, invisible);
                }
                catch (final Throwable error) {
                    Knob.logError(error, "Failed to change entity visibility: %s", this.entity);
                }
            }
        }
        
        @Deprecated
        @Override
        public void health(final float health) {
            if (this.entity instanceof Damageable) {
                final Damageable entity = (Damageable)this.entity;
                entity.setHealth(health * (entity.getMaxHealth() - 0.10000000149011612) + 0.10000000149011612);
                this.broadcastPacket(this.createMetadataPacket());
            }
        }
        
        @Override
        public void name(@NotNull final Component name) {
            this.entity.setCustomName(BukkitComponentSerializer.legacy().serialize(name));
            this.broadcastPacket(this.createMetadataPacket());
        }
        
        @Override
        public void close() {
            HandlerList.unregisterAll((Listener)this);
            for (final Player viewer : new LinkedList((Collection)this.viewers)) {
                this.teleport(viewer, null);
            }
        }
        
        static {
            CLASS_CRAFT_WORLD = MinecraftReflection.findCraftClass("CraftWorld", World.class);
            CLASS_NMS_LIVING_ENTITY = MinecraftReflection.findNmsClass("EntityLiving");
            CLASS_DATA_WATCHER = MinecraftReflection.findNmsClass("DataWatcher");
            CRAFT_WORLD_CREATE_ENTITY = MinecraftReflection.findMethod(FakeEntity.CLASS_CRAFT_WORLD, "createEntity", CraftBukkitFacet.CLASS_NMS_ENTITY, Location.class, Class.class);
            NMS_ENTITY_GET_BUKKIT_ENTITY = MinecraftReflection.findMethod(CraftBukkitFacet.CLASS_NMS_ENTITY, "getBukkitEntity", CraftBukkitFacet.CLASS_CRAFT_ENTITY, (Class<?>[])new Class[0]);
            NMS_ENTITY_GET_DATA_WATCHER = MinecraftReflection.findMethod(CraftBukkitFacet.CLASS_NMS_ENTITY, "getDataWatcher", FakeEntity.CLASS_DATA_WATCHER, (Class<?>[])new Class[0]);
            NMS_ENTITY_SET_LOCATION = MinecraftReflection.findMethod(CraftBukkitFacet.CLASS_NMS_ENTITY, "setLocation", Void.TYPE, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE, Float.TYPE);
            NMS_ENTITY_SET_INVISIBLE = MinecraftReflection.findMethod(CraftBukkitFacet.CLASS_NMS_ENTITY, "setInvisible", Void.TYPE, Boolean.TYPE);
            DATA_WATCHER_WATCH = MinecraftReflection.findMethod(FakeEntity.CLASS_DATA_WATCHER, "watch", Void.TYPE, Integer.TYPE, Object.class);
            CLASS_SPAWN_LIVING_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutSpawnEntityLiving");
            NEW_SPAWN_LIVING_PACKET = MinecraftReflection.findConstructor(FakeEntity.CLASS_SPAWN_LIVING_PACKET, FakeEntity.CLASS_NMS_LIVING_ENTITY);
            CLASS_ENTITY_DESTROY_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutEntityDestroy");
            NEW_ENTITY_DESTROY_PACKET = MinecraftReflection.findConstructor(FakeEntity.CLASS_ENTITY_DESTROY_PACKET, int[].class);
            CLASS_ENTITY_METADATA_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutEntityMetadata");
            NEW_ENTITY_METADATA_PACKET = MinecraftReflection.findConstructor(FakeEntity.CLASS_ENTITY_METADATA_PACKET, Integer.TYPE, FakeEntity.CLASS_DATA_WATCHER, Boolean.TYPE);
            CLASS_ENTITY_TELEPORT_PACKET = MinecraftReflection.findNmsClass("PacketPlayOutEntityTeleport");
            NEW_ENTITY_TELEPORT_PACKET = MinecraftReflection.findConstructor(FakeEntity.CLASS_ENTITY_TELEPORT_PACKET, CraftBukkitFacet.CLASS_NMS_ENTITY);
            CLASS_ENTITY_WITHER = MinecraftReflection.findNmsClass("EntityWither");
            CLASS_WORLD = MinecraftReflection.findNmsClass("World");
            CLASS_WORLD_SERVER = MinecraftReflection.findNmsClass("WorldServer");
            CRAFT_WORLD_GET_HANDLE = MinecraftReflection.findMethod(FakeEntity.CLASS_CRAFT_WORLD, "getHandle", FakeEntity.CLASS_WORLD_SERVER, (Class<?>[])new Class[0]);
            NEW_ENTITY_WITHER = MinecraftReflection.findConstructor(FakeEntity.CLASS_ENTITY_WITHER, FakeEntity.CLASS_WORLD);
            SUPPORTED = ((FakeEntity.CRAFT_WORLD_CREATE_ENTITY != null || (FakeEntity.NEW_ENTITY_WITHER != null && FakeEntity.CRAFT_WORLD_GET_HANDLE != null)) && CraftBukkitFacet.CRAFT_ENTITY_GET_HANDLE != null && FakeEntity.NMS_ENTITY_GET_BUKKIT_ENTITY != null && FakeEntity.NMS_ENTITY_GET_DATA_WATCHER != null);
        }
    }
    
    static final class BossBarWither extends CraftBukkitFacet.FakeEntity<Wither> implements Facet.BossBarEntity<Player, Location>
    {
        private volatile boolean initialized;
        
        private BossBarWither(@NotNull final Collection<Player> viewers) {
            super(Wither.class, ((Player)viewers.iterator().next()).getWorld().getSpawnLocation());
            this.initialized = false;
            this.invisible(true);
            this.metadata(20, 890);
        }
        
        public void bossBarInitialized(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar) {
            super.bossBarInitialized(bar);
            this.initialized = true;
        }
        
        @NotNull
        @Override
        public Location createPosition(@NotNull final Player viewer) {
            final Location position = super.createPosition(viewer);
            position.setPitch(position.getPitch() - 30.0f);
            position.setYaw(position.getYaw() + 0.0f);
            position.add(position.getDirection().multiply(40));
            return position;
        }
        
        public boolean isEmpty() {
            return !this.initialized || this.viewers.isEmpty();
        }
        
        public static class Builder extends CraftBukkitFacet<Player> implements Facet.BossBar.Builder<Player, BossBarWither>
        {
            protected Builder() {
                super((Class<? extends CommandSender>)Player.class);
            }
            
            @NotNull
            @Override
            public BossBarWither createBossBar(@NotNull final Collection<Player> viewers) {
                return new BossBarWither((Collection)viewers);
            }
        }
    }
    
    static class TabList extends PacketFacet<Player> implements Facet.TabList<Player, Object>
    {
        private static final Class<?> CLIENTBOUND_TAB_LIST_PACKET;
        @Nullable
        private static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17;
        @Nullable
        protected static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_CTOR;
        @Nullable
        private static final Field CRAFT_PLAYER_TAB_LIST_HEADER;
        @Nullable
        private static final Field CRAFT_PLAYER_TAB_LIST_FOOTER;
        @Nullable
        protected static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER;
        @Nullable
        protected static final MethodHandle CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER;
        
        private static MethodHandle first(final MethodHandle... handles) {
            for (int i = 0; i < handles.length; ++i) {
                final MethodHandle handle = handles[i];
                if (handle != null) {
                    return handle;
                }
            }
            return null;
        }
        
        @Override
        public boolean isSupported() {
            return (TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR != null || (TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17 != null && TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER != null && TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER != null)) && super.isSupported();
        }
        
        protected Object create117Packet(final Player viewer, @Nullable final Object header, @Nullable final Object footer) throws Throwable {
            return TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR.invoke((header == null) ? this.createMessage(viewer, Component.empty()) : header, (footer == null) ? this.createMessage(viewer, Component.empty()) : footer);
        }
        
        @Override
        public void send(final Player viewer, @Nullable Object header, @Nullable Object footer) {
            try {
                if (TabList.CRAFT_PLAYER_TAB_LIST_HEADER != null && TabList.CRAFT_PLAYER_TAB_LIST_FOOTER != null) {
                    if (header == null) {
                        header = TabList.CRAFT_PLAYER_TAB_LIST_HEADER.get((Object)viewer);
                    }
                    else {
                        TabList.CRAFT_PLAYER_TAB_LIST_HEADER.set((Object)viewer, header);
                    }
                    if (footer == null) {
                        footer = TabList.CRAFT_PLAYER_TAB_LIST_FOOTER.get((Object)viewer);
                    }
                    else {
                        TabList.CRAFT_PLAYER_TAB_LIST_FOOTER.set((Object)viewer, footer);
                    }
                }
                Object packet;
                if (TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR != null) {
                    packet = this.create117Packet(viewer, header, footer);
                }
                else {
                    packet = TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17.invoke();
                    TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER.invoke(packet, (header == null) ? this.createMessage(viewer, Component.empty()) : header);
                    TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER.invoke(packet, (footer == null) ? this.createMessage(viewer, Component.empty()) : footer);
                }
                this.sendPacket(viewer, packet);
            }
            catch (final Throwable thr) {
                Knob.logError(thr, "Failed to send tab list header and footer to %s", viewer);
            }
        }
        
        static {
            CLIENTBOUND_TAB_LIST_PACKET = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("PacketPlayOutPlayerListHeaderFooter"), MinecraftReflection.findMcClassName("network.protocol.game.PacketPlayOutPlayerListHeaderFooter"), MinecraftReflection.findMcClassName("network.protocol.game.ClientboundTabListPacket"));
            CLIENTBOUND_TAB_LIST_PACKET_CTOR_PRE_1_17 = MinecraftReflection.findConstructor(TabList.CLIENTBOUND_TAB_LIST_PACKET, (Class<?>[])new Class[0]);
            CLIENTBOUND_TAB_LIST_PACKET_CTOR = MinecraftReflection.findConstructor(TabList.CLIENTBOUND_TAB_LIST_PACKET, CraftBukkitFacet.CLASS_CHAT_COMPONENT, CraftBukkitFacet.CLASS_CHAT_COMPONENT);
            CRAFT_PLAYER_TAB_LIST_HEADER = MinecraftReflection.findField(TabList.CLASS_CRAFT_PLAYER, "playerListHeader");
            CRAFT_PLAYER_TAB_LIST_FOOTER = MinecraftReflection.findField(TabList.CLASS_CRAFT_PLAYER, "playerListFooter");
            CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER = first(MinecraftReflection.findSetterOf(MinecraftReflection.findField(TabList.CLIENTBOUND_TAB_LIST_PACKET, PaperFacet.NATIVE_COMPONENT_CLASS, "adventure$header")), MinecraftReflection.findSetterOf(MinecraftReflection.findField(TabList.CLIENTBOUND_TAB_LIST_PACKET, CraftBukkitFacet.CLASS_CHAT_COMPONENT, "header", "a")));
            CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER = first(MinecraftReflection.findSetterOf(MinecraftReflection.findField(TabList.CLIENTBOUND_TAB_LIST_PACKET, PaperFacet.NATIVE_COMPONENT_CLASS, "adventure$footer")), MinecraftReflection.findSetterOf(MinecraftReflection.findField(TabList.CLIENTBOUND_TAB_LIST_PACKET, CraftBukkitFacet.CLASS_CHAT_COMPONENT, "footer", "b")));
        }
    }
    
    static final class Translator extends FacetBase<Server> implements FacetComponentFlattener.Translator<Server>
    {
        private static final Class<?> CLASS_LANGUAGE;
        private static final MethodHandle LANGUAGE_GET_INSTANCE;
        private static final MethodHandle LANGUAGE_GET_OR_DEFAULT;
        
        private static MethodHandle unreflectUnchecked(final Method m) {
            try {
                m.setAccessible(true);
                return MinecraftReflection.lookup().unreflect(m);
            }
            catch (final IllegalAccessException ex) {
                return null;
            }
        }
        
        Translator() {
            super(Server.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Translator.LANGUAGE_GET_INSTANCE != null && Translator.LANGUAGE_GET_OR_DEFAULT != null;
        }
        
        @NotNull
        @Override
        public String valueOrDefault(@NotNull final Server game, @NotNull final String key) {
            try {
                return Translator.LANGUAGE_GET_OR_DEFAULT.invoke(Translator.LANGUAGE_GET_INSTANCE.invoke(), key);
            }
            catch (final Throwable ex) {
                Knob.logError(ex, "Failed to transate key '%s'", key);
                return key;
            }
        }
        
        static {
            CLASS_LANGUAGE = MinecraftReflection.findClass(MinecraftReflection.findNmsClassName("LocaleLanguage"), MinecraftReflection.findMcClassName("locale.LocaleLanguage"), MinecraftReflection.findMcClassName("locale.Language"));
            if (Translator.CLASS_LANGUAGE == null) {
                LANGUAGE_GET_INSTANCE = null;
                LANGUAGE_GET_OR_DEFAULT = null;
            }
            else {
                LANGUAGE_GET_INSTANCE = (MethodHandle)Arrays.stream((Object[])Translator.CLASS_LANGUAGE.getDeclaredMethods()).filter(m -> Modifier.isStatic(m.getModifiers()) && !Modifier.isPrivate(m.getModifiers()) && m.getReturnType().equals(Translator.CLASS_LANGUAGE) && m.getParameterCount() == 0).findFirst().map(Translator::unreflectUnchecked).orElse((Object)null);
                LANGUAGE_GET_OR_DEFAULT = (MethodHandle)Arrays.stream((Object[])Translator.CLASS_LANGUAGE.getDeclaredMethods()).filter(m -> !Modifier.isStatic(m.getModifiers()) && Modifier.isPublic(m.getModifiers()) && m.getParameterCount() == 1 && m.getParameterTypes()[0] == String.class && m.getReturnType().equals(String.class)).findFirst().map(Translator::unreflectUnchecked).orElse((Object)null);
            }
        }
    }
}
