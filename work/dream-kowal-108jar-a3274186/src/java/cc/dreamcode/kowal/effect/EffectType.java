package cc.dreamcode.kowal.effect;

import lombok.Generated;

public enum EffectType
{
    POTION_DURATION("potion"), 
    DAMAGE("damage"), 
    ARMOR_DAMAGE("armor"), 
    ARROW("arrow");
    
    final String data;
    
    private EffectType(final String data) {
        this.data = data;
    }
    
    @Generated
    public String getData() {
        return this.data;
    }
}
