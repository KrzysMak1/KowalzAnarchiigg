package cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.commons.duration;

import java.time.temporal.TemporalUnit;
import cc.dreamcode.kowal.libs.eu.okaeri.configs.serdes.SerdesContextAttachment;

public final class DurationSpecData implements SerdesContextAttachment
{
    private final TemporalUnit fallbackUnit;
    private final DurationFormat format;
    
    private DurationSpecData(final TemporalUnit fallbackUnit, final DurationFormat format) {
        this.fallbackUnit = fallbackUnit;
        this.format = format;
    }
    
    public static DurationSpecData of(final TemporalUnit fallbackUnit, final DurationFormat format) {
        return new DurationSpecData(fallbackUnit, format);
    }
    
    public TemporalUnit getFallbackUnit() {
        return this.fallbackUnit;
    }
    
    public DurationFormat getFormat() {
        return this.format;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof DurationSpecData)) {
            return false;
        }
        final DurationSpecData other = (DurationSpecData)o;
        final Object this$fallbackUnit = this.getFallbackUnit();
        final Object other$fallbackUnit = other.getFallbackUnit();
        Label_0055: {
            if (this$fallbackUnit == null) {
                if (other$fallbackUnit == null) {
                    break Label_0055;
                }
            }
            else if (this$fallbackUnit.equals(other$fallbackUnit)) {
                break Label_0055;
            }
            return false;
        }
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
        final Object $fallbackUnit = this.getFallbackUnit();
        result = result * 59 + (($fallbackUnit == null) ? 43 : $fallbackUnit.hashCode());
        final Object $format = this.getFormat();
        result = result * 59 + (($format == null) ? 43 : $format.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "DurationSpecData(fallbackUnit=" + (Object)this.getFallbackUnit() + ", format=" + (Object)this.getFormat() + ")";
    }
}
