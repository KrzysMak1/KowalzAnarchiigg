package cc.dreamcode.kowal.level;

import lombok.Generated;
import org.bukkit.Material;
import java.util.Map;

public class Level
{
    private final Map<Material, Integer> upgradeItems;
    private final String upgradeItemsLore;
    private final String costLore;
    private final double moneyUpgrade;
    private final String itemLoreDisplay;
    private final double hpReduce;
    private final int chance;
    
    @Generated
    public Level(final Map<Material, Integer> upgradeItems, final String upgradeItemsLore, final String costLore, final double moneyUpgrade, final String itemLoreDisplay, final double hpReduce, final int chance) {
        this.upgradeItems = upgradeItems;
        this.upgradeItemsLore = upgradeItemsLore;
        this.costLore = costLore;
        this.moneyUpgrade = moneyUpgrade;
        this.itemLoreDisplay = itemLoreDisplay;
        this.hpReduce = hpReduce;
        this.chance = chance;
    }
    
    @Generated
    public Map<Material, Integer> getUpgradeItems() {
        return this.upgradeItems;
    }
    
    @Generated
    public String getUpgradeItemsLore() {
        return this.upgradeItemsLore;
    }
    
    @Generated
    public String getCostLore() {
        return this.costLore;
    }
    
    @Generated
    public double getMoneyUpgrade() {
        return this.moneyUpgrade;
    }
    
    @Generated
    public String getItemLoreDisplay() {
        return this.itemLoreDisplay;
    }
    
    @Generated
    public double getHpReduce() {
        return this.hpReduce;
    }
    
    @Generated
    public int getChance() {
        return this.chance;
    }

    public boolean hasUpgradeItems() {
        return this.upgradeItems != null && !this.upgradeItems.isEmpty();
    }

    public boolean hasMoneyUpgrade() {
        return this.moneyUpgrade > 0.0;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Level)) {
            return false;
        }
        final Level other = (Level)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (Double.compare(this.getMoneyUpgrade(), other.getMoneyUpgrade()) != 0) {
            return false;
        }
        if (Double.compare(this.getHpReduce(), other.getHpReduce()) != 0) {
            return false;
        }
        if (this.getChance() != other.getChance()) {
            return false;
        }
        final Object this$upgradeItems = this.getUpgradeItems();
        final Object other$upgradeItems = other.getUpgradeItems();
        Label_0110: {
            if (this$upgradeItems == null) {
                if (other$upgradeItems == null) {
                    break Label_0110;
                }
            }
            else if (this$upgradeItems.equals(other$upgradeItems)) {
                break Label_0110;
            }
            return false;
        }
        final Object this$upgradeItemsLore = this.getUpgradeItemsLore();
        final Object other$upgradeItemsLore = other.getUpgradeItemsLore();
        Label_0147: {
            if (this$upgradeItemsLore == null) {
                if (other$upgradeItemsLore == null) {
                    break Label_0147;
                }
            }
            else if (this$upgradeItemsLore.equals(other$upgradeItemsLore)) {
                break Label_0147;
            }
            return false;
        }
        final Object this$costLore = this.getCostLore();
        final Object other$costLore = other.getCostLore();
        Label_0184: {
            if (this$costLore == null) {
                if (other$costLore == null) {
                    break Label_0184;
                }
            }
            else if (this$costLore.equals(other$costLore)) {
                break Label_0184;
            }
            return false;
        }
        final Object this$itemLoreDisplay = this.getItemLoreDisplay();
        final Object other$itemLoreDisplay = other.getItemLoreDisplay();
        if (this$itemLoreDisplay == null) {
            if (other$itemLoreDisplay == null) {
                return true;
            }
        }
        else if (this$itemLoreDisplay.equals(other$itemLoreDisplay)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Level;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $moneyUpgrade = Double.doubleToLongBits(this.getMoneyUpgrade());
        result = result * 59 + (int)($moneyUpgrade >>> 32 ^ $moneyUpgrade);
        final long $hpReduce = Double.doubleToLongBits(this.getHpReduce());
        result = result * 59 + (int)($hpReduce >>> 32 ^ $hpReduce);
        result = result * 59 + this.getChance();
        final Object $upgradeItems = this.getUpgradeItems();
        result = result * 59 + (($upgradeItems == null) ? 43 : $upgradeItems.hashCode());
        final Object $upgradeItemsLore = this.getUpgradeItemsLore();
        result = result * 59 + (($upgradeItemsLore == null) ? 43 : $upgradeItemsLore.hashCode());
        final Object $costLore = this.getCostLore();
        result = result * 59 + (($costLore == null) ? 43 : $costLore.hashCode());
        final Object $itemLoreDisplay = this.getItemLoreDisplay();
        result = result * 59 + (($itemLoreDisplay == null) ? 43 : $itemLoreDisplay.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Level(upgradeItems=" + (Object)this.getUpgradeItems() + ", upgradeItemsLore=" + this.getUpgradeItemsLore() + ", costLore=" + this.getCostLore() + ", moneyUpgrade=" + this.getMoneyUpgrade() + ", itemLoreDisplay=" + this.getItemLoreDisplay() + ", hpReduce=" + this.getHpReduce() + ", chance=" + this.getChance() + ")";
    }
}
