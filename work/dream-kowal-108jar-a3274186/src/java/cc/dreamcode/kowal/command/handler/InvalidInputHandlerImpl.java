package cc.dreamcode.kowal.command.handler;

import lombok.Generated;
import cc.dreamcode.kowal.libs.eu.okaeri.injector.annotation.Inject;
import java.util.Map;
import cc.dreamcode.kowal.libs.cc.dreamcode.utilities.builder.MapBuilder;
import org.bukkit.World;
import org.bukkit.entity.Player;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.bukkit.BukkitSender;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.DreamSender;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.InvalidInputHandler;

public class InvalidInputHandlerImpl implements InvalidInputHandler
{
    private final MessageConfig messageConfig;
    
    @Override
    public void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final Class<?> requiringClass, @NonNull final String input) {
        if (dreamSender == null) {
            throw new NullPointerException("dreamSender is marked non-null but is null");
        }
        if (requiringClass == null) {
            throw new NullPointerException("requiringClass is marked non-null but is null");
        }
        if (input == null) {
            throw new NullPointerException("input is marked non-null but is null");
        }
        final BukkitSender bukkitSender = (BukkitSender)dreamSender;
        if (requiringClass.isAssignableFrom(Player.class)) {
            this.messageConfig.playerNotFound.send(bukkitSender.getHandler());
            return;
        }
        if (requiringClass.isAssignableFrom(World.class)) {
            this.messageConfig.worldNotFound.send(bukkitSender.getHandler());
            return;
        }
        this.messageConfig.invalidFormat.send(bukkitSender.getHandler(), (Map<String, Object>)new MapBuilder<String, String>().put("input", input).build());
    }
    
    @Inject
    @Generated
    public InvalidInputHandlerImpl(final MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }
}
