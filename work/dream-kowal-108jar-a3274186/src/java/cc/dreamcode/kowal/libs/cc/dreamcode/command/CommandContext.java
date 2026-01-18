package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import java.util.Arrays;
import lombok.Generated;
import lombok.NonNull;
import cc.dreamcode.kowal.libs.cc.dreamcode.command.annotation.Command;

public class CommandContext
{
    private final String name;
    private final String[] aliases;
    private final String description;
    
    public CommandContext(@NonNull final Command command) {
        if (command == null) {
            throw new NullPointerException("command is marked non-null but is null");
        }
        this.name = command.name();
        this.aliases = command.aliases();
        this.description = command.description();
    }
    
    @Generated
    public String getName() {
        return this.name;
    }
    
    @Generated
    public String[] getAliases() {
        return this.aliases;
    }
    
    @Generated
    public String getDescription() {
        return this.description;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandContext)) {
            return false;
        }
        final CommandContext other = (CommandContext)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        if (!Arrays.deepEquals((Object[])this.getAliases(), (Object[])other.getAliases())) {
            return false;
        }
        final Object this$description = this.getDescription();
        final Object other$description = other.getDescription();
        if (this$description == null) {
            if (other$description == null) {
                return true;
            }
        }
        else if (this$description.equals(other$description)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CommandContext;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + Arrays.deepHashCode((Object[])this.getAliases());
        final Object $description = this.getDescription();
        result = result * 59 + (($description == null) ? 43 : $description.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "CommandContext(name=" + this.getName() + ", aliases=" + Arrays.deepToString((Object[])this.getAliases()) + ", description=" + this.getDescription() + ")";
    }
    
    @Generated
    public CommandContext(final String name, final String[] aliases, final String description) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
    }
}
