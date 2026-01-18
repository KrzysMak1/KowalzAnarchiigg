package cc.dreamcode.kowal.libs.eu.okaeri.injector;

import lombok.NonNull;

public class Injectable<T>
{
    private final String name;
    private final T object;
    private final Class<T> type;
    
    public static <T> Injectable<T> of(@NonNull final String name, @NonNull final T object, @NonNull final Class<T> type) {
        if (name == null) {
            throw new NullPointerException("name is marked non-null but is null");
        }
        if (object == null) {
            throw new NullPointerException("object is marked non-null but is null");
        }
        if (type == null) {
            throw new NullPointerException("type is marked non-null but is null");
        }
        return new Injectable<T>(name, object, type);
    }
    
    public String getName() {
        return this.name;
    }
    
    public T getObject() {
        return this.object;
    }
    
    public Class<T> getType() {
        return this.type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Injectable)) {
            return false;
        }
        final Injectable<?> other = (Injectable<?>)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$object = this.getObject();
        final Object other$object = other.getObject();
        Label_0102: {
            if (this$object == null) {
                if (other$object == null) {
                    break Label_0102;
                }
            }
            else if (this$object.equals(other$object)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        if (this$type == null) {
            if (other$type == null) {
                return true;
            }
        }
        else if (this$type.equals(other$type)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Injectable;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $object = this.getObject();
        result = result * 59 + (($object == null) ? 43 : $object.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Injectable(name=" + this.getName() + ", object=" + this.getObject() + ", type=" + (Object)this.getType() + ")";
    }
    
    private Injectable(final String name, final T object, final Class<T> type) {
        this.name = name;
        this.object = object;
        this.type = type;
    }
}
