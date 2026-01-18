package cc.dreamcode.kowal.libs.net.kyori.adventure.platform.bukkit;

import java.util.function.Supplier;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.viaversion.ViaFacet;
import cc.dreamcode.kowal.libs.net.kyori.adventure.bossbar.BossBar;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetAudienceProvider;
import org.jetbrains.annotations.NotNull;
import org.bukkit.util.Vector;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.Facet;
import java.util.Collection;
import com.viaversion.viaversion.api.connection.UserConnection;
import org.bukkit.entity.Player;
import java.util.function.Function;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.net.kyori.adventure.platform.facet.FacetAudience;

final class BukkitAudience extends FacetAudience<CommandSender>
{
    static final ThreadLocal<Plugin> PLUGIN;
    private static final Function<Player, UserConnection> VIA;
    private static final Collection<Facet.Chat<? extends CommandSender, ?>> CHAT;
    private static final Collection<Facet.ActionBar<Player, ?>> ACTION_BAR;
    private static final Collection<Facet.Title<Player, ?, ?, ?>> TITLE;
    private static final Collection<Facet.Sound<Player, Vector>> SOUND;
    private static final Collection<Facet.EntitySound<Player, Object>> ENTITY_SOUND;
    private static final Collection<Facet.Book<Player, ?, ?>> BOOK;
    private static final Collection<Facet.BossBar.Builder<Player, ?>> BOSS_BAR;
    private static final Collection<Facet.TabList<Player, ?>> TAB_LIST;
    private static final Collection<Facet.Pointers<? extends CommandSender>> POINTERS;
    @NotNull
    private final Plugin plugin;
    
    BukkitAudience(@NotNull final Plugin plugin, final FacetAudienceProvider<?, ?> provider, @NotNull final Collection<CommandSender> viewers) {
        super(provider, viewers, BukkitAudience.CHAT, BukkitAudience.ACTION_BAR, BukkitAudience.TITLE, BukkitAudience.SOUND, BukkitAudience.ENTITY_SOUND, BukkitAudience.BOOK, BukkitAudience.BOSS_BAR, BukkitAudience.TAB_LIST, BukkitAudience.POINTERS);
        this.plugin = plugin;
    }
    
    @Override
    public void showBossBar(@NotNull final BossBar bar) {
        BukkitAudience.PLUGIN.set((Object)this.plugin);
        super.showBossBar(bar);
        BukkitAudience.PLUGIN.set((Object)null);
    }
    
    static {
        PLUGIN = new ThreadLocal();
        VIA = (Function)new BukkitFacet.ViaHook();
        CHAT = Facet.of(() -> new ViaFacet.Chat(Player.class, BukkitAudience.VIA), () -> new CraftBukkitFacet.Chat1_19_3(), () -> new CraftBukkitFacet.Chat(), () -> new BukkitFacet.Chat());
        ACTION_BAR = Facet.of(() -> new ViaFacet.ActionBarTitle(Player.class, BukkitAudience.VIA), () -> new ViaFacet.ActionBar(Player.class, BukkitAudience.VIA), () -> new CraftBukkitFacet.ActionBar_1_17(), () -> new CraftBukkitFacet.ActionBar(), () -> new CraftBukkitFacet.ActionBarLegacy());
        TITLE = Facet.of(() -> new ViaFacet.Title(Player.class, BukkitAudience.VIA), () -> new CraftBukkitFacet.Title_1_17(), () -> new CraftBukkitFacet.Title());
        SOUND = Facet.of(() -> new BukkitFacet.SoundWithCategory(), () -> new BukkitFacet.Sound());
        ENTITY_SOUND = Facet.of(() -> new CraftBukkitFacet.EntitySound_1_19_3(), () -> new CraftBukkitFacet.EntitySound());
        BOOK = Facet.of(() -> new CraftBukkitFacet.Book_1_20_5(), () -> new CraftBukkitFacet.BookPost1_13(), () -> new CraftBukkitFacet.Book1_13(), () -> new CraftBukkitFacet.BookPre1_13());
        BOSS_BAR = Facet.of(() -> new ViaFacet.BossBar.Builder(Player.class, BukkitAudience.VIA), () -> new ViaFacet.BossBar.Builder1_9_To_1_15(Player.class, BukkitAudience.VIA), () -> new CraftBukkitFacet.BossBar.Builder(), () -> new BukkitFacet.BossBarBuilder(), () -> new CraftBukkitFacet.BossBarWither.Builder());
        TAB_LIST = Facet.of(() -> new ViaFacet.TabList(Player.class, BukkitAudience.VIA), () -> new PaperFacet.TabList(), () -> new CraftBukkitFacet.TabList(), () -> new BukkitFacet.TabList());
        POINTERS = Facet.of(() -> new BukkitFacet.CommandSenderPointers(), () -> new BukkitFacet.ConsoleCommandSenderPointers(), () -> new BukkitFacet.PlayerPointers());
    }
}
