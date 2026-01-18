package cc.dreamcode.kowal.effect;

import lombok.Generated;

public class Effect
{
    private final String lore;
    private final int amplifierChance;
    
    @Generated
    public Effect(final String lore, final int amplifierChance) {
        this.lore = lore;
        this.amplifierChance = amplifierChance;
    }
    
    @Generated
    public String getLore() {
        return this.lore;
    }
    
    @Generated
    public int getAmplifierChance() {
        return this.amplifierChance;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Effect)) {
            return false;
        }
        final Effect other = (Effect)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAmplifierChance() != other.getAmplifierChance()) {
            return false;
        }
        final Object this$lore = this.getLore();
        final Object other$lore = other.getLore();
        if (this$lore == null) {
            if (other$lore == null) {
                return true;
            }
        }
        else if (this$lore.equals(other$lore)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Effect;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAmplifierChance();
        final Object $lore = this.getLore();
        result = result * 59 + (($lore == null) ? 43 : $lore.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Effect(lore=" + this.getLore() + ", amplifierChance=" + this.getAmplifierChance() + ")";
    }
}
