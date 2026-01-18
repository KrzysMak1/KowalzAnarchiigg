package cc.dreamcode.kowal.libs.cc.dreamcode.command.handler.exception;

import lombok.Generated;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandInput;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.CommandMeta;

public class InvalidUsageException extends RuntimeException
{
    private final CommandMeta commandMeta;
    private final CommandInput commandInput;
    
    public InvalidUsageException(final CommandMeta commandMeta, @NonNull final CommandInput commandInput, @NonNull final String cause) {
        super(cause);
        if (commandInput == null) {
            throw new NullPointerException("commandInput is marked non-null but is null");
        }
        if (cause == null) {
            throw new NullPointerException("cause is marked non-null but is null");
        }
        this.commandMeta = commandMeta;
        this.commandInput = commandInput;
    }
    
    @Generated
    public CommandMeta getCommandMeta() {
        return this.commandMeta;
    }
    
    @Generated
    public CommandInput getCommandInput() {
        return this.commandInput;
    }
}
