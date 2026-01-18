package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit;

import java.util.List;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandInput;
import org.bukkit.command.CommandSender;
import java.util.Arrays;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandContext;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.command.Command;

public class BukkitCommandWrapper extends Command implements PluginIdentifiableCommand
{
    private final Plugin plugin;
    private final CommandContext commandContext;
    private final BukkitCommandProvider bukkitCommandProvider;
    
    public BukkitCommandWrapper(@NonNull final Plugin plugin, @NonNull final CommandContext context, @NonNull final BukkitCommandProvider bukkitCommandProvider) {
        super(context.getName());
        if (plugin == null) {
            throw new NullPointerException("plugin is marked non-null but is null");
        }
        if (context == null) {
            throw new NullPointerException("context is marked non-null but is null");
        }
        if (bukkitCommandProvider == null) {
            throw new NullPointerException("bukkitCommandProvider is marked non-null but is null");
        }
        this.plugin = plugin;
        this.commandContext = context;
        this.bukkitCommandProvider = bukkitCommandProvider;
        this.setDescription(context.getDescription());
        this.setAliases(Arrays.asList((Object[])context.getAliases()));
    }
    
    public Plugin getPlugin() {
        return this.plugin;
    }
    
    public boolean execute(final CommandSender sender, final String commandLabel, final String[] args) {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandContext.getName(), args, false);
        this.bukkitCommandProvider.call(bukkitSender, commandInput);
        return true;
    }
    
    public List<String> tabComplete(final CommandSender sender, final String alias, final String[] args) throws IllegalArgumentException {
        final BukkitSender bukkitSender = new BukkitSender(sender);
        final CommandInput commandInput = new CommandInput(this.commandContext.getName(), args, false);
        return this.bukkitCommandProvider.getSuggestion(bukkitSender, commandInput);
    }
}
