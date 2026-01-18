package cc.dreamcode.kowal.libs.eu.okaeri.configs.postprocessor;

import lombok.NonNull;

public class ConfigLineInfo
{
    private int indent;
    private int change;
    private String name;
    
    public static ConfigLineInfo of(final int indent, final int change, @NonNull final String name) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        final ConfigLineInfo info = new ConfigLineInfo();
        info.indent = indent;
        info.change = change;
        info.name = name;
        return info;
    }
    
    public int getIndent() {
        return this.indent;
    }
    
    public int getChange() {
        return this.change;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setIndent(final int indent) {
        this.indent = indent;
    }
    
    public void setChange(final int change) {
        this.change = change;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ConfigLineInfo)) {
            return false;
        }
        final ConfigLineInfo other = (ConfigLineInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getIndent() != other.getIndent()) {
            return false;
        }
        if (this.getChange() != other.getChange()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return true;
            }
        }
        else if (this$name.equals(other$name)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigLineInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIndent();
        result = result * 59 + this.getChange();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ConfigLineInfo(indent=" + this.getIndent() + ", change=" + this.getChange() + ", name=" + this.getName() + ")";
    }
}
