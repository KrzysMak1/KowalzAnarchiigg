package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit;

import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.suggestion.OfflinePlayersSuggestion;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.suggestion.supplier.SuggestionSupplier;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.suggestion.AllPlayersSuggestion;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.resolver.OfflinePlayerTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.resolver.WorldTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.resolver.transformer.ObjectTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.resolver.PlayerTransformer;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.player.PlayerWorldBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.player.PlayerLocationBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.player.PlayerInventoryBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.player.PlayerGamemodeBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.ServerBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.ConsoleCommandSenderBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.CommandSenderBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.PlayerBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bind.BindResolver;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.bind.BukkitSenderBind;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandScheduler;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandRegistry;
import lombok.NonNull;
import org.bukkit.plugin.Plugin;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandProviderImpl;

public class BukkitCommandProvider extends CommandProviderImpl
{
    public BukkitCommandProvider(@NonNull final Plugin plugin) {
        super(true);
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        this.setCommandRegistry(new BukkitCommandRegistry(plugin, this));
        this.setCommandScheduler(new BukkitCommandScheduler(plugin));
        this.registerBind(new BukkitSenderBind());
        this.registerBind(new PlayerBind());
        this.registerBind(new CommandSenderBind());
        this.registerBind(new ConsoleCommandSenderBind());
        this.registerBind(new ServerBind());
        this.registerBind(new PlayerGamemodeBind());
        this.registerBind(new PlayerInventoryBind());
        this.registerBind(new PlayerLocationBind());
        this.registerBind(new PlayerWorldBind());
        this.registerTransformer(new PlayerTransformer(plugin));
        this.registerTransformer(new WorldTransformer(plugin));
        this.registerTransformer(new OfflinePlayerTransformer(plugin));
        final AllPlayersSuggestion allPlayersSuggestion = new AllPlayersSuggestion(plugin);
        this.registerSuggestion("@allplayers", allPlayersSuggestion);
        this.registerSuggestion("@all-players", allPlayersSuggestion);
        final OfflinePlayersSuggestion offlinePlayersSuggestion = new OfflinePlayersSuggestion(plugin);
        this.registerSuggestion("@offlineplayers", offlinePlayersSuggestion);
        this.registerSuggestion("@offline-players", offlinePlayersSuggestion);
    }
    
    public static BukkitCommandProvider create(@NonNull final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        return new BukkitCommandProvider(plugin);
    }
}
