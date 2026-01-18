package cc.dreamcode.kowal.libs.cc.dreamcode.command;

import lombok.Generated;

public class CommandArgument
{
    private final Type type;
    private final String value;
    
    @Generated
    public CommandArgument(final Type type, final String value) {
        this.type = type;
        this.value = value;
    }
    
    @Generated
    public Type getType() {
        return this.type;
    }
    
    @Generated
    public String getValue() {
        return this.value;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof CommandArgument)) {
            return false;
        }
        final CommandArgument other = (CommandArgument)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value == null) {
            if (other$value == null) {
                return true;
            }
        }
        else if (this$value.equals(other$value)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CommandArgument;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "CommandArgument(type=" + (Object)this.getType() + ", value=" + this.getValue() + ")";
    }
    
    enum Type
    {
        ARG, 
        OPTIONAL_ARG, 
        ARGS;
    }
}
