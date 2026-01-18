package cc.dreamcode.kowal.libs.net.kyori.adventure.key;

import cc.dreamcode.kowal.libs.net.kyori.examination.ExaminableProperty;
import java.util.stream.Stream;
import java.util.OptionalInt;
import org.jetbrains.annotations.Nullable;
import java.util.Comparator;
import org.jetbrains.annotations.NotNull;
import cc.dreamcode.kowal.libs.net.kyori.examination.Examinable;

public interface Key extends Comparable<Key>, Examinable, Namespaced, Keyed
{
    public static final String MINECRAFT_NAMESPACE = "minecraft";
    public static final char DEFAULT_SEPARATOR = ':';
    
    @NotNull
    default Key key(@NotNull @KeyPattern final String string) {
        return key(string, ':');
    }
    
    @NotNull
    default Key key(@NotNull final String string, final char character) {
        final int index = string.indexOf((int)character);
        final String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
        final String value = (index >= 0) ? string.substring(index + 1) : string;
        return key(namespace, value);
    }
    
    @NotNull
    default Key key(@NotNull final Namespaced namespaced, @NotNull @KeyPattern.Value final String value) {
        return key(namespaced.namespace(), value);
    }
    
    @NotNull
    default Key key(@NotNull @KeyPattern.Namespace final String namespace, @NotNull @KeyPattern.Value final String value) {
        return new KeyImpl(namespace, value);
    }
    
    @NotNull
    default Comparator<? super Key> comparator() {
        return KeyImpl.COMPARATOR;
    }
    
    default boolean parseable(@Nullable final String string) {
        if (string == null) {
            return false;
        }
        final int index = string.indexOf(58);
        final String namespace = (index >= 1) ? string.substring(0, index) : "minecraft";
        final String value = (index >= 0) ? string.substring(index + 1) : string;
        return parseableNamespace(namespace) && parseableValue(value);
    }
    
    default boolean parseableNamespace(@NotNull final String namespace) {
        return !checkNamespace(namespace).isPresent();
    }
    
    @NotNull
    default OptionalInt checkNamespace(@NotNull final String namespace) {
        for (int i = 0, length = namespace.length(); i < length; ++i) {
            if (!allowedInNamespace(namespace.charAt(i))) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
    
    default boolean parseableValue(@NotNull final String value) {
        return !checkValue(value).isPresent();
    }
    
    @NotNull
    default OptionalInt checkValue(@NotNull final String value) {
        for (int i = 0, length = value.length(); i < length; ++i) {
            if (!allowedInValue(value.charAt(i))) {
                return OptionalInt.of(i);
            }
        }
        return OptionalInt.empty();
    }
    
    default boolean allowedInNamespace(final char character) {
        return KeyImpl.allowedInNamespace(character);
    }
    
    default boolean allowedInValue(final char character) {
        return KeyImpl.allowedInValue(character);
    }
    
    @NotNull
    @KeyPattern.Namespace
    String namespace();
    
    @NotNull
    @KeyPattern.Value
    String value();
    
    @NotNull
    String asString();
    
    @NotNull
    default String asMinimalString() {
        if (this.namespace().equals((Object)"minecraft")) {
            return this.value();
        }
        return this.asString();
    }
    
    @NotNull
    default Stream<? extends ExaminableProperty> examinableProperties() {
        return (Stream<? extends ExaminableProperty>)Stream.of((Object[])new ExaminableProperty[] { ExaminableProperty.of("namespace", this.namespace()), ExaminableProperty.of("value", this.value()) });
    }
    
    default int compareTo(@NotNull final Key that) {
        return comparator().compare((Object)this, (Object)that);
    }
    
    @NotNull
    default Key key() {
        return this;
    }
}
