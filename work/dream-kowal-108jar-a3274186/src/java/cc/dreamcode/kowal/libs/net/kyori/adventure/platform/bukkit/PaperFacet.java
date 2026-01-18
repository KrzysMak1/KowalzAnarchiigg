package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.ComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import com.destroystokyo.paper.Title;
import net.md_5.bungee.api.chat.BaseComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import org.jetbrains.annotations.Nullable;
import java.lang.reflect.Method;
import java.lang.invoke.MethodHandle;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetBase;
import org.bukkit.command.CommandSender;

class PaperFacet<V extends CommandSender> extends FacetBase<V>
{
    private static final boolean SUPPORTED;
    static final Class<?> NATIVE_COMPONENT_CLASS;
    private static final MethodHandle PAPER_ADVENTURE_AS_VANILLA;
    private static final Class<?> NATIVE_GSON_COMPONENT_SERIALIZER_CLASS;
    private static final Class<?> NATIVE_GSON_COMPONENT_SERIALIZER_IMPL_CLASS;
    private static final MethodHandle NATIVE_GSON_COMPONENT_SERIALIZER_GSON_GETTER;
    private static final MethodHandle NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD;
    
    @Nullable
    private static MethodHandle findAsVanillaMethod() {
        try {
            final Class<?> paperAdventure = MinecraftReflection.findClass("io.papermc.paper.adventure.PaperAdventure");
            final Method method = paperAdventure.getDeclaredMethod("asVanilla", PaperFacet.NATIVE_COMPONENT_CLASS);
            return MinecraftReflection.lookup().unreflect(method);
        }
        catch (final NoSuchMethodException | IllegalAccessException | NullPointerException e) {
            return null;
        }
    }
    
    @Nullable
    private static MethodHandle findNativeDeserializeMethod() {
        try {
            final Method method = PaperFacet.NATIVE_GSON_COMPONENT_SERIALIZER_IMPL_CLASS.getDeclaredMethod("deserialize", String.class);
            method.setAccessible(true);
            return MinecraftReflection.lookup().unreflect(method);
        }
        catch (final NoSuchMethodException | IllegalAccessException | NullPointerException e) {
            return null;
        }
    }
    
    protected PaperFacet(@Nullable final Class<? extends V> viewerClass) {
        super(viewerClass);
    }
    
    @Override
    public boolean isSupported() {
        return super.isSupported() && PaperFacet.SUPPORTED;
    }
    
    static {
        SUPPORTED = Knob.isEnabled("paper", true);
        NATIVE_COMPONENT_CLASS = MinecraftReflection.findClass(String.join((CharSequence)".", new CharSequence[] { (CharSequence)"net", (CharSequence)"kyori", (CharSequence)"adventure", (CharSequence)"text", (CharSequence)"Component" }));
        PAPER_ADVENTURE_AS_VANILLA = findAsVanillaMethod();
        NATIVE_GSON_COMPONENT_SERIALIZER_CLASS = MinecraftReflection.findClass(String.join((CharSequence)".", new CharSequence[] { (CharSequence)"net", (CharSequence)"kyori", (CharSequence)"adventure", (CharSequence)"text", (CharSequence)"serializer", (CharSequence)"gson", (CharSequence)"GsonComponentSerializer" }));
        NATIVE_GSON_COMPONENT_SERIALIZER_IMPL_CLASS = MinecraftReflection.findClass(String.join((CharSequence)".", new CharSequence[] { (CharSequence)"net", (CharSequence)"kyori", (CharSequence)"adventure", (CharSequence)"text", (CharSequence)"serializer", (CharSequence)"gson", (CharSequence)"GsonComponentSerializerImpl" }));
        NATIVE_GSON_COMPONENT_SERIALIZER_GSON_GETTER = MinecraftReflection.findStaticMethod(PaperFacet.NATIVE_GSON_COMPONENT_SERIALIZER_CLASS, "gson", PaperFacet.NATIVE_GSON_COMPONENT_SERIALIZER_CLASS, (Class<?>[])new Class[0]);
        NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD = findNativeDeserializeMethod();
    }
    
    static class Title extends SpigotFacet.Message<Player> implements Facet.Title<Player, BaseComponent[], com.destroystokyo.paper.Title.Builder, com.destroystokyo.paper.Title>
    {
        private static final boolean SUPPORTED;
        
        protected Title() {
            super(Player.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Title.SUPPORTED;
        }
        
        @Override
        public com.destroystokyo.paper.Title.Builder createTitleCollection() {
            return com.destroystokyo.paper.Title.builder();
        }
        
        @Override
        public void contributeTitle(final com.destroystokyo.paper.Title.Builder coll, final BaseComponent[] title) {
            coll.title(title);
        }
        
        @Override
        public void contributeSubtitle(final com.destroystokyo.paper.Title.Builder coll, final BaseComponent[] subtitle) {
            coll.subtitle(subtitle);
        }
        
        @Override
        public void contributeTimes(final com.destroystokyo.paper.Title.Builder coll, final int inTicks, final int stayTicks, final int outTicks) {
            if (inTicks > -1) {
                coll.fadeIn(inTicks);
            }
            if (stayTicks > -1) {
                coll.stay(stayTicks);
            }
            if (outTicks > -1) {
                coll.fadeOut(outTicks);
            }
        }
        
        @Nullable
        @Override
        public com.destroystokyo.paper.Title completeTitle(final com.destroystokyo.paper.Title.Builder coll) {
            return coll.build();
        }
        
        @Override
        public void showTitle(@NotNull final Player viewer, final com.destroystokyo.paper.Title title) {
            viewer.sendTitle(title);
        }
        
        @Override
        public void clearTitle(@NotNull final Player viewer) {
            viewer.hideTitle();
        }
        
        @Override
        public void resetTitle(@NotNull final Player viewer) {
            viewer.resetTitle();
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasClass("com.destroystokyo.paper.Title");
        }
    }
    
    static class TabList extends CraftBukkitFacet.TabList
    {
        private static final boolean SUPPORTED;
        private static final MethodHandle NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD_BOUND;
        
        @Nullable
        private static MethodHandle createBoundNativeDeserializeMethodHandle() {
            if (TabList.SUPPORTED) {
                try {
                    return PaperFacet.NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD.bindTo(PaperFacet.NATIVE_GSON_COMPONENT_SERIALIZER_GSON_GETTER.invoke());
                }
                catch (final Throwable throwable) {
                    Knob.logError(throwable, "Failed to access native GsonComponentSerializer", new Object[0]);
                    return null;
                }
            }
            return null;
        }
        
        @Override
        public boolean isSupported() {
            return TabList.SUPPORTED && super.isSupported() && ((TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER != null && TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER != null) || PaperFacet.PAPER_ADVENTURE_AS_VANILLA != null);
        }
        
        @Override
        protected Object create117Packet(final Player viewer, @Nullable final Object header, @Nullable final Object footer) throws Throwable {
            if (TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER == null && TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER == null) {
                return TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR.invoke(PaperFacet.PAPER_ADVENTURE_AS_VANILLA.invoke((header == null) ? this.createMessage(viewer, Component.empty()) : header), PaperFacet.PAPER_ADVENTURE_AS_VANILLA.invoke((footer == null) ? this.createMessage(viewer, Component.empty()) : footer));
            }
            final Object packet = TabList.CLIENTBOUND_TAB_LIST_PACKET_CTOR.invoke((Void)null, (Void)null);
            TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_HEADER.invoke(packet, (header == null) ? this.createMessage(viewer, Component.empty()) : header);
            TabList.CLIENTBOUND_TAB_LIST_PACKET_SET_FOOTER.invoke(packet, (footer == null) ? this.createMessage(viewer, Component.empty()) : footer);
            return packet;
        }
        
        @Nullable
        @Override
        public Object createMessage(@NotNull final Player viewer, @NotNull final Component message) {
            try {
                return TabList.NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD_BOUND.invoke((String)((ComponentSerializer<Component, O, String>)GsonComponentSerializer.gson()).serialize(message));
            }
            catch (final Throwable throwable) {
                Knob.logError(throwable, "Failed to create native Component message", new Object[0]);
                return null;
            }
        }
        
        static {
            SUPPORTED = (MinecraftReflection.hasField(TabList.CLASS_CRAFT_PLAYER, PaperFacet.NATIVE_COMPONENT_CLASS, "playerListHeader") && MinecraftReflection.hasField(TabList.CLASS_CRAFT_PLAYER, PaperFacet.NATIVE_COMPONENT_CLASS, "playerListFooter"));
            NATIVE_GSON_COMPONENT_SERIALIZER_DESERIALIZE_METHOD_BOUND = createBoundNativeDeserializeMethodHandle();
        }
    }
}
