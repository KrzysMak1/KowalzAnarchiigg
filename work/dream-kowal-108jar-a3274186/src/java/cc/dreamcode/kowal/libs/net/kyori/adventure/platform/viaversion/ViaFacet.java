package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.viaversion;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import java.util.Iterator;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Collection;
import java.util.UUID;
import java.util.Set;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.List;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.MessageType;
import com.viaversion.viaversion.api.type.Type;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import com.viaversion.viaversion.libs.gson.JsonParser;
import com.viaversion.viaversion.libs.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import java.text.MessageFormat;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.Protocol;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;
import com.viaversion.viaversion.api.Via;
import org.jetbrains.annotations.NotNull;
import com.viaversion.viaversion.api.connection.UserConnection;
import java.util.function.Function;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetBase;

public class ViaFacet<V> extends FacetBase<V> implements Facet.Message<V, String>
{
    private static final String PACKAGE = "com.viaversion.viaversion";
    private static final int SUPPORTED_VIA_MAJOR_VERSION = 4;
    private static final boolean SUPPORTED;
    private final Function<V, UserConnection> connectionFunction;
    private final int minProtocol;
    
    public ViaFacet(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction, final int minProtocol) {
        super(viewerClass);
        this.connectionFunction = connectionFunction;
        this.minProtocol = minProtocol;
    }
    
    @Override
    public boolean isSupported() {
        return super.isSupported() && ViaFacet.SUPPORTED && this.connectionFunction != null && this.minProtocol >= 0;
    }
    
    @Override
    public boolean isApplicable(@NotNull final V viewer) {
        return super.isApplicable(viewer) && this.minProtocol > Via.getAPI().getServerVersion().lowestSupportedVersion() && this.findProtocol(viewer) >= this.minProtocol;
    }
    
    @Nullable
    public UserConnection findConnection(@NotNull final V viewer) {
        return (UserConnection)this.connectionFunction.apply((Object)viewer);
    }
    
    public int findProtocol(@NotNull final V viewer) {
        final UserConnection connection = this.findConnection(viewer);
        if (connection != null) {
            return connection.getProtocolInfo().getProtocolVersion();
        }
        return -1;
    }
    
    @NotNull
    @Override
    public String createMessage(@NotNull final V viewer, @NotNull final Component message) {
        final int protocol = this.findProtocol(viewer);
        if (protocol >= 713) {
            return ((ComponentSerializer<Component, O, String>)GsonComponentSerializer.gson()).serialize(message);
        }
        return ((ComponentSerializer<Component, O, String>)GsonComponentSerializer.colorDownsamplingGson()).serialize(message);
    }
    
    static {
        boolean supported = false;
        try {
            Class.forName("com.viaversion.viaversion.api.ViaAPI").getDeclaredMethod("majorVersion", (Class<?>[])new Class[0]);
            supported = (Via.getAPI().majorVersion() == 4);
        }
        catch (final Throwable t) {}
        SUPPORTED = (supported && Knob.isEnabled("viaversion", true));
    }
    
    public static class ProtocolBased<V> extends ViaFacet<V>
    {
        private final Class<? extends Protocol<?, ?, ?, ?>> protocolClass;
        private final Class<? extends ClientboundPacketType> packetClass;
        private final int packetId;
        
        protected ProtocolBased(@NotNull final String fromProtocol, @NotNull final String toProtocol, final int minProtocol, @NotNull final String packetName, @NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super(viewerClass, connectionFunction, minProtocol);
            final String protocolClassName = MessageFormat.format("{0}.protocols.protocol{1}to{2}.Protocol{1}To{2}", new Object[] { "com.viaversion.viaversion", fromProtocol, toProtocol });
            final String packetClassName = MessageFormat.format("{0}.protocols.protocol{1}to{2}.ClientboundPackets{1}", new Object[] { "com.viaversion.viaversion", fromProtocol, toProtocol });
            Class<? extends Protocol<?, ?, ?, ?>> protocolClass = null;
            Class<? extends ClientboundPacketType> packetClass = null;
            int packetId = -1;
            try {
                protocolClass = (Class<? extends Protocol<?, ?, ?, ?>>)Class.forName(protocolClassName);
                packetClass = (Class<? extends ClientboundPacketType>)Class.forName(packetClassName);
                for (final ClientboundPacketType type : (ClientboundPacketType[])packetClass.getEnumConstants()) {
                    if (type.getName().equals((Object)packetName)) {
                        packetId = type.getId();
                        break;
                    }
                }
            }
            catch (final Throwable t) {}
            this.protocolClass = protocolClass;
            this.packetClass = packetClass;
            this.packetId = packetId;
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && this.protocolClass != null && this.packetClass != null && this.packetId >= 0;
        }
        
        public PacketWrapper createPacket(@NotNull final V viewer) {
            return PacketWrapper.create(this.packetId, (ByteBuf)null, this.findConnection(viewer));
        }
        
        public void sendPacket(@NotNull final PacketWrapper packet) {
            if (packet.user() == null) {
                return;
            }
            try {
                packet.scheduleSend((Class)this.protocolClass);
            }
            catch (final Throwable error) {
                Knob.logError(error, "Failed to send ViaVersion packet: %s %s", packet.user(), packet);
            }
        }
        
        @NotNull
        public JsonElement parse(@NotNull final String message) {
            return JsonParser.parseString(message);
        }
    }
    
    public static class Chat<V> extends ProtocolBased<V> implements Facet.ChatPacket<V, String>
    {
        public Chat(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super("1_16", "1_15_2", 713, "CHAT_MESSAGE", viewerClass, connectionFunction);
        }
        
        @Override
        public void sendMessage(@NotNull final V viewer, @NotNull final Identity source, @NotNull final String message, @NotNull final Object type) {
            final PacketWrapper packet = this.createPacket(viewer);
            packet.write(Type.COMPONENT, (Object)this.parse(message));
            packet.write((Type)Type.BYTE, (Object)this.createMessageType((type instanceof MessageType) ? ((MessageType)type) : MessageType.SYSTEM));
            packet.write(Type.UUID, (Object)source.uuid());
            this.sendPacket(packet);
        }
    }
    
    public static class ActionBar<V> extends ViaFacet.Chat<V> implements Facet.ActionBar<V, String>
    {
        public ActionBar(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super(viewerClass, connectionFunction);
        }
        
        @Override
        public byte createMessageType(@NotNull final MessageType type) {
            return 2;
        }
        
        @Override
        public void sendMessage(@NotNull final V viewer, @NotNull final String message) {
            this.sendMessage(viewer, Identity.nil(), message, MessageType.CHAT);
        }
    }
    
    public static class ActionBarTitle<V> extends ProtocolBased<V> implements Facet.ActionBar<V, String>
    {
        public ActionBarTitle(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super("1_11", "1_10", 310, "TITLE", viewerClass, connectionFunction);
        }
        
        @Override
        public void sendMessage(@NotNull final V viewer, @NotNull final String message) {
            final PacketWrapper packet = this.createPacket(viewer);
            packet.write((Type)Type.VAR_INT, (Object)2);
            packet.write(Type.COMPONENT, (Object)this.parse(message));
            this.sendPacket(packet);
        }
    }
    
    public static class Title<V> extends ProtocolBased<V> implements Facet.TitlePacket<V, String, List<Consumer<PacketWrapper>>, Consumer<V>>
    {
        protected Title(@NotNull final String fromProtocol, @NotNull final String toProtocol, final int minProtocol, @NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            super(fromProtocol, toProtocol, minProtocol, "TITLE", viewerClass, connectionFunction);
        }
        
        public Title(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
            this("1_16", "1_15_2", 713, viewerClass, connectionFunction);
        }
        
        @NotNull
        @Override
        public List<Consumer<PacketWrapper>> createTitleCollection() {
            return (List<Consumer<PacketWrapper>>)new ArrayList();
        }
        
        @Override
        public void contributeTitle(@NotNull final List<Consumer<PacketWrapper>> coll, @NotNull final String title) {
            coll.add((Object)(packet -> {
                packet.write((Type)Type.VAR_INT, (Object)0);
                packet.write(Type.COMPONENT, (Object)this.parse(title));
            }));
        }
        
        @Override
        public void contributeSubtitle(@NotNull final List<Consumer<PacketWrapper>> coll, @NotNull final String subtitle) {
            coll.add((Object)(packet -> {
                packet.write((Type)Type.VAR_INT, (Object)1);
                packet.write(Type.COMPONENT, (Object)this.parse(subtitle));
            }));
        }
        
        @Override
        public void contributeTimes(@NotNull final List<Consumer<PacketWrapper>> coll, final int inTicks, final int stayTicks, final int outTicks) {
            coll.add((Object)(packet -> {
                packet.write((Type)Type.VAR_INT, (Object)3);
                packet.write((Type)Type.INT, (Object)inTicks);
                packet.write((Type)Type.INT, (Object)stayTicks);
                packet.write((Type)Type.INT, (Object)outTicks);
            }));
        }
        
        @Nullable
        @Override
        public Consumer<V> completeTitle(@NotNull final List<Consumer<PacketWrapper>> coll) {
            return (Consumer<V>)(v -> {
                for (int i = 0, length = coll.size(); i < length; ++i) {
                    final PacketWrapper pkt = this.createPacket((V)v);
                    ((Consumer)coll.get(i)).accept((Object)pkt);
                    this.sendPacket(pkt);
                }
            });
        }
        
        @Override
        public void showTitle(@NotNull final V viewer, @NotNull final Consumer<V> title) {
            title.accept((Object)viewer);
        }
        
        @Override
        public void clearTitle(@NotNull final V viewer) {
            final PacketWrapper packet = this.createPacket(viewer);
            packet.write((Type)Type.VAR_INT, (Object)4);
            this.sendPacket(packet);
        }
        
        @Override
        public void resetTitle(@NotNull final V viewer) {
            final PacketWrapper packet = this.createPacket(viewer);
            packet.write((Type)Type.VAR_INT, (Object)5);
            this.sendPacket(packet);
        }
    }
    
    public static final class BossBar<V> extends ProtocolBased<V> implements Facet.BossBarPacket<V>
    {
        private final Set<V> viewers;
        private UUID id;
        private String title;
        private float health;
        private int color;
        private int overlay;
        private byte flags;
        
        private BossBar(@NotNull final String fromProtocol, @NotNull final String toProtocol, @NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction, final Collection<V> viewers) {
            super(fromProtocol, toProtocol, 356, "BOSSBAR", viewerClass, connectionFunction);
            this.viewers = (Set<V>)new CopyOnWriteArraySet((Collection)viewers);
        }
        
        @Override
        public void bossBarInitialized(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar) {
            super.bossBarInitialized(bar);
            this.id = UUID.randomUUID();
            this.broadcastPacket(0);
        }
        
        @Override
        public void bossBarNameChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Component oldName, @NotNull final Component newName) {
            if (!this.viewers.isEmpty()) {
                this.title = this.createMessage((V)this.viewers.iterator().next(), newName);
                this.broadcastPacket(3);
            }
        }
        
        @Override
        public void bossBarProgressChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final float oldPercent, final float newPercent) {
            this.health = newPercent;
            this.broadcastPacket(2);
        }
        
        @Override
        public void bossBarColorChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color oldColor, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Color newColor) {
            this.color = this.createColor(newColor);
            this.broadcastPacket(4);
        }
        
        @Override
        public void bossBarOverlayChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay oldOverlay, final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Overlay newOverlay) {
            this.overlay = this.createOverlay(newOverlay);
            this.broadcastPacket(4);
        }
        
        @Override
        public void bossBarFlagsChanged(final cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar bar, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsAdded, @NotNull final Set<cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar.Flag> flagsRemoved) {
            this.flags = this.createFlag(this.flags, flagsAdded, flagsRemoved);
            this.broadcastPacket(5);
        }
        
        public void sendPacket(@NotNull final V viewer, final int action) {
            final PacketWrapper packet = this.createPacket(viewer);
            packet.write(Type.UUID, (Object)this.id);
            packet.write((Type)Type.VAR_INT, (Object)action);
            if (action == 0 || action == 3) {
                packet.write(Type.COMPONENT, (Object)this.parse(this.title));
            }
            if (action == 0 || action == 2) {
                packet.write((Type)Type.FLOAT, (Object)this.health);
            }
            if (action == 0 || action == 4) {
                packet.write((Type)Type.VAR_INT, (Object)this.color);
                packet.write((Type)Type.VAR_INT, (Object)this.overlay);
            }
            if (action == 0 || action == 5) {
                packet.write((Type)Type.BYTE, (Object)this.flags);
            }
            this.sendPacket(packet);
        }
        
        public void broadcastPacket(final int action) {
            if (this.isEmpty()) {
                return;
            }
            for (final V viewer : this.viewers) {
                this.sendPacket(viewer, action);
            }
        }
        
        @Override
        public void addViewer(@NotNull final V viewer) {
            if (this.viewers.add((Object)viewer)) {
                this.sendPacket(viewer, 0);
            }
        }
        
        @Override
        public void removeViewer(@NotNull final V viewer) {
            if (this.viewers.remove((Object)viewer)) {
                this.sendPacket(viewer, 1);
            }
        }
        
        @Override
        public boolean isEmpty() {
            return this.id == null || this.viewers.isEmpty();
        }
        
        @Override
        public void close() {
            this.broadcastPacket(1);
            this.viewers.clear();
        }
        
        public static class Builder<V> extends ViaFacet<V> implements Facet.BossBar.Builder<V, Facet.BossBar<V>>
        {
            public Builder(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
                super(viewerClass, connectionFunction, 713);
            }
            
            @Override
            public Facet.BossBar<V> createBossBar(@NotNull final Collection<V> viewer) {
                return new ViaFacet.BossBar<V>("1_16", "1_15_2", (Class)this.viewerClass, this::findConnection, (Collection)viewer);
            }
        }
        
        public static class Builder1_9_To_1_15<V> extends ViaFacet<V> implements Facet.BossBar.Builder<V, Facet.BossBar<V>>
        {
            public Builder1_9_To_1_15(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> connectionFunction) {
                super(viewerClass, connectionFunction, 356);
            }
            
            @Override
            public Facet.BossBar<V> createBossBar(@NotNull final Collection<V> viewer) {
                return new ViaFacet.BossBar<V>("1_9", "1_8", (Class)this.viewerClass, this::findConnection, (Collection)viewer);
            }
        }
    }
    
    public static final class TabList<V> extends ProtocolBased<V> implements Facet.TabList<V, String>
    {
        public TabList(@NotNull final Class<? extends V> viewerClass, @NotNull final Function<V, UserConnection> userConnection) {
            super("1_16", "1_15_2", 713, "TAB_LIST", viewerClass, userConnection);
        }
        
        @Override
        public void send(final V viewer, @Nullable final String header, @Nullable final String footer) {
            final PacketWrapper packet = this.createPacket(viewer);
            packet.write(Type.COMPONENT, (Object)this.parse(header));
            packet.write(Type.COMPONENT, (Object)this.parse(footer));
            this.sendPacket(packet);
        }
    }
}
