package cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit;

import lombok.Generated;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;

public class BukkitSender implements DreamSender<CommandSender>
{
    private final CommandSender commandSender;
    
    @Override
    public Type getType() {
        if (this.commandSender instanceof Player) {
            return Type.CLIENT;
        }
        return Type.CONSOLE;
    }
    
    @Override
    public String getName() {
        return this.commandSender.getName();
    }
    
    @Override
    public boolean hasPermission(@NonNull final String permission) {
        if (permission == null) {
            throw new NullPointerException("permission is marked non-null but is null");
        }
        return this.commandSender.hasPermission(permission);
    }
    
    @Override
    public CommandSender getHandler() {
        return this.commandSender;
    }
    
    @Generated
    public BukkitSender(final CommandSender commandSender) {
        this.commandSender = commandSender;
    }
}
