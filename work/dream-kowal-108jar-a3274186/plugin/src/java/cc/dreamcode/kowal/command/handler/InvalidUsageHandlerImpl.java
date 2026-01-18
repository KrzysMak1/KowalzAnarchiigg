package cc.dreamcode.kowal.command.handler;

import lombok.Generated;
import eu.okaeri.injector.annotation.Inject;
import java.util.Iterator;
import java.util.List;
import cc.dreamcode.command.CommandPathMeta;
import java.util.Map;
import cc.dreamcode.command.bukkit.BukkitSender;
import cc.dreamcode.command.CommandInput;
import cc.dreamcode.command.CommandMeta;
import java.util.Optional;
import lombok.NonNull;
import cc.dreamcode.command.DreamSender;
import cc.dreamcode.kowal.config.MessageConfig;
import cc.dreamcode.command.handler.InvalidUsageHandler;

public class InvalidUsageHandlerImpl implements InvalidUsageHandler
{
    private final MessageConfig messageConfig;
    
    @Override
    public void handle(@NonNull final DreamSender<?> dreamSender, @NonNull final Optional<CommandMeta> optionalCommandMeta, @NonNull final CommandInput commandInput) {
        if (dreamSender == null) {
            throw new NullPointerException("dreamSender is marked non-null but is null");
        }
        if (optionalCommandMeta == null) {
            throw new NullPointerException("optionalCommandMeta is marked non-null but is null");
        }
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        final BukkitSender bukkitSender = (BukkitSender)dreamSender;
        if (!optionalCommandMeta.isPresent()) {
            this.messageConfig.usageNotFound.send(bukkitSender.getHandler());
            return;
        }
        final CommandMeta commandMeta = (CommandMeta)optionalCommandMeta.get();
        final String commandName = commandMeta.getCommandEntry().getName();
        final String commandDescription = commandMeta.getCommandEntry().getDescription();
        final List<CommandPathMeta> commandPathMetas = commandMeta.getFilteredCommandPaths(dreamSender);
        if (commandPathMetas.isEmpty()) {
            this.messageConfig.pathNotFound.send(bukkitSender.getHandler(), Map.of("label", "/" + commandName, "description", commandDescription));
            return;
        }
        this.messageConfig.usage.send(bukkitSender.getHandler(), Map.of("label", "/" + commandName, "description", commandDescription));
        for (final CommandPathMeta commandPathMeta : commandPathMetas) {
            this.messageConfig.usagePath.send(bukkitSender.getHandler(), Map.of("usage", commandPathMeta.getUsage(), "description", commandPathMeta.getDescription()));
        }
    }
    
    @Inject
    @Generated
    public InvalidUsageHandlerImpl(final MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }
}
