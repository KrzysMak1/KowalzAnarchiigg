package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.object;

import lombok.Generated;
import java.util.function.Consumer;

public class Duo<A, B>
{
    private final A first;
    private final B second;
    
    public Duo<A, B> then(final Consumer<Duo<A, B>> after) {
        after.accept((Object)this);
        return this;
    }
    
    public MutableDuo<A, B> mutable() {
        return MutableDuo.of(this.first, this.second);
    }
    
    @Generated
    public A getFirst() {
        return this.first;
    }
    
    @Generated
    public B getSecond() {
        return this.second;
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Duo)) {
            return false;
        }
        final Duo<?, ?> other = (Duo<?, ?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$first = this.getFirst();
        final Object other$first = other.getFirst();
        Label_0065: {
            if (this$first == null) {
                if (other$first == null) {
                    break Label_0065;
                }
            }
            else if (this$first.equals(other$first)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$second = this.getSecond();
        final Object other$second = other.getSecond();
        if (this$second == null) {
            if (other$second == null) {
                return true;
            }
        }
        else if (this$second.equals(other$second)) {
            return true;
        }
        return false;
    }
    
    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof Duo;
    }
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $first = this.getFirst();
        result = result * 59 + (($first == null) ? 43 : $first.hashCode());
        final Object $second = this.getSecond();
        result = result * 59 + (($second == null) ? 43 : $second.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Duo(first=" + this.getFirst() + ", second=" + this.getSecond() + ")";
    }
    
    @Generated
    private Duo(final A first, final B second) {
        this.first = first;
        this.second = second;
    }
    
    @Generated
    public static <A, B> Duo<A, B> of(final A first, final B second) {
        return new Duo<A, B>(first, second);
    }
}
