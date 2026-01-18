package cc.dreamcode.kowal.libs.eu.okaeri.placeholders.message.part;

import lombok.NonNull;

public class MessageStatic implements MessageElement
{
    private String value;
    
    public static MessageStatic of(@NonNull final String value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return new MessageStatic(value);
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MessageStatic)) {
            return false;
        }
        final MessageStatic other = (MessageStatic)o;
        if (!other.canEqual(this)) {
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
    
    protected boolean canEqual(final Object other) {
        return other instanceof MessageStatic;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.getValue();
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MessageStatic(value=" + this.getValue() + ")";
    }
    
    private MessageStatic(final String value) {
        this.value = value;
    }
}
