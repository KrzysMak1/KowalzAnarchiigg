package cc.dreamcode.kowal.libs.eu.okaeri.configs.yaml.bukkit.serdes.itemstack;

import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContextAttachment;

public final class ItemStackSpecData implements SerdesContextAttachment
{
    private final ItemStackFormat format;
    
    private ItemStackSpecData(final ItemStackFormat format) {
        this.format = format;
    }
    
    public static ItemStackSpecData of(final ItemStackFormat format) {
        return new ItemStackSpecData(format);
    }
    
    public ItemStackFormat getFormat() {
        return this.format;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ItemStackSpecData)) {
            return false;
        }
        final ItemStackSpecData other = (ItemStackSpecData)o;
        final Object this$format = this.getFormat();
        final Object other$format = other.getFormat();
        if (this$format == null) {
            if (other$format == null) {
                return true;
            }
        }
        else if (this$format.equals(other$format)) {
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $format = this.getFormat();
        result = result * 59 + (($format == null) ? 43 : $format.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ItemStackSpecData(format=" + (Object)this.getFormat() + ")";
    }
}
