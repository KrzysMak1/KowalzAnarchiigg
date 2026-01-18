package cc.dreamcode.kowal.libs.eu.okaeri.configs.schema;

public class GenericsPair<L, R>
{
    private GenericsDeclaration from;
    private GenericsDeclaration to;
    
    public GenericsPair<R, L> reverse() {
        return (GenericsPair<R, L>)new GenericsPair(this.to, this.from);
    }
    
    public GenericsDeclaration getFrom() {
        return this.from;
    }
    
    public GenericsDeclaration getTo() {
        return this.to;
    }
    
    public void setFrom(final GenericsDeclaration from) {
        this.from = from;
    }
    
    public void setTo(final GenericsDeclaration to) {
        this.to = to;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GenericsPair)) {
            return false;
        }
        final GenericsPair<?, ?> other = (GenericsPair<?, ?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$from = this.getFrom();
        final Object other$from = other.getFrom();
        Label_0065: {
            if (this$from == null) {
                if (other$from == null) {
                    break Label_0065;
                }
            }
            else if (this$from.equals(other$from)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$to = this.getTo();
        final Object other$to = other.getTo();
        if (this$to == null) {
            if (other$to == null) {
                return true;
            }
        }
        else if (this$to.equals(other$to)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GenericsPair;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $from = this.getFrom();
        result = result * 59 + (($from == null) ? 43 : $from.hashCode());
        final Object $to = this.getTo();
        result = result * 59 + (($to == null) ? 43 : $to.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "GenericsPair(from=" + (Object)this.getFrom() + ", to=" + (Object)this.getTo() + ")";
    }
    
    public GenericsPair(final GenericsDeclaration from, final GenericsDeclaration to) {
        this.from = from;
        this.to = to;
    }
}
