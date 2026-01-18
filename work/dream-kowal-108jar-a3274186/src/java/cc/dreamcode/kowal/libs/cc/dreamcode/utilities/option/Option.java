package cc.dreamcode.kowal.libs.cc.dreamcode.utilities.option;

import lombok.Generated;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.Optional;
import lombok.NonNull;

public final class Option<V>
{
    private final V value;
    
    public Option() {
        this.value = null;
    }
    
    public Option(@NonNull final V value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        this.value = value;
    }
    
    public Option(@NonNull final Optional<V> optionalValue) {
        if (optionalValue == null) {
            throw new NullPointerException("optionalValue is marked non-null but is null");
        }
        this.value = (V)optionalValue.orElse((Object)null);
    }
    
    public static <T> Option<T> empty() {
        return new Option<T>();
    }
    
    public static <T> Option<T> of(@NonNull final T value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return new Option<T>(value);
    }
    
    public static <T> Option<T> ofNullable(final T value) {
        return (Option<T>)((value == null) ? empty() : of((Object)value));
    }
    
    public static <T> Option<T> ofOptional(@NonNull final Optional<T> optionalValue) {
        if (optionalValue == null) {
            throw new NullPointerException("optionalValue is marked non-null but is null");
        }
        return new Option<T>(optionalValue);
    }
    
    public boolean isPresent() {
        return this.value != null;
    }
    
    public boolean isEmpty() {
        return this.value == null;
    }
    
    public V get() {
        return this.value;
    }
    
    public V getOrElse(final V value) {
        return this.isPresent() ? this.get() : value;
    }
    
    public V getOrInvokeElse(@NonNull final Supplier<V> value) {
        if (value == null) {
            throw new NullPointerException("value is marked non-null but is null");
        }
        return (V)(this.isPresent() ? this.get() : value.get());
    }
    
    public V getOrNull() {
        return this.getOrElse(null);
    }
    
    public V getOrThrow() throws NoSuchElementException {
        if (this.isPresent()) {
            return this.get();
        }
        throw new NoSuchElementException("No optional result present");
    }
    
    public <X extends Throwable> V getOrThrow(@NonNull final Supplier<X> supplier) throws X, Throwable {
        if (supplier == null) {
            throw new NullPointerException("supplier is marked non-null but is null");
        }
        if (this.isPresent()) {
            return this.get();
        }
        throw (Throwable)supplier.get();
    }
    
    public Option<V> ifPresent(@NonNull final Consumer<? super V> consumer) {
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        if (this.isEmpty()) {
            return this;
        }
        consumer.accept((Object)this.value);
        return this;
    }
    
    public Option<V> ifPresentOrElse(@NonNull final Consumer<? super V> consumer, @NonNull final Runnable runnable) {
        if (consumer == null) {
            throw new NullPointerException("consumer is marked non-null but is null");
        }
        if (runnable == null) {
            throw new NullPointerException("runnable is marked non-null but is null");
        }
        if (this.isEmpty()) {
            runnable.run();
            return this;
        }
        consumer.accept((Object)this.value);
        return this;
    }
    
    public Stream<V> stream() {
        return (Stream<V>)Stream.of((Object)this.value);
    }
    
    public Optional<V> toOptional() {
        return (Optional<V>)Optional.ofNullable((Object)this.value);
    }
    
    @Generated
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Option)) {
            return false;
        }
        final Option<?> other = (Option<?>)o;
        final Object this$value = this.value;
        final Object other$value = other.value;
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
    
    @Generated
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $value = this.value;
        result = result * 59 + (($value == null) ? 43 : $value.hashCode());
        return result;
    }
    
    @Generated
    @Override
    public String toString() {
        return "Option(value=" + (Object)this.value + ")";
    }
}
