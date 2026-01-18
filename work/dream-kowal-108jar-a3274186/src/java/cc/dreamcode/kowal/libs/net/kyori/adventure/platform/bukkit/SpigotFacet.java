package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import net.md_5.bungee.chat.TranslationRegistry;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetComponentFlattener;
import org.bukkit.Server;
import java.util.Iterator;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import net.md_5.bungee.api.ChatMessageType;
import cc.dreamcode.kowal.libs.net.kyori.adventure.audience.MessageType;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.net.kyori.adventure.identity.Identity;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import net.md_5.bungee.api.chat.BaseComponent;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import cc.dreamcode.kowal.libs.net.kyori.adventure.text.serializer.bungeecord.BungeeComponentSerializer;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Knob;
import org.jetbrains.annotations.Nullable;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetBase;
import org.bukkit.command.CommandSender;

class SpigotFacet<V extends CommandSender> extends FacetBase<V>
{
    private static final boolean SUPPORTED;
    private static final Class<?> BUNGEE_CHAT_MESSAGE_TYPE;
    static final Class<?> BUNGEE_COMPONENT_TYPE;
    
    protected SpigotFacet(@Nullable final Class<? extends V> viewerClass) {
        super(viewerClass);
    }
    
    @Override
    public boolean isSupported() {
        return super.isSupported() && SpigotFacet.SUPPORTED;
    }
    
    static {
        SUPPORTED = (Knob.isEnabled("spigot", true) && BungeeComponentSerializer.isNative());
        BUNGEE_CHAT_MESSAGE_TYPE = MinecraftReflection.findClass("net.md_5.bungee.api.ChatMessageType");
        BUNGEE_COMPONENT_TYPE = MinecraftReflection.findClass("net.md_5.bungee.api.chat.BaseComponent");
    }
    
    static class Message<V extends CommandSender> extends SpigotFacet<V> implements Facet.Message<V, BaseComponent[]>
    {
        private static final BungeeComponentSerializer SERIALIZER;
        
        protected Message(@Nullable final Class<? extends V> viewerClass) {
            super(viewerClass);
        }
        
        @NotNull
        @Override
        public BaseComponent[] createMessage(@NotNull final V viewer, @NotNull final Component message) {
            return Message.SERIALIZER.serialize(message);
        }
        
        static {
            SERIALIZER = BungeeComponentSerializer.of(BukkitComponentSerializer.gson(), BukkitComponentSerializer.legacy());
        }
    }
    
    static final class Chat extends SpigotFacet.Message<CommandSender> implements Facet.Chat<CommandSender, BaseComponent[]>
    {
        private static final boolean SUPPORTED;
        
        protected Chat() {
            super(CommandSender.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Chat.SUPPORTED;
        }
        
        @Override
        public void sendMessage(@NotNull final CommandSender viewer, @NotNull final Identity source, final BaseComponent[] message, @NotNull final Object type) {
            viewer.spigot().sendMessage(message);
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasClass("org.bukkit.command.CommandSender$Spigot");
        }
    }
    
    static class ChatWithType extends SpigotFacet.Message<Player> implements Facet.Chat<Player, BaseComponent[]>
    {
        private static final Class<?> PLAYER_CLASS;
        private static final boolean SUPPORTED;
        
        protected ChatWithType() {
            super(Player.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && ChatWithType.SUPPORTED;
        }
        
        @Nullable
        private ChatMessageType createType(@NotNull final MessageType type) {
            if (type == MessageType.CHAT) {
                return ChatMessageType.CHAT;
            }
            if (type == MessageType.SYSTEM) {
                return ChatMessageType.SYSTEM;
            }
            Knob.logUnsupported(this, type);
            return null;
        }
        
        @Override
        public void sendMessage(@NotNull final Player viewer, @NotNull final Identity source, final BaseComponent[] message, @NotNull final Object type) {
            final ChatMessageType chat = (type instanceof MessageType) ? this.createType((MessageType)type) : ChatMessageType.SYSTEM;
            if (chat != null) {
                viewer.spigot().sendMessage(chat, message);
            }
        }
        
        static {
            PLAYER_CLASS = MinecraftReflection.findClass("org.bukkit.entity.Player$Spigot");
            SUPPORTED = MinecraftReflection.hasMethod(ChatWithType.PLAYER_CLASS, "sendMessage", SpigotFacet.BUNGEE_CHAT_MESSAGE_TYPE, ChatWithType.BUNGEE_COMPONENT_TYPE);
        }
    }
    
    static final class ActionBar extends ChatWithType implements Facet.ActionBar<Player, BaseComponent[]>
    {
        @Override
        public void sendMessage(@NotNull final Player viewer, final BaseComponent[] message) {
            viewer.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
        }
    }
    
    static final class Book extends SpigotFacet.Message<Player> implements Facet.Book<Player, BaseComponent[], ItemStack>
    {
        private static final boolean SUPPORTED;
        
        protected Book() {
            super(Player.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Book.SUPPORTED;
        }
        
        @NotNull
        @Override
        public ItemStack createBook(@NotNull final String title, @NotNull final String author, @NotNull final Iterable<BaseComponent[]> pages) {
            final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
            final ItemMeta meta = book.getItemMeta();
            if (meta instanceof BookMeta) {
                final BookMeta spigot = (BookMeta)meta;
                for (final BaseComponent[] page : pages) {
                    spigot.spigot().addPage(new BaseComponent[][] { page });
                }
                spigot.setTitle(title);
                spigot.setAuthor(author);
                book.setItemMeta((ItemMeta)spigot);
            }
            return book;
        }
        
        @Override
        public void openBook(@NotNull final Player viewer, @NotNull final ItemStack book) {
            viewer.openBook(book);
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasMethod(Player.class, "openBook", ItemStack.class);
        }
    }
    
    static class Translator extends FacetBase<Server> implements FacetComponentFlattener.Translator<Server>
    {
        private static final boolean SUPPORTED;
        
        Translator() {
            super(Server.class);
        }
        
        @Override
        public boolean isSupported() {
            return super.isSupported() && Translator.SUPPORTED;
        }
        
        @NotNull
        @Override
        public String valueOrDefault(@NotNull final Server game, @NotNull final String key) {
            return TranslationRegistry.INSTANCE.translate(key);
        }
        
        static {
            SUPPORTED = MinecraftReflection.hasClass("net.md_5.bungee.chat.TranslationRegistry");
        }
    }
}
